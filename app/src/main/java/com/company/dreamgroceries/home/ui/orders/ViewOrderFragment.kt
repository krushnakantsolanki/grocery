package com.company.dreamgroceries.home.ui.orders

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.dreamgroceries.R
import com.company.dreamgroceries.base.BaseFragment
import com.company.dreamgroceries.customviews.CFEditText
import com.company.dreamgroceries.data.*
import com.company.dreamgroceries.databinding.FragmentViewOrdersBinding
import com.company.dreamgroceries.extensions.getViewModelFactory
import com.company.dreamgroceries.extensions.toFormattedDate
import com.company.dreamgroceries.home.ui.profile.AddressListAdapter
import com.company.dreamgroceries.utilities.*
import com.company.dreamgroceries.webservices.Resource
import com.company.dreamgroceries.webservices.Status
import com.google.android.material.textfield.TextInputLayout


/**
 * A simple [Fragment] subclass.
 * Use the [ViewCartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewOrderFragment : BaseFragment() {
    private var orderNo: String? = null
    private var mAddressCount: Int? = 0
    private lateinit var mAddressAdapter: AddressListAdapter
    private lateinit var mAdapter: ViewOrderProductsAdapter
    private lateinit var mBinding: FragmentViewOrdersBinding

    val viewModel by lazy {
        requireActivity().getViewModelFactory<OrdersViewModel>()
    }


    // TODO: Rename and change types of parameters
    private var order: Order? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            order = it.getParcelable<Order>(BUN_1)
            orderNo = it.getString(BUN_2)

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentViewOrdersBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (order != null) {

            showOrderDetails()
        } else {
            orderNo?.run {
                viewModel.getOrder(OrderReq(this))
                    .observe(viewLifecycleOwner, Observer { handleOrderResponse(it) })
            }


        }

        mBinding.ivBack.setOnClickListener { requireActivity().onBackPressed() }


    }

    private fun handleOrderResponse(it: Resource<OrderResponse>?) {
        it?.let { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    order = resource.data?.data
                    showOrderDetails()
                    hideProgress(mBinding.progressBar)

                }
                Status.ERROR -> {
                    hideProgress(mBinding.progressBar)
                    Utils.showToast(requireContext(), resource.message)
                }
                Status.LOADING -> {
                    showProgress(mBinding.progressBar)
                }
            }
        }
    }

    private fun showOrderDetails() {
        showOrderHeader()

        if (order?.order_type == ORDER_BROWSE_DELIVERY || order?.order_type == ORDER_BROWSE_PICKUP)
            showCartAndPriceDetails()
        else
            showManualProductList()

        if (order?.order_type == ORDER_BROWSE_DELIVERY || order?.order_type == ORDER_MANUAL_DELIVERY) {
            showDeliveryAddress()
        } else {
            showSchedulePickUpTime()
        }
    }

    private fun showSchedulePickUpTime() {
        mBinding.tvAChooseAddress.visibility = View.GONE
        mBinding.rvAddress.visibility = View.GONE
        mBinding.tvScdPickUpLbl.visibility = View.VISIBLE
        mBinding.tvScdValue.visibility = View.VISIBLE

        mBinding.tvScdValue.setText(order?.pickup_date?.toFormattedDate())

    }

    private fun showDeliveryAddress() {
        mBinding.tvAChooseAddress.visibility = View.VISIBLE
        mBinding.rvAddress.visibility = View.VISIBLE
        mBinding.tvScdPickUpLbl.visibility = View.GONE
        mBinding.tvScdValue.visibility = View.GONE

        mBinding.rvAddress.layoutManager =
            LinearLayoutManager(activity)
        mAddressAdapter = AddressListAdapter(null)
        mBinding.rvAddress.adapter = mAddressAdapter
        val addressList = ArrayList<Addresse>()
        order?.order_address?.run {
            addressList.add(this)
            mAddressAdapter.submitList(addressList)
        }


    }

    private fun showManualProductList() {
        mBinding.tvBillDetails.visibility = View.GONE
        mBinding.cardBill.visibility = View.GONE
        mBinding.viewRemoveClick.setOnClickListener { }
        showManualOrders()

        showAdditionalInsts()

    }

    private fun showAdditionalInsts() {
        if (order?.additional_instructions.isNullOrEmpty()) {
            mBinding.tvAddInst.visibility = View.GONE
            mBinding.tvAddInstValue.visibility = View.GONE
        } else {
            mBinding.tvAddInst.visibility = View.VISIBLE
            mBinding.tvAddInstValue.visibility = View.VISIBLE
            mBinding.tvAddInstValue.text = order?.additional_instructions
        }
    }

    private fun showManualOrders() {
        order?.order_manual_products?.run {

            for (manualOrder in this) {
                val view = LayoutInflater.from(activity).inflate(R.layout.add_item, null, false)
                val editText = view.findViewById<CFEditText>(R.id.edtItem)

                editText.setText(manualOrder.product_name)
                editText.isClickable = false
                editText.setPadding(
                    resources.getDimensionPixelSize(R.dimen.dp_16),
                    resources.getDimensionPixelSize(R.dimen.dp_16),
                    resources.getDimensionPixelSize(R.dimen.dp_16),
                    resources.getDimensionPixelSize(R.dimen.dp_16)
                )
                view.findViewById<TextInputLayout>(R.id.tiItem).isClickable = false
                mBinding.llAddView.addView(view)
            }

        }
        if (mBinding.llAddView.childCount > 0) {
            mBinding.llAddView.visibility = View.VISIBLE
            if (order?.order_type == ORDER_BROWSE_PICKUP || order?.order_type == ORDER_BROWSE_DELIVERY) {
                mBinding.tvItemsNotAvailable.visibility = View.VISIBLE
            }
        }


    }

    private fun showCartAndPriceDetails() {
        mBinding.tvAddInst.visibility = View.GONE
        mBinding.tvAddInstValue.visibility = View.GONE
        mBinding.llAddView.visibility = View.GONE
        mBinding.rvProducts.layoutManager =
            LinearLayoutManager(activity)
        mAdapter = ViewOrderProductsAdapter()
        mBinding.rvProducts.adapter = mAdapter
        mAdapter.submitList(order?.order_products)
        mBinding.tvSubTotal.text = "$CURR${order?.order_details?.sub_total}"
        mBinding.tvTotalTax.text = "$CURR${order?.order_details?.tax_amt}"
        mBinding.tvTotalPrice.text = "$CURR${order?.order_details?.total_amt}"


        showManualOrders()
        showAdditionalInsts()


    }

    private fun showOrderHeader() {
        order?.run {
            val mView = mBinding.layoutMyOrder

            mView.tvOrderId.text = "#$order_no"


            if (order_type == ORDER_BROWSE_DELIVERY || order_type == ORDER_BROWSE_PICKUP) {
                mView.tvPriceQun.text = "$CURR${order_details?.total_amt}"
                mView.tvPriceQun.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
                mView.tvPriceQun.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.txt_14)
                )
                if (status == DELIVERED || status == PICKED_UP) {
                    mView.tvDownloadInvoice.visibility = View.VISIBLE
                    mView.tvDownloadInvoice.setOnClickListener {

                        val browserIntent = Intent(Intent.ACTION_VIEW)
                        browserIntent.setDataAndType(
                            Uri.parse(invoice),
                            "application/pdf"
                        )

                        if (isActivityForIntentAvailable(requireContext(), browserIntent)) {
                            val chooser =
                                Intent.createChooser(browserIntent, "Open with")
                            chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK // optional
                            startActivity(chooser)
                        } else {
                            val i = Intent(Intent.ACTION_VIEW)
                            i.data = Uri.parse(invoice)
                            startActivity(i)
                        }
                    }
                }
            }

            Utils.setOrderStatusAndColor(mView.tvOrderStatus, status)
            mView.tvTime.text = created_at.toFormattedDate().replace("am", "AM").replace("pm", "PM")





            if (status == PENDING || status == CONFIRM || status == IN_PROCESS || status == READY) {
                mView.tvCancelOrder.visibility = View.VISIBLE
                mView.tvCancelOrder.setOnClickListener {
                    order?.run { onCancelOrder(order_no) }

                }
            } else
                mView.tvCancelOrder.visibility = View.GONE

        }
    }

    fun isActivityForIntentAvailable(
        context: Context,
        intent: Intent?
    ): Boolean {
        val packageManager = context.packageManager
        val list: List<*> =
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return list.size > 0
    }

    fun onCancelOrder(order: String) {
        Utils.showDialog(
            requireContext(),
            getString(R.string.cancel_order),
            getString(R.string.are_sure_cancel_order),
            getString(R.string.yes),
            getString(R.string.no),
            DialogInterface.OnClickListener { dialogInterface, i ->
                viewModel.cancelOrder(order)
                    .observe(
                        viewLifecycleOwner,
                        Observer { handleResponseCancelOrder(it) })
            },
            null
        )
    }

    private fun handleResponseCancelOrder(it: Resource<BaseResponse>?) {
        it?.let { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    hideProgress(mBinding.progressBar)
                    Utils.showToastShort(requireContext(), it.data?.message)
                    requireActivity().setResult(RESULT_OK)
                    order?.status = ORDER_CANCELLED
                    order?.let {
                        Utils.setOrderStatusAndColor(
                            mBinding.layoutMyOrder.tvOrderStatus,
                            it.status
                        )
                    }
                    val intent = Intent()
                    intent.putExtra(BUN_1, order?.id)
                    requireActivity().setResult(RESULT_OK, intent)
                    //requireActivity().onBackPressed()

                }
                Status.ERROR -> {
                    hideProgress(mBinding.progressBar)
                    Utils.showToast(requireContext(), resource.message)
                }
                Status.LOADING -> {
                    showProgress(mBinding.progressBar)
                }
            }
        }
    }

}
