package com.company.dreamgroceries.login.fragments


import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.eazypermissions.common.model.PermissionResult
import com.eazypermissions.dsl.extension.requestPermissions
import com.company.dreamgroceries.R
import com.company.dreamgroceries.base.BaseFragment
import com.company.dreamgroceries.data.Profile
import com.company.dreamgroceries.data.SocialLoginRequest
import com.company.dreamgroceries.data.SocialLoginResponse
import com.company.dreamgroceries.databinding.LoginFragmentBinding
import com.company.dreamgroceries.extensions.getViewModelFactory
import com.company.dreamgroceries.login.LoginViewModel
import com.company.dreamgroceries.utilities.GOOGLE_SIGN_IN
import com.company.dreamgroceries.utilities.REQUEST_GOOGLE_SIGN_IN
import com.company.dreamgroceries.utilities.Utils
import com.company.dreamgroceries.webservices.Resource
import com.company.dreamgroceries.webservices.Status
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


class LoginFragment : BaseFragment() {

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(parentJob + Dispatchers.Default)

    companion object {
        fun newInstance() =
            LoginFragment()
    }

    private val viewModel by lazy {
        requireActivity().getViewModelFactory<LoginViewModel>()
    }


    //  private lateinit var viewModel: LoginViewModel
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var binding: LoginFragmentBinding
    // private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //    viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // setupViewModel(LoginViewModel::class.java)


        initGoogleSignIn()


        binding.llLogin?.setOnClickListener {
            // getLocationPermission(1)
            openEnterMobileNumScreen()
        }

       /* binding.viewGg.setOnClickListener {
            //  getLocationPermission(2)
            showProgress(binding.progressBar)
            startGoogleSignIn()

        }*/

    }


    private fun startGoogleSignIn() {
        startActivityForResult(mGoogleSignInClient.signInIntent, REQUEST_GOOGLE_SIGN_IN)
    }

    private fun initGoogleSignIn() {
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail()
            .requestProfile().build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        /*val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()*/
    }

    private fun openEnterMobileNumScreen() {
        val action =
            LoginFragmentDirections
                .actionMainFragmentToPhoneNumberFragment()
        binding.llLogin?.findNavController()?.navigate(action)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === REQUEST_GOOGLE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            //updateUI(account)
            getFirebaseToken(account)

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            //updateUI(null)
            hideProgress(binding.progressBar)
            Utils.showToast(
                requireContext(),
                getString(R.string.google_sign_in_failed, e.statusCode)
            )
        }
    }

    private fun getFirebaseToken(account: GoogleSignInAccount?) {
        val addOnCompleteListener = FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                val token: String? = if (!task.isSuccessful) {
                    Log.w("TAG", "getInstanceId failed", task.exception)
                    ""
                } else {
                    task.result?.token

                }

                loginUser(account, token)


            })
    }

    private fun loginUser(
        account: GoogleSignInAccount?,
        token: String?
    ) {
        val email = account?.email

        viewModel.socialMediaSignIn(
            SocialLoginRequest(
                account?.id,
                account?.givenName,
                account?.familyName,
                email,
                GOOGLE_SIGN_IN,
                token
            )
        ).observe(requireActivity(), Observer {
            handleResponse(it)

        })


    }

    private fun handleResponse(it: Resource<SocialLoginResponse?>) {
        it?.let { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    hideProgress(binding.progressBar)
                    val profile = resource.data?.data as Profile
                    Utils.showToast(
                        requireContext(),
                        getString(R.string.welcome, profile.first_name)
                    )
                    val action = LoginFragmentDirections.actionMainFragmentToHomeActivity()
                  //  binding.viewGg.findNavController().navigate(action)
                    requireActivity().finish()

                }
                Status.ERROR -> {

                    hideProgress(binding.progressBar)
                    Utils.showToast(requireContext(), resource.message)
                }
                Status.LOADING -> {
                    showProgress(binding.progressBar)
                    /*progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE*/
                }
            }
        }
    }


    private fun getLocationPermission(btnType: Int) {
        requestPermissions(Manifest.permission.ACCESS_FINE_LOCATION) {
            requestCode = 1
            resultCallback = {
                handlePermissionsResult(this, btnType)
            }
        }
    }


    private fun handlePermissionsResult(
        permissionResult: PermissionResult,
        btnType: Int
    ) {
        when (permissionResult) {
            is PermissionResult.PermissionGranted -> {
                if (btnType == 1) {
                    openEnterMobileNumScreen()
                } else if (btnType == 2) {
                    showProgress(binding.progressBar)
                    startGoogleSignIn()
                }
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
                                handlePermissionsResult(this, btnType)
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


}
