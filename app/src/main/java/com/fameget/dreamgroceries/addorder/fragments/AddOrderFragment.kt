package com.fameget.dreamgroceries.addorder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.fameget.dreamgroceries.addorder.fragments.AddOrderFragmentDirections
import com.fameget.dreamgroceries.databinding.AddOrderFragmentBinding

class AddOrderFragment : Fragment() {

    companion object {
        fun newInstance() =
            AddOrderFragment()
    }

    private lateinit var mBinding: AddOrderFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = com.fameget.dreamgroceries.databinding.AddOrderFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
      //  viewModel = ViewModelProviders.of(this).get(AddOrderViewModel::class.java)

        mBinding.ivBack.setOnClickListener { activity?.onBackPressed() }

        mBinding.viewQuickOrder.setOnClickListener {
            val action =
                AddOrderFragmentDirections.actionAddOrderFragmentToManualOrderFragment()
            mBinding.viewQuickOrder.findNavController().navigate(action)
        }
        mBinding.viewBrowseStore.setOnClickListener {
            val action =
                AddOrderFragmentDirections.actionAddOrderFragmentToProductsFragment()
            mBinding.viewQuickOrder.findNavController().navigate(action)
        }
    }

}
