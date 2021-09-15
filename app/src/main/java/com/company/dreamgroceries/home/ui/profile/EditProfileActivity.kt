package com.company.dreamgroceries.home.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.lifecycle.Observer
import com.company.dreamgroceries.MyApp
import com.company.dreamgroceries.R
import com.company.dreamgroceries.base.BaseActivity
import com.company.dreamgroceries.data.*
import com.company.dreamgroceries.databinding.ActivityEditProfileBinding
import com.company.dreamgroceries.extensions.getViewModelFactory
import com.company.dreamgroceries.extensions.isTilEmpty
import com.company.dreamgroceries.extensions.isTilValidEmail
import com.company.dreamgroceries.utilities.BUN_1
import com.company.dreamgroceries.utilities.REQUEST_CHANGE_PHONE
import com.company.dreamgroceries.utilities.Utils
import com.company.dreamgroceries.webservices.Resource
import com.company.dreamgroceries.webservices.Status

class EditProfileActivity : BaseActivity() {

    private lateinit var mProfile: Profile
    private lateinit var mPhoneNumber: String
    private lateinit var mBinding: ActivityEditProfileBinding

    val viewModel by lazy {
        getViewModelFactory<ProfileViewModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btnNext.setOnClickListener {
            if (validateInputs()) {
                if (mBinding.edtPhone.text.toString().trim() == mPhoneNumber) {

                    viewModel.updateProfile(
                        UpdateProfileReq(
                            country_code = mProfile.country_code.toString(),
                            email = mBinding.edtEmail.text.toString().trim(),
                            first_name = mBinding.edtFirstName.text.toString().trim(),
                            last_name = mBinding.edtLastName.text.toString().trim(),
                            phone_number = mBinding.edtPhone.text.toString().trim()
                        )
                    ).observe(this, Observer {
                        handleResponse(it)
                    })
                } else {
                    viewModel.getOtp(
                        OtpRequest(
                            country_code = mProfile.country_code.toString(),
                            phone_number = mBinding.edtPhone.text.toString().trim()
                        )
                    ).observe(this, Observer { handleOtpResponse(it) })
                }
            }

        }

        mBinding.ivBack.setOnClickListener { onBackPressed() }

        viewModel.getProfile().observe(this, Observer {
            it?.let {
                mProfile = it
                it.first_name?.let { mBinding.edtFirstName.setText(it) }
                it.last_name?.let { mBinding.edtLastName.setText(it) }
                it.email?.let { mBinding.edtEmail.setText(it) }
                it.phone_number?.let { mBinding.edtPhone.setText(it) }
                mPhoneNumber = it.phone_number
            }


        })
    }

    private fun handleOtpResponse(resource: Resource<OtpResponse>?) {
        when (resource?.status) {
            Status.SUCCESS -> {
                hideProgress(mBinding.progressBar)

                onBackPressed()

                Utils.showToastShort(this, resource.data?.message)
                Utils.showToastShort(
                    this, resource.data?.otp
                )

                val intent = Intent(this, EnterOtpActivity::class.java)
                val updateProfile = UpdateProfileReq(
                    country_code = mProfile.country_code.toString(),
                    email = mBinding.edtEmail.text.toString().trim(),
                    first_name = mBinding.edtFirstName.text.toString().trim(),
                    last_name = mBinding.edtLastName.text.toString().trim(),
                    phone_number = mBinding.edtPhone.text.toString().trim()
                )
                intent.putExtra(BUN_1, updateProfile)
                startActivityForResult(intent, REQUEST_CHANGE_PHONE)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            Status.ERROR -> {

                hideProgress(mBinding.progressBar)
                Utils.showToast(this, resource.message)
            }
            Status.LOADING -> {
                showProgress(mBinding.progressBar)
            }
        }
    }

    private fun handleResponse(resource: Resource<SocialLoginResponse>?) {
        when (resource?.status) {
            Status.SUCCESS -> {
                hideProgress(mBinding.progressBar)
                AsyncTask.execute {
                    resource.data?.data?.let {
                        MyApp.getInstance().toInfoDao().insertUser(
                            it
                        )
                    }
                }
                onBackPressed()
                /*val action =
                    AddressListFragmentDirections.actionAddressListFragmentToThanksFragment()
                mBinding.viewNext.findNavController().navigate(action)*/
                Utils.showToastShort(this, resource.data?.message)
            }
            Status.ERROR -> {

                hideProgress(mBinding.progressBar)
                Utils.showToast(this, resource.message)
            }
            Status.LOADING -> {
                showProgress(mBinding.progressBar)
            }
        }
    }

    private fun validateInputs(): Boolean {
        if (mBinding.tiFirstName.isTilEmpty() || mBinding.tiLastName.isTilEmpty() || mBinding.tiEmail.isTilEmpty() || !mBinding.tiEmail.isTilValidEmail() || mBinding.tiPhone.isTilEmpty()) {
            return false
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHANGE_PHONE && resultCode == Activity.RESULT_OK) {
            super.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}
