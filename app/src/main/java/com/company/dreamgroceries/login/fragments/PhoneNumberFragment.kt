package com.company.dreamgroceries.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.company.dreamgroceries.base.BaseFragment
import com.company.dreamgroceries.data.OtpRequest
import com.company.dreamgroceries.data.OtpResponse
import com.company.dreamgroceries.databinding.FragmentPhoneNumberBinding
import com.company.dreamgroceries.extensions.getViewModelFactory
import com.company.dreamgroceries.extensions.isTilEmpty
import com.company.dreamgroceries.login.LoginViewModel
import com.company.dreamgroceries.utilities.Utils
import com.company.dreamgroceries.webservices.Status
import kotlinx.android.synthetic.main.fragment_phone_number.*
import com.company.dreamgroceries.webservices.Resource as Resource1

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PhoneNumberFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhoneNumberFragment : BaseFragment() {
    // private lateinit var viewModel: LoginViewModel
    private lateinit var mBinding: FragmentPhoneNumberBinding

    private val viewModel by lazy {
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
        mBinding = FragmentPhoneNumberBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //setupViewModel(LoginViewModel::class.java)

        mBinding.btnNext.setOnClickListener {
            if (validateInputs()) {
                getOtp()
            }
        }
        mBinding.ivBack.setOnClickListener { activity?.onBackPressed() }
    }

    private fun getOtp() {
        /*if(!Utils.isNetworkConnected(requireContext())){
            Utils.showToast(requireContext(),getString(R.string.no_internet))
            return
        }*/
        viewModel.getOtp(
            OtpRequest(
                mBinding.ccp?.selectedCountryCode,
                mBinding.tiMob.editText?.text.toString()
            )
        )
            .observe(requireActivity(), Observer {
                handleResponse(it)

            })
    }

    private fun handleResponse(it: Resource1<OtpResponse?>) {
        it?.let { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    hideProgress(mBinding.progressBar2)
                    val otpResponse = resource.data as OtpResponse
                    Utils.showToastShort(requireContext(), otpResponse.message)
                    Utils.showToast(requireContext(), otpResponse.otp)
                    val action =
                        PhoneNumberFragmentDirections
                            .actionPhoneNumberFragmentToEnterOtpFragment()
                    mBinding.btnNext.findNavController().navigate(action)

                }
                Status.ERROR -> {
                    /*recyclerView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE*/
                    hideProgress(progressBar2)
                    Utils.showToast(requireContext(), resource.message)
                }
                Status.LOADING -> {
                    showProgress(mBinding.progressBar2)
                    /*progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE*/
                }
            }
        }
    }


    private fun validateInputs(): Boolean {
        if (tiMob.isTilEmpty(true))
            return false
        return true
    }

    /*protected fun setupViewModel() {
        viewModel = requireActivity().run {
            ViewModelProvider(
                this,
                ViewModelFactory()
            ).get(LoginViewModel::class.java)
        }


    }*/


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PhoneNumberFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PhoneNumberFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
