package com.company.dreamgroceries.login.fragments

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.company.dreamgroceries.R
import com.company.dreamgroceries.base.BaseFragment
import com.company.dreamgroceries.data.SocialLoginResponse
import com.company.dreamgroceries.databinding.FragmentEnterDetailsBinding
import com.company.dreamgroceries.extensions.getViewModelFactory
import com.company.dreamgroceries.extensions.isTilEmpty
import com.company.dreamgroceries.extensions.isTilValidEmail
import com.company.dreamgroceries.home.ui.profile.ui.addresslist.NoDeliveryAddresActivity
import com.company.dreamgroceries.login.LoginViewModel
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
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EnterDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EnterDetailsFragment : BaseFragment(), View.OnFocusChangeListener {
    private var mLatLong: LatLng? = null
    private lateinit var mBinding: FragmentEnterDetailsBinding

    private val loginVM by lazy {
        requireActivity().getViewModelFactory<LoginViewModel>()
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = com.company.dreamgroceries.databinding.FragmentEnterDetailsBinding.inflate(
            inflater,
            container,
            false
        )
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mBinding.ivBack.setOnClickListener {
            if (mBinding.llNoData.visibility == View.VISIBLE) {
                mBinding.llNoData.visibility = View.GONE
                return@setOnClickListener
            }
            activity?.onBackPressed()
        }
        hideViews();

        mBinding.llEnterDetails.viewPickLocation.setOnClickListener { openLocationPicker() }
        mBinding.llEnterDetails.edtLocation.setOnFocusChangeListener(this)

        mBinding.llEnterDetails.ivLocation.setOnClickListener { getLocationPermission() }



        mBinding.llEnterDetails.ivLocation.setOnClickListener {
            getLocationPermission()
        }


        mBinding.btnNext.setOnClickListener {
            if (validateInputs()) {

                loginVM.registerUser(
                    loginVM.getRegisterUserRequest(
                        mBinding.llEnterDetails.edtFirstName.text.toString(),
                        mBinding.llEnterDetails.edtLastName.text.toString(),
                        mBinding.llEnterDetails.edtEmail.text.toString(),
                        mBinding.llEnterDetails.edtLocation.text.toString(),
                        mBinding.llEnterDetails.edtApt.text.toString(),
                        mBinding.llEnterDetails.edtZip.text.toString(),
                        mBinding.llEnterDetails.edtCity.text.toString(),
                        mBinding.llEnterDetails.edtState.text.toString(),
                        mLatLong?.latitude.toString(),
                        mLatLong?.longitude.toString()

                    )


                ).observe(requireActivity(), Observer { handleResponse(it) })

            }


        }
    }

    @Override
    override fun extractDetailsFromPlace(place: Place) {
        fetchAddressComponentsAndShow(place)
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
            if (resultCode === RESULT_OK) {
                data?.let {
                    val place = Autocomplete.getPlaceFromIntent(data)
                    fetchAddressComponentsAndShow(place)

                }

            } else if (resultCode === AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                data?.let {
                    val status = Autocomplete.getStatusFromIntent(data)
                    Log.i(TAG, status.getStatusMessage())
                }

            } else if (resultCode === RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private fun fetchAddressComponentsAndShow(place: Place) {
        mLatLong = place.latLng
        val list = ArrayList<String?>()
        list.add(place.name)

        var state = ""
        var city = ""
        var postalCode = ""
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
        mBinding.llEnterDetails.edtLocation.setText(TextUtils.join(COMMA_SEP, list))
        mBinding.llEnterDetails.edtZip.setText(postalCode)
        mBinding.llEnterDetails.edtCity.setText(city)
        mBinding.llEnterDetails.edtState.setText(state)
        mBinding.llEnterDetails.ivLocation.visibility = View.VISIBLE

    }

    private fun hideViews() {
        // mBinding.llEnterDetails.tiCity.visibility = View.GONE
        // mBinding.llEnterDetails.tiState.visibility = View.GONE
        mBinding.llEnterDetails.ivArrow?.visibility = View.GONE

    }

    private fun handleResponse(resource: Resource<SocialLoginResponse?>) {
        when (resource.status) {
            Status.SUCCESS -> {
                hideProgress(mBinding.progressBar)

                //  if (Utils.isAnyValidAddressForDelivery(resource.data)) {
                Utils.showToast(
                    requireContext(),
                    getString(R.string.welcome, resource.data?.data?.first_name)
                )
                val action =
                    EnterDetailsFragmentDirections.actionEnterDetailsFragmentToHomeActivity()
                mBinding.btnNext.findNavController().navigate(action)
                requireActivity().finish()
                // }


            }
            Status.ERROR -> {

                /*val action =
                    EnterDetailsFragmentDirections.actionEnterDetailsFragmentToNoDeliveryAddressFragment(
                        getString(R.string.enter_your_details)
                    )
                mBinding.btnNext.findNavController().navigate(action)*/
                hideProgress(mBinding.progressBar)
                if(resource.errorCode == 422) {
                    val intent = Intent(requireActivity(), NoDeliveryAddresActivity::class.java)
                    intent.putExtra(BUN_1, getString(R.string.enter_your_details))
                    startActivity(intent)
                    requireActivity().overridePendingTransition(0, 0)
                }else{
                    Utils.showToast(requireContext(), resource.message)
                }


            }
            Status.LOADING -> {
                showProgress(mBinding.progressBar)
            }
        }
    }


    /*  private fun validateInputs(): Boolean {
          if (!tiFirstName.isTilEmpty() && !tiLastName.isTilEmpty() && tiEmail.isTilValidEmail()) {
              if (!mBinding.llEnterDetails.edtLocation.text.toString()
                      .isEmpty() && mBinding.llEnterDetails.edtZip.text.toString().isEmpty()
              ) {
                  tiZip.isTilEmpty()
                  return false
              } else if (!mBinding.llEnterDetails.edtLocation.text.toString().isEmpty()
                  && !mBinding.llEnterDetails.edtZip.text.toString().isEmpty()
                  && mLatLong == null
              ) {
                  Utils.showToastShort(
                      requireContext(),
                      getString(R.string.please_pick_valid_location)
                  )
                  return false
              } else if (!mBinding.llEnterDetails.edtZip.text.toString().isEmpty()
                  && mBinding.llEnterDetails.edtLocation.text.toString().isEmpty()

              ) {
                  tiPickLocation.isTilEmpty()
                  mBinding.llEnterDetails.ivLocation.visibility = View.GONE
                  return false
              } else if (mBinding.llEnterDetails.edtLocation.text.toString().isEmpty()
                  && mBinding.llEnterDetails.edtZip.text.toString().isEmpty()
                  && !mBinding.llEnterDetails.edtApt.text.toString().isEmpty()

              ) {
                  tiPickLocation.isTilEmpty()
                  tiZip.isTilEmpty()
                  mBinding.llEnterDetails.ivLocation.visibility = View.GONE
                  return false
              }
              return true
          }
          return false
      }*/


    private fun validateInputs(): Boolean {
        if (!tiFirstName.isTilEmpty() && !tiLastName.isTilEmpty() && tiEmail.isTilValidEmail() && !mBinding.llEnterDetails.tiPickLocation.isTilEmpty() && !mBinding.llEnterDetails.tiPickLocation.isTilEmpty()
            && !mBinding.llEnterDetails.tiZip.isTilEmpty()
        ) {
            if (mLatLong == null) {
                Utils.showToastShort(
                    requireContext(),
                    getString(R.string.please_pick_valid_location)
                )
                return false
            }
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
         * @return A new instance of fragment EnterDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EnterDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        if (p1) {
            mBinding.llEnterDetails.ivLocation.visibility = View.VISIBLE
            openLocationPicker()
        }
    }

    fun hideNoDataViewIfVisible(): Boolean {
        if (mBinding.llNoData.visibility == View.VISIBLE) {
            mBinding.llNoData.visibility = View.GONE
            return true
        }
        return false
    }
}
