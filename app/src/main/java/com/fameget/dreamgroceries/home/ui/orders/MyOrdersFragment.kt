package com.fameget.dreamgroceries.home.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.fameget.dreamgroceries.databinding.FragmentMyOrdersBinding

class MyOrdersFragment : Fragment() {

    private lateinit var mBinding: FragmentMyOrdersBinding
    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        mBinding = FragmentMyOrdersBinding.inflate(layoutInflater, container, false)

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mBinding.rvList.layoutManager = LinearLayoutManager(activity)
        val mAdapter = MyOrdersAdapter()
        mBinding.rvList.adapter = mAdapter

    }
}
