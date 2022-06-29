package com.company.dreamgroceries.home.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.company.dreamgroceries.R
import com.company.dreamgroceries.addorder.AddOrderActivity
import com.company.dreamgroceries.databinding.FragmentHomeBinding
import com.company.dreamgroceries.utilities.BUN_1
import com.company.dreamgroceries.utilities.ORD_DILVRY
import com.company.dreamgroceries.utilities.ORD_PICK
import com.company.dreamgroceries.utilities.REQUEST_ADD_ORDER

class HomeFragment : Fragment() {

    private lateinit var mBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentHomeBinding.inflate(inflater, container, false)

        /*homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mBinding.viewPick.setOnClickListener {
            startAddOrder(ORD_PICK)
        }

        mBinding.viewDel.setOnClickListener {
            startAddOrder(ORD_DILVRY)

        }
    }

    private fun startAddOrder(odrType: Int) {
        val intent = Intent(activity, AddOrderActivity::class.java)
        intent.putExtra(BUN_1, odrType)
        activity?.startActivityForResult(intent, REQUEST_ADD_ORDER)
        activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
