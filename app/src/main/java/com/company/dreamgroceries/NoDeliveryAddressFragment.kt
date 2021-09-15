package com.company.dreamgroceries

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.company.dreamgroceries.databinding.FragmentNoDeliveryAddressBinding
import com.company.dreamgroceries.extensions.enterAnimation
import com.company.dreamgroceries.home.ui.support.SupportActivity
import com.company.dreamgroceries.utilities.BUN_1

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NoDeliveryAddressFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NoDeliveryAddressFragment : Fragment() {
    private lateinit var mBinding: FragmentNoDeliveryAddressBinding

    val args: NoDeliveryAddressFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentNoDeliveryAddressBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showNoDeliveryView()
        mBinding.tvTitle.setText(arguments?.getString(BUN_1))

        mBinding.ivBack.setOnClickListener { requireActivity().onBackPressed() }
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
        val intent = Intent(requireActivity(), SupportActivity::class.java)
        requireActivity().startActivity(intent)
        requireActivity().enterAnimation()
    }


}
