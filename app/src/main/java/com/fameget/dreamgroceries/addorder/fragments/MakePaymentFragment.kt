package com.fameget.dreamgroceries.addorder.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.fameget.dreamgroceries.MyApp
import com.fameget.dreamgroceries.addorder.ProductsViewModel
import com.fameget.dreamgroceries.base.BaseFragment
import com.fameget.dreamgroceries.data.BaseResponse
import com.fameget.dreamgroceries.data.BrowseDelOrderReq
import com.fameget.dreamgroceries.data.BrowsePickUpOrderReq
import com.fameget.dreamgroceries.data.PlaceOrderResponse
import com.fameget.dreamgroceries.databinding.FragmentMakePaymentBinding
import com.fameget.dreamgroceries.extensions.*
import com.fameget.dreamgroceries.utilities.*
import com.fameget.dreamgroceries.webservices.Resource
import com.fameget.dreamgroceries.webservices.Status

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MakePaymentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MakePaymentFragment : BaseFragment() {
    private lateinit var makePaymentFragmentListener: MakePaymentFragmentListener
    private lateinit var mBinding: FragmentMakePaymentBinding

    val args: MakePaymentFragmentArgs by navArgs()

    val viewModel by lazy {
        requireActivity().getViewModelFactory<ProductsViewModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        makePaymentFragmentListener = activity as MakePaymentFragmentListener

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentMakePaymentBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mBinding.ivBack.setOnClickListener { activity?.onBackPressed() }

        mBinding.viewPay.setOnClickListener {

            if (validateInputs()) {

                viewModel.addProductsToCart(args.productList.toList()).observe(viewLifecycleOwner,
                    Observer { handleAddToCartResponse(it) })
            }


        }

        mBinding.ivBack.setOnClickListener { activity?.onBackPressed() }
    }


    private fun handleAddToCartResponse(resource: Resource<BaseResponse>?) {
        when (resource?.status) {
            Status.SUCCESS -> {

                if (makePaymentFragmentListener.getType() == ORD_DILVRY) {
                    viewModel.placeBrowseDeliveryCODOrder(getBrowseDeliveryOnlineReq())
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

    private fun getBrowsePickUpCODReq(): BrowsePickUpOrderReq {
        return BrowsePickUpOrderReq(args.time, ORDER_BROWSE_PICKUP, PAYMENT_COD)
    }

    private fun handlePlaceOrder(resource: Resource<PlaceOrderResponse>?) {
        when (resource?.status) {
            Status.SUCCESS -> {
                hideProgress(mBinding.progressBar)
                AsyncTask.execute { MyApp.getInstance().cartDao().deleteAll() }
                resource.data?.let {
                    val action =
                        MakePaymentFragmentDirections.actionMakePaymentFragmentToThanksFragment(it.order_no)
                    mBinding.viewPay.findNavController().navigate(action)
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

    private fun getBrowseDeliveryOnlineReq(): BrowseDelOrderReq {
        val addressId = args.addressId
        return BrowseDelOrderReq(addressId, ORDER_BROWSE_DELIVERY, PAYMENT_ONLINE)
    }


    private fun validateInputs(): Boolean {
        if (mBinding.tiCardNo.isTilEmpty() || mBinding.tiNameCard.isTilEmpty() || mBinding.tiExp.isTilEmpty() || mBinding.tiCVV.isTilEmpty()
            || mBinding.tiCardNo.isTilNotValidCard() || mBinding.tiExp.isValidExp() || mBinding.tiCVV.isValidCVV()
        ) {
            return false
        }
        return true
    }

    interface MakePaymentFragmentListener {
        fun getType(): Int?
    }


}
