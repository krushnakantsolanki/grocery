package com.fameget.dreamgroceries.login.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.fameget.dreamgroceries.databinding.LoginFragmentBinding
import com.fameget.dreamgroceries.ui.main.MainViewModel

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() =
            LoginFragment()
    }

    private lateinit var binding: LoginFragmentBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        binding.llLogin?.setOnClickListener { openEnterMobileNumScreen() }

    }

    private fun openEnterMobileNumScreen() {
        val action =
            LoginFragmentDirections
                .actionMainFragmentToPhoneNumberFragment()
        binding.llLogin?.findNavController()?.navigate(action)
    }

}
