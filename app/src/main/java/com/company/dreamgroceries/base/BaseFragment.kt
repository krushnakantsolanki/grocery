package com.company.dreamgroceries.base

import android.Manifest
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.eazypermissions.common.model.PermissionResult
import com.eazypermissions.dsl.extension.requestPermissions
import com.company.dreamgroceries.MyApp
import com.company.dreamgroceries.R
import com.company.dreamgroceries.di.AppComponent
import com.company.dreamgroceries.utilities.Utils
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import java.util.*


open class BaseFragment : Fragment() {


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity

    }


    protected fun getAppComponent(): AppComponent {
        return MyApp.getAppComponent()
    }

    protected fun showProgress(progressBar2: ProgressBar?) {
        progressBar2?.visibility = View.VISIBLE
        // if (!requireActivity().isFinishing) {
        //CustomProgressDialog.showProgressDialog(requireActivity() as BaseActivity)
        //}

    }

    protected fun hideProgress(progressBar2: ProgressBar?) {
        progressBar2?.visibility = View.GONE
        //CustomProgressDialog.dismissProgressDialog()
    }

    public fun getLocationPermission() {
        requestPermissions(Manifest.permission.ACCESS_FINE_LOCATION) {
            requestCode = 1
            resultCallback = {
                handlePermissionsResult(this)
            }
        }
    }


    private fun handlePermissionsResult(
        permissionResult: PermissionResult

    ) {
        when (permissionResult) {
            is PermissionResult.PermissionGranted -> {
                onLocationPermissionGranted();

            }
            is PermissionResult.PermissionDenied -> {
            }
            is PermissionResult.ShowRational -> {
                AlertDialog.Builder(requireContext())
                    .setMessage(getString(R.string.location_per_required))
                    .setTitle(getString(R.string.alert))
                    .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->
                        val permissions = when (permissionResult.requestCode) {
                            1 -> {
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                            }

                            else -> {
                                arrayOf()
                            }
                        }
                        requestPermissions(*permissions) {
                            requestCode = permissionResult.requestCode
                            resultCallback = {
                                handlePermissionsResult(this)
                            }
                        }
                    }
                    .create()
                    .show()
            }
            is PermissionResult.PermissionDeniedPermanently -> {
                requireActivity().finish()
            }
        }
    }

    public open fun onLocationPermissionGranted() {
        /*val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
            }*/
        val placesClient = Places.createClient(requireContext())
        val placeFields: List<Place.Field> =
            Collections.singletonList(Place.Field.ID)

        val request = FindCurrentPlaceRequest.newInstance(placeFields)

        val placeResponse: Task<FindCurrentPlaceResponse> = placesClient.findCurrentPlace(request)
        placeResponse.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val response: FindCurrentPlaceResponse? = task.result
                response?.run {
                    for (placeLikelihood in placeLikelihoods) {
                        Log.i(
                            TAG, String.format(
                                "Place '%s' has likelihood: %f",
                                placeLikelihood.place.id,
                                placeLikelihood.likelihood
                            )
                        )
                    }
                    val placeId = placeLikelihoods[0].place.id
                    placeId?.run {
                        getPlaceById(this, placesClient)
                    }


                }

            } else {
                val exception: Exception? = task.getException()
                if (exception is ApiException) {
                    Log.e(TAG, "Place not found: " + exception.statusCode)
                }
            }
        }

    }

    private fun getPlaceById(
        placeId: String,
        placesClient: PlacesClient
    ) {
        // Define a Place ID.
        // Define a Place ID.


// Specify the fields to return.

// Specify the fields to return.
        val placeFields =
            Arrays.asList(
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS_COMPONENTS,
                Place.Field.NAME
            )

// Construct a request object, passing the place ID and fields array.

// Construct a request object, passing the place ID and fields array.
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            val place: Place = response.getPlace()
            extractDetailsFromPlace(place)
        }.addOnFailureListener { exception ->
            if (exception is ApiException) {
                val statusCode = exception.statusCode
                // Handle error with given status code.
                Log.e(TAG, "Place not found: " + exception.localizedMessage)
                Utils.showToast(requireContext(), getString(R.string.unable_to_pick_your_address))
            }
        }
    }

    public open fun extractDetailsFromPlace(place: Place) {

    }


}