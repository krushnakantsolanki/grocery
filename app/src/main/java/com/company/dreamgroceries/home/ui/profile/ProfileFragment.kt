package com.company.dreamgroceries.home.ui.profile

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.company.dreamgroceries.MyApp
import com.company.dreamgroceries.R
import com.company.dreamgroceries.base.BaseFragment
import com.company.dreamgroceries.base.ViewModelFactory
import com.company.dreamgroceries.data.BaseResponse
import com.company.dreamgroceries.data.Profile
import com.company.dreamgroceries.data.UpdateNotificationReq
import com.company.dreamgroceries.databinding.FragmentProfileBinding
import com.company.dreamgroceries.utilities.BUN_1
import com.company.dreamgroceries.utilities.Utils
import com.company.dreamgroceries.webservices.Resource
import com.company.dreamgroceries.webservices.Status

class ProfileFragment : BaseFragment() {

    private var mProfile: Profile? = null
    private lateinit var viewModel: ProfileViewModel
    private lateinit var mBinding: FragmentProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        return mBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        viewModel.getProfile().observe(viewLifecycleOwner, Observer {
            it?.let {
                mProfile = it
                it.first_name?.let { mBinding.tvFirstName.text = it }
                it.last_name?.let { mBinding.tvLastName.text = it }
                it.email?.let { mBinding.tvEmail.text = it }
                it.phone_number?.let { mBinding.tvPhoneNumber.text = it }
                it.notification_status?.let { setNotificationSwitchIcon(it) }
            }


        })

        mBinding.conAddress.setOnClickListener {
            val intent = Intent(activity, AddressListActivity::class.java)
            intent.putExtra(BUN_1, 1)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        mBinding.conNotification.setOnClickListener {
            mProfile?.let {
                val newStatus = 1 - it.notification_status
                viewModel.updateNotificationSetting(UpdateNotificationReq(newStatus))
                    .observe(viewLifecycleOwner,
                        Observer { handleResponse(it, newStatus) })
            }

        }

    }

    private fun setNotificationSwitchIcon(status: Int) {
        if (status == 0) {
            mBinding.switchNotification.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_switch_off
                )
            )
        } else {
            mBinding.switchNotification.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_switch_on
                )
            )
        }
    }

    private fun handleResponse(
        resource: Resource<BaseResponse>?,
        status: Int
    ) {
        when (resource?.status) {
            Status.SUCCESS -> {
                hideProgress(mBinding.progressBar)
                setNotificationSwitchIcon(status)
                mProfile?.let {
                    it.notification_status = status
                    AsyncTask.execute { MyApp.getInstance().toInfoDao().insertUser(it) }
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

    private fun setupViewModel() {
        viewModel = requireActivity().run {
            ViewModelProvider(
                this,
                ViewModelFactory()
            ).get(ProfileViewModel::class.java)
        }


    }

}
