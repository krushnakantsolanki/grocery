package com.company.dreamgroceries.addorder.fragments

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.dreamgroceries.MyApp
import com.company.dreamgroceries.R
import com.company.dreamgroceries.addorder.ProductsViewModel
import com.company.dreamgroceries.base.BaseFragment
import com.company.dreamgroceries.data.*
import com.company.dreamgroceries.databinding.FragmentViewCartBinding
import com.company.dreamgroceries.extensions.getViewModelFactory
import com.company.dreamgroceries.home.ui.orders.ViewCartProductsAdapter
import com.company.dreamgroceries.home.ui.orders.ViewCarttemClickListener
import com.company.dreamgroceries.home.ui.profile.AddAddressActivity
import com.company.dreamgroceries.home.ui.profile.AddressListActivity
import com.company.dreamgroceries.home.ui.profile.AddressListAdapter
import com.company.dreamgroceries.utilities.*
import com.company.dreamgroceries.webservices.Resource
import com.company.dreamgroceries.webservices.Status
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewCartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewCartFragment : BaseFragment(), ViewCarttemClickListener {
    private var mAddressCount: Int? = 0
    private lateinit var mAddressAdapter: AddressListAdapter
    private lateinit var mAdapter: ViewCartProductsAdapter
    private lateinit var viewCartFragmentListner: ViewCartFragmentListener
    private lateinit var mBinding: FragmentViewCartBinding

    val viewModel by lazy {
        requireActivity().getViewModelFactory<ProductsViewModel>()
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewCartFragmentListner = activity as ViewCartFragmentListener

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentViewCartBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initUi()

        handleClickListeners()

        viewModel.getCartProducts().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.isEmpty()) {
                    if (isAdded)
                        requireActivity().onBackPressed()
                } else {
                    if (isAdded) {
                        mAdapter.submitList(it)
                        mBinding.tvSubTotal.text = "$CURR${Utils.getTotalPrice(it)}"
                        mBinding.tvTotalTax.text = "$CURR${Utils.getTotalTax(it)}"
                        mBinding.tvTotalPrice.text = "$CURR${getFinalPrice(it)}"
                    }
                }
            }
        })

        if (viewCartFragmentListner.getType() == ORD_DILVRY) {
            viewModel.getCurrentAddress().observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    mBinding.tvAChooseAddress.visibility = View.VISIBLE
                    it.is_selected = 0
                    val list = ArrayList<Addresse>()
                    list.add(it)
                    mAddressAdapter.submitList(list)
                    mBinding.tvAddAddress.text = getString(R.string.change_address)
                } else {
                    mBinding.tvAChooseAddress.visibility = View.GONE
                    mBinding.tvAddAddress.text = getString(R.string.add_address)
                    mAddressAdapter.submitList(ArrayList())
                }
            })

            viewModel.getAddressCount().observe(viewLifecycleOwner, Observer {
                mAddressCount = it
            })

        }
    }

    private fun getFinalPrice(it: List<Product>): String {
        val price =
            Utils.getTotalPrice(it) + Utils.getTotalTax(it)
                .toDouble()
        return "%.2f".format(price)

    }

    private fun openCaleder() {
        val currentDate: Calendar = Calendar.getInstance()
        val date = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                date.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(
                    requireContext(),
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        date.set(Calendar.MINUTE, minute)
                        if (date.time.time > System.currentTimeMillis())
                            mBinding.tvScdValue.setText(formatDate(date.time)?.replace("am", "AM")?.replace("pm", "PM"))
                        else {
                            Utils.showToast(
                                requireContext(),
                                getString(R.string.pickup_time_not_valid)
                            )
                            openCaleder()
                        }
                    },
                    currentDate.get(Calendar.HOUR_OF_DAY),
                    currentDate.get(Calendar.MINUTE),
                    false
                ).show()
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DATE)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun formatDate(date: Date): String {
        val format = SimpleDateFormat("dd MMM yyyy hh:mm aa")
        return format.format(date)
    }

    private fun handleClickListeners() {
        mBinding.tvScdPickUp.setOnClickListener {
            /*val action = ViewCartFragmentDirections.actionViewCartFragmentToSchedulePickupFragment()
            mBinding.tvScdPickUp.findNavController().navigate(action)*/
            /*val intent = Intent(activity, SchedulePickUpActivity::class.java)
            startActivityForResult(intent, REQUEST_SCHEDULE_PICKUP)
            activity?.overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )*/
            openCaleder()

        }


        mBinding.tvAddAddress.setOnClickListener {

            if (mAddressCount == 0) {
                val intent = Intent(activity, AddAddressActivity::class.java)
                startActivity(intent)

            } else {
                val intent = Intent(activity, AddressListActivity::class.java)
                startActivity(intent)
            }
            activity?.overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )

        }

        mBinding.viewPayLater.setOnClickListener {
            /*val action =
                ViewCartFragmentDirections.actionViewCartFragmentToThanksFragment()
            mBinding.viewPayLater.findNavController().navigate(action)*/
            if (validateInputs()) {
                viewModel.addProductsToCart(mAdapter.currentList).observe(viewLifecycleOwner,
                    Observer { handleAddToCartResponse(it) })

            }


        }

        mBinding.viewPayNow.setOnClickListener {
            //  viewModel.productsList = mAdapter.currentList
            //   viewModel.selectedAddressId = mAddressAdapter.currentList[0].id


            if (validateInputs()) {
                if (viewCartFragmentListner.getType() == ORD_DILVRY) {
                    val action =
                        ViewCartFragmentDirections.actionViewCartFragmentToMakePaymentFragment(
                            mAdapter.currentList.toTypedArray(),
                            mAddressAdapter.currentList[0].id,
                            ""

                        )
                    mBinding.viewPayLater.findNavController().navigate(action)
                } else {
                    viewModel.pickUpTime = mBinding.tvScdValue.text.toString()
                    val pickUp = Utils.getTimeInSeconds(mBinding.tvScdValue.text.toString())
                    val action =
                        ViewCartFragmentDirections.actionViewCartFragmentToMakePaymentFragment(
                            mAdapter.currentList.toTypedArray(), 0, pickUp.toString()
                        )


                    mBinding.viewPayLater.findNavController().navigate(action)
                }
            }
        }

        mBinding.ivBack.setOnClickListener { activity?.onBackPressed() }

    }

    private fun handleAddToCartResponse(resource: Resource<BaseResponse>?) {
        when (resource?.status) {
            Status.SUCCESS -> {

                if (viewCartFragmentListner.getType() == ORD_DILVRY) {
                    viewModel.placeBrowseDeliveryCODOrder(getBrowseDeliveryCODReq())
                        .observe(viewLifecycleOwner,
                            Observer { handlePlaceOrder(it) })
                } else {
                    viewModel.placeBrowseDeliveryPickUpOrder(getBrowsePickUpCODReq())
                        .observe(viewLifecycleOwner,
                            Observer { handlePlaceOrder(it) })
                }

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

    private fun handlePlaceOrder(resource: Resource<PlaceOrderResponse>?) {
        when (resource?.status) {
            Status.SUCCESS -> {
                hideProgress(mBinding.progressBar)
                AsyncTask.execute { MyApp.getInstance().cartDao().deleteAll() }
                resource.data?.let {
                    val action =
                        ViewCartFragmentDirections.actionViewCartFragmentToThanksFragment(it.order_no)
                    mBinding.viewPayLater.findNavController().navigate(action)
                }

                Utils.showToastShort(requireContext(), resource.data?.message)
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

    private fun getBrowseDeliveryCODReq(): BrowseDelOrderReq {
        val addressId = mAddressAdapter.currentList[0].id
        return BrowseDelOrderReq(addressId, ORDER_BROWSE_DELIVERY, PAYMENT_COD)
    }

    private fun getBrowsePickUpCODReq(): BrowsePickUpOrderReq {
        val pickUp = Utils.getTimeInSeconds(mBinding.tvScdPickUp.text.toString())
        return BrowsePickUpOrderReq(pickUp.toString(), ORDER_BROWSE_PICKUP, PAYMENT_COD)
    }

    private fun validateInputs(): Boolean {
        if (mAdapter.itemCount == 0) {
            Utils.showToastShort(requireContext(), getString(R.string.cart_is_empty))
            return false
        } else if (viewCartFragmentListner.getType() == ORD_DILVRY) {
            if (mAddressAdapter.itemCount == 0) {
                Utils.showToastShort(
                    requireContext(),
                    getString(R.string.no_delivert_address_selected)
                )
                return false
            }
        } else if (viewCartFragmentListner.getType() == ORD_PICK) {
            if (TextUtils.isEmpty(mBinding.tvScdValue.text.toString())) {
                Utils.showToastShort(
                    requireContext(),
                    getString(R.string.no_pick_time_selected)
                )
                return false
            }


        }
        return true
    }

    private fun initUi() {
        mBinding.rvProducts.layoutManager =
            LinearLayoutManager(activity)
        mAdapter = ViewCartProductsAdapter(this)
        mBinding.rvProducts.adapter = mAdapter

        mBinding.rvAddress.layoutManager =
            LinearLayoutManager(activity)
        mAddressAdapter = AddressListAdapter(null)
        mBinding.rvAddress.adapter = mAddressAdapter

        if (viewCartFragmentListner.getType() == ORD_PICK) {
            hideShowScdView(true)
            hideShowAddAddressView(false)
        } else {
            hideShowScdView(false)
            hideShowAddAddressView(true)
        }
    }

    private fun hideShowAddAddressView(show: Boolean) {
        if (show) {
            mBinding.rvAddress.visibility = View.VISIBLE
            mBinding.tvAChooseAddress.visibility = View.VISIBLE
            mBinding.tvAddAddress.visibility = View.VISIBLE
        } else {
            mBinding.rvAddress.visibility = View.GONE
            mBinding.tvAChooseAddress.visibility = View.GONE
            mBinding.tvAddAddress.visibility = View.GONE
        }
    }

    private fun hideShowScdView(show: Boolean) {
        if (show) {
            mBinding.tvScdPickUp.visibility = View.VISIBLE
            mBinding.tvScdPickUpLbl.visibility = View.VISIBLE
            mBinding.tvScdValue.text = viewModel.pickUpTime
        } else {
            mBinding.tvScdPickUp.visibility = View.GONE
            mBinding.tvScdPickUpLbl.visibility = View.GONE
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewCartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewCartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    interface ViewCartFragmentListener {
        fun getType(): Int?
    }

    override fun removeProduct(product: Product) {

        Utils.showDialog(
            requireContext(),
            getString(R.string.remove_product),
            getString(R.string.remove_product_desc),
            getString(R.string.yes),
            getString(R.string.no),
            DialogInterface.OnClickListener { _, _ ->
                AsyncTask.execute { MyApp.getInstance().cartDao().deleteProduct(product) }
            },
            null
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SCHEDULE_PICKUP && resultCode == RESULT_OK) {
            val scdDate = data?.getStringExtra(BUN_1)
            mBinding.tvScdValue.text = scdDate
        }
    }

    override fun updateProduct(product: Product) {
        AsyncTask.execute { MyApp.getInstance().cartDao().updateProduct(product) }
    }
}
