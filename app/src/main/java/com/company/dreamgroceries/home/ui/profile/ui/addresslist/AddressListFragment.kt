package com.company.dreamgroceries.home.ui.profile.ui.addresslist

import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.dreamgroceries.MyApp
import com.company.dreamgroceries.R
import com.company.dreamgroceries.addorder.AddOrderActivity
import com.company.dreamgroceries.base.BaseFragment
import com.company.dreamgroceries.data.AddressResponse
import com.company.dreamgroceries.data.Addresse
import com.company.dreamgroceries.data.PlaceOrderResponse
import com.company.dreamgroceries.databinding.AddressListFragmentBinding
import com.company.dreamgroceries.extensions.getViewModelFactory
import com.company.dreamgroceries.home.ui.profile.*
import com.company.dreamgroceries.utilities.*
import com.company.dreamgroceries.webservices.Resource
import com.company.dreamgroceries.webservices.Status


class AddressListFragment : BaseFragment(), AddressClickListener {

    companion object {
        fun newInstance(isFromProfile: Int): AddressListFragment {
            val addressListFragment = AddressListFragment()
            val bundle = Bundle()
            bundle.putInt(BUN_1, isFromProfile)
            addressListFragment.arguments = bundle
            return addressListFragment
        }
    }

    private val EMPTY_DATA = 1
    private var isFromProfile: Int = 0
    val args: AddAddressFragmentArgs by navArgs()

    private lateinit var mAdapter: AddressListAdapter
    private lateinit var mBinding: AddressListFragmentBinding
    private val viewModel by lazy {
        requireActivity().getViewModelFactory<AddressViewModel>()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = AddressListFragmentBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    private fun showConfirmationDialog() {
        Utils.showDialog(
            requireContext(),
            getString(R.string.app_name),
            getString(R.string.verify_order_texr),
            getString(R.string.confirm_order),
            getString(R.string.edit_order),
            DialogInterface.OnClickListener { dialogInterface, i -> getCurrentAddressAndPlaceOrder() },
            DialogInterface.OnClickListener { dialogInterface, i -> saveDataState() },
            getString(R.string.cancel)

        )
    }

    private fun saveDataState() {
        //activity?.onBackPressed()

        mBinding.viewNext.findNavController().previousBackStackEntry?.savedStateHandle?.set(
            "key",
            args.productList.toList()
        )
        mBinding.viewNext.findNavController().previousBackStackEntry?.savedStateHandle?.set(
            "key2",
            args.addInst
        )
        mBinding.viewNext.findNavController().popBackStack(R.id.manualOrderFragment, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            isFromProfile = it.getInt(BUN_1, 0)
        }

        if (isFromProfile == 1) {
            mBinding.tvTitle.text = getString(R.string.manage_address)
        } else {
            mBinding.tvTitle.text = getString(R.string.select_address)
        }


        mBinding.rvList.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = AddressListAdapter(this)
        mBinding.rvList.adapter = mAdapter


        if (requireActivity() is AddOrderActivity) {
            mBinding.viewNext.visibility = View.VISIBLE
            mBinding.tvSubmit.visibility = View.VISIBLE
        } else {
            mBinding.viewNext.visibility = View.GONE
            mBinding.tvSubmit.visibility = View.GONE
        }

        mBinding.viewNext.setOnClickListener {
            if (requireActivity() is AddOrderActivity) {
                 showConfirmationDialog()
            }
        }

        viewModel.getAddresses().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                mBinding.rvList.visibility = View.VISIBLE
                mBinding.llNoData.visibility = View.GONE
                mAdapter.submitList(it)
            } else {
                mBinding.rvList.visibility = View.GONE
                showNoDataView(EMPTY_DATA)
            }

        })

        mBinding.ivBack.setOnClickListener {
            activity?.onBackPressed()

        }

        mBinding.ivAdd.setOnClickListener {
            startAddAddress()
        }

    }

    private fun startAddAddress() {
        val intent = Intent(requireActivity(), AddAddressActivity::class.java)
        startActivityForResult(intent, REQUEST_UPDATE_ADDRESS)
    }

    private fun showNoDataView(type: Int) {
        mBinding.llNoData.visibility = View.VISIBLE
        when (type) {
            EMPTY_DATA -> {
                setNoDataViewsText(
                    R.drawable.ic_no_data,
                    getString(R.string.no_address_added),
                    getString(R.string.semms_like_you_not_added_address)
                )
                mBinding.noData.btnNext.text = getString(R.string.add_address)
                mBinding.noData.btnNext.setOnClickListener {
                    startAddAddress()
                }

            }

        }

    }

    private fun setNoDataViewsText(
        icon: Int,
        title: String,
        desc: String
    ) {
        mBinding.noData.ivView.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                icon
            )
        )
        mBinding.noData.tvTitle.text = title
        mBinding.noData.tvDesc.text = desc
    }

    private fun getCurrentAddressAndPlaceOrder() {

        MyApp.getInstance().addressDao().geCurrentAddress()
            .observe(viewLifecycleOwner,
                androidx.lifecycle.Observer {
                    if (it != null) {
                        viewModel.placeManualDelOrder(
                            viewModel.createManualDelOrderReq(
                                it.id,
                                args.productList,
                                args.addInst
                            )
                        ).observe(
                            viewLifecycleOwner,
                            androidx.lifecycle.Observer {
                                handlePlaceOrder(
                                    it
                                )
                            })
                    } else {
                        Utils.showToast(
                            requireContext(),
                            getString(R.string.plesae_select_address_to_deliver)
                        )
                    }

                })

    }

    private fun handlePlaceOrder(resource: Resource<PlaceOrderResponse>?) {
        when (resource?.status) {
            Status.SUCCESS -> {
                hideProgress(mBinding.progressBar)

                resource.data?.let {
                    val action =
                        AddressListFragmentDirections.actionAddressListFragmentToThanksFragment(it.order_no)
                    mBinding.viewNext.findNavController().navigate(action)
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

    override fun onAddressClicked(address: Addresse, type: Int) {
        when (type) {
            ADDRESS_EDIT -> openAddressEdit(address)
            ADDRESS_DELETE -> deleteAddress(address)
        }
    }

    override fun onAddressSelected(address: Addresse) {
        viewModel.updateAddress(address)
            .observe(viewLifecycleOwner, Observer { handleDeleteResponse(it, true) })
    }

    private fun deleteAddress(address: Addresse) {
        Utils.showDialog(
            requireContext(),
            getString(R.string.delete_address),
            getString(R.string.delete_address_desc),
            getString(R.string.ok),
            getString(R.string.cancel),
            DialogInterface.OnClickListener { dialogInterface, i -> callDeleteAddress(address) },
            null
        )
    }

    private fun callDeleteAddress(address: Addresse) {
        viewModel.deleteAddress(address)
            .observe(requireActivity(), Observer { handleDeleteResponse(it, false) })
    }

    private fun handleDeleteResponse(
        resource: Resource<AddressResponse>?,
        isUpdateAddress: Boolean
    ) {
        when (resource?.status) {
            Status.SUCCESS -> {
                AsyncTask.execute {
                    resource.data?.data?.let {
                        MyApp.getInstance().addressDao().deleteAll()
                        MyApp.getInstance().addressDao().insertAddress(
                            it
                        )
                    }
                }
                hideProgress(mBinding.progressBar)
                Utils.showToastShort(requireContext(), resource.data?.message)
                if (isUpdateAddress && isFromProfile == 0 && requireActivity() is AddressListActivity) {
                    requireActivity().onBackPressed()
                }
                /* if (mAdapter.itemCount == 0)
                     showNoDataView(EMPTY_DATA)*/

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

    private fun openAddressEdit(address: Addresse) {
        val intent = Intent(requireActivity(), AddAddressActivity::class.java)
        intent.putExtra(BUN_1, address)
        startActivityForResult(intent, REQUEST_UPDATE_ADDRESS)
    }


}
