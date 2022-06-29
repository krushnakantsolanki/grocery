package com.company.dreamgroceries.home.ui.profile

import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.company.dreamgroceries.MyApp
import com.company.dreamgroceries.R
import com.company.dreamgroceries.addorder.AddOrderActivity
import com.company.dreamgroceries.base.BaseFragment
import com.company.dreamgroceries.data.AddressResponse
import com.company.dreamgroceries.data.Addresse
import com.company.dreamgroceries.data.PlaceOrderResponse
import com.company.dreamgroceries.databinding.FragmentAddAddressBinding
import com.company.dreamgroceries.extensions.getViewModelFactory
import com.company.dreamgroceries.home.ui.profile.ui.addresslist.NoDeliveryAddresActivity
import com.company.dreamgroceries.home.ui.support.SupportActivity
import com.company.dreamgroceries.utilities.BUN_1
import com.company.dreamgroceries.utilities.COMMA_SEP
import com.company.dreamgroceries.utilities.REQUEST_PICK_LOCATION
import com.company.dreamgroceries.utilities.Utils
import com.company.dreamgroceries.webservices.Resource
import com.company.dreamgroceries.webservices.Status
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddAddressFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddAddressFragment : BaseFragment() {
    private var mLatLong: LatLng? = null
    private var mAddress: Addresse? = null
    private lateinit var mBinding: FragmentAddAddressBinding


    val args: AddAddressFragmentArgs by navArgs()

    val viewModel by lazy {
        requireActivity().getViewModelFactory<AddressViewModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentAddAddressBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mBinding.llDummy.requestFocus()
        hideViews()
        fillDetailsIfEditView()

        mBinding.viewPickLocation.setOnClickListener { openLocationPicker() }
        //mBinding.edtLocation.setOnFocusChangeListener(this)


        mBinding.btnNext.setOnClickListener {
            if (validateInputs()) {
                updateAddressObj(
                    mBinding.edtLocation.text.toString(),
                    mBinding.edtApt.text.toString(),
                    mBinding.edtZip.text.toString(),
                    mBinding.edtCity.text.toString(),
                    mBinding.edtState.text.toString(),
                    getLatitude(), getLongitude()
                )
                mAddress?.let {
                    viewModel.updateAddress(it).observe(
                        requireActivity(),
                        androidx.lifecycle.Observer { handleResponse(it) })
                }


            }


        }

        mBinding.ivLocation.setOnClickListener {
            getLocationPermission()
        }

        mBinding.ivBack.setOnClickListener {
            if (mBinding.llNoData.visibility == View.VISIBLE) {
                mBinding.llNoData.visibility = View.GONE
            }
            activity?.onBackPressed()
        }
    }


    private fun updateAddressObj(
        street: String,
        floor: String,
        zip: String,
        city: String,
        state: String,
        latitude: String,
        longitude: String
    ) {
        if (mAddress != null) {

            mAddress?.street = street
            mAddress?.floor = floor
            mAddress?.city = city
            mAddress?.state = state
            mAddress?.latitude = latitude
            mAddress?.longitude = longitude


        } else {
            mAddress = Addresse(
                street = street,
                floor = floor,
                zip_code = zip,
                latitude = latitude,
                longitude = longitude,
                city = city,
                state = state,
                is_selected = 1
            )
        }

    }

    private fun getLatitude(): String {
        mLatLong?.let {
            return it.latitude.toString()
        } ?: mAddress?.let { return it.latitude?.let { it }.toString() }
        return ""
    }

    private fun getLongitude(): String {
        mLatLong?.let {
            return it.longitude.toString()
        } ?: mAddress?.let { return it.longitude?.let { it }.toString() }
        return ""
    }


    private fun handleResponse(resource: Resource<AddressResponse?>) {
        when (resource.status) {
            Status.SUCCESS -> {

                // if (isAnyValidAddressForDelivery(resource.data)) {
                AsyncTask.execute {
                    resource.data?.data?.let {
                        MyApp.getInstance().addressDao().deleteAll()
                        MyApp.getInstance().addressDao().insertAddress(
                            it
                        )
                        requireActivity().runOnUiThread {
                            if (requireActivity() is AddOrderActivity)
                                showConfirmationDialog()

                            //   getCurrentAddressAndPlaceOrder()
                            if (activity is AddAddressActivity) {
                                // Utils.showToastShort(requireContext(), resource.data?.message)
                                requireActivity().onBackPressed()
                            }

                        }

                    }
                }


                hideProgress(mBinding.progressBar)
            }
            Status.ERROR -> {
                /*if (activity is AddAddressActivity) {
                    showNoDeliveryView()
                } else {
                    val action =
                        AddAddressFragmentDirections.actionAddAddressFragmentToNoDeliveryAddressFragment2(
                            getString(R.string.add_new_address)
                        )
                    mBinding.btnNext.findNavController().navigate(action)
                }*/
                Log.e("error code ", "errir cide receuved " + resource.errorCode)
                hideProgress(mBinding.progressBar)
                if (resource.errorCode == 422) {
                    val intent = Intent(requireActivity(), NoDeliveryAddresActivity::class.java)
                    intent.putExtra(BUN_1, getString(R.string.add_new_address))
                    startActivity(intent)
                    requireActivity().overridePendingTransition(0, 0)
                } else {
                    Utils.showToast(requireContext(), resource.message)
                }
            }
            Status.LOADING -> {
                showProgress(mBinding.progressBar)
            }
        }
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

        mBinding.btnNext.findNavController().previousBackStackEntry?.savedStateHandle?.set(
            "key",
            args.productList.toList()
        )
        mBinding.btnNext.findNavController().previousBackStackEntry?.savedStateHandle?.set(
            "key2",
            args.addInst
        )
        mBinding.btnNext.findNavController().popBackStack(R.id.manualOrderFragment, false)

    }


    private fun showNoDeliveryView() {
        mBinding.llNoData.visibility = View.VISIBLE
        mBinding.noData.tvTitle.setText(getString(R.string.no_delivery_available))
        mBinding.noData.tvDesc.setText(getString(R.string.you_can_contact_your_team))
        mBinding.noData.ivView.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_no_products
            )
        )
        mBinding.noData.btnNext.setText(getString(R.string.customer_support))
        mBinding.noData.btnNext.setOnClickListener { showCustomerSupport() }
    }

    private fun showCustomerSupport() {
        val intent = Intent(requireContext(), SupportActivity::class.java)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


    /*fun isAnyValidAddressForDelivery(data: AddressResponse?): Boolean {
        val list = data?.data
        list?.let {
            for (address in it) {
                if (address.allow_order == 1) {
                    return true
                }
            }
        }
        return false

    }*/

    private fun getCurrentAddressAndPlaceOrder() {
        if (activity is AddOrderActivity) {

            MyApp.getInstance().addressDao().geCurrentAddress()
                .observe(viewLifecycleOwner,
                    androidx.lifecycle.Observer {
                        it?.let {
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
                        }

                    })
        }
    }

    private fun handlePlaceOrder(resource: Resource<PlaceOrderResponse>?) {
        when (resource?.status) {
            Status.SUCCESS -> {
                hideProgress(mBinding.progressBar)

                resource.data?.let {
                    val action =
                        AddAddressFragmentDirections.actionAddAddressFragmentToThanksFragment(it.order_no)
                    mBinding.btnNext.findNavController().navigate(action)
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


    private fun validateInputs(): Boolean {

        if (mBinding.tiPickLocation.isTilEmpty()) {
            mBinding.ivLocation.visibility = View.GONE
            return false
        } else if (mBinding.tiZip.isTilEmpty()) {
            return false
        } else if (mLatLong == null) {
            Utils.showToast(requireContext(), getString(R.string.please_pick_valid_location))
            return false
        }
        return true

    }


    private fun openLocationPicker() {
        val fields: List<Place.Field> =
            Arrays.asList(
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS_COMPONENTS,
                Place.Field.NAME
            )

        val intent: Intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        )
            .build(requireActivity())
        startActivityForResult(intent, REQUEST_PICK_LOCATION)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_PICK_LOCATION) {
            if (resultCode === Activity.RESULT_OK) {
                data?.let {
                    val place = Autocomplete.getPlaceFromIntent(data)
                    fetchAddressComponentsAndShow(place)

                }

            } else if (resultCode === AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                data?.let {
                    val status = Autocomplete.getStatusFromIntent(data)
                    Log.i(ContentValues.TAG, status.getStatusMessage())
                }

            } else if (resultCode === Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    override fun extractDetailsFromPlace(place: Place) {
        fetchAddressComponentsAndShow(place)
    }

    private fun fetchAddressComponentsAndShow(place: Place) {
        mLatLong = place.latLng
        val list = ArrayList<String?>()
        list.add(place.name)


        var postalCode = ""
        var state = ""
        var city = ""
        place.addressComponents?.let {
            it.asList().forEach {
                if (it.types.contains("route")) {
                    list.add(it.name)
                }
                if (it.types.contains("sublocality_level_2")) {
                    list.add(it.name)
                }
                if (it.types.contains("sublocality_level_1")) {
                    list.add(it.name)
                }
                if (it.types.contains("postal_code")) {
                    postalCode = it.name
                }
                if (it.types.contains("administrative_area_level_1")) {
                    state = it.name
                }

                if (it.types.contains("administrative_area_level_2")) {
                    city = it.name
                }
            }
        }
        mBinding.edtLocation.setText(TextUtils.join(COMMA_SEP, list))
        mBinding.edtZip.setText(postalCode)
        mBinding.edtCity.setText(city)
        mBinding.edtState.setText(state)
        mBinding.ivLocation.visibility = View.VISIBLE

    }


    private fun fillDetailsIfEditView() {
        mAddress = arguments?.getParcelable<Addresse>(ARG_PARAM1)
        mAddress?.let {
            mBinding.tvTitle.setText(getString(R.string.edit_address))
            mBinding.edtLocation.setText(it.street)
            mBinding.edtApt.setText(it.floor)
            mBinding.edtZip.setText(it.zip_code)
            mBinding.edtState.setText(it.state)
            mBinding.edtCity.setText(it.city)
            mLatLong = it.latitude?.toDouble()?.let { it1 ->
                it.longitude?.toDouble()?.let { it2 ->
                    LatLng(
                        it1,
                        it2
                    )
                }
            }
        } ?: mBinding.tvTitle.setText(getString(R.string.add_new_address))
    }

    private fun hideViews() {
        //   mBinding.tiCity.visibility = View.GONE
        //  mBinding.tiState.visibility = View.GONE
        mBinding.ivArrow.visibility = View.GONE

    }

    fun isNoDataVisible(): Boolean {
        if (mBinding.llNoData.visibility == View.VISIBLE) {
            mBinding.llNoData.visibility = View.GONE
            return true
        }
        return false

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddAddressFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Addresse?) =
            AddAddressFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)

                }
            }
    }
}
