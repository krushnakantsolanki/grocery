package com.fameget.dreamgroceries.home.ui.orders

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.base.BaseFragment
import com.fameget.dreamgroceries.data.BaseResponse
import com.fameget.dreamgroceries.data.Order
import com.fameget.dreamgroceries.databinding.FragmentMyOrdersBinding
import com.fameget.dreamgroceries.extensions.getViewModelFactory
import com.fameget.dreamgroceries.home.HomeActivity
import com.fameget.dreamgroceries.utilities.BUN_1
import com.fameget.dreamgroceries.utilities.ORDER_CANCELLED
import com.fameget.dreamgroceries.utilities.REQUEST_ORDER_DETAIL
import com.fameget.dreamgroceries.utilities.Utils
import com.fameget.dreamgroceries.webservices.NetStatus
import com.fameget.dreamgroceries.webservices.Resource
import com.fameget.dreamgroceries.webservices.Status

class MyOrdersFragment : BaseFragment(), OrderClickListener {

    private val EMPTY_DATA = 2
    private val NO_INTERNET = 1
    private lateinit var mAdapter: MyOrdersAdapter
    private lateinit var mBinding: FragmentMyOrdersBinding

    val viewModel by lazy {
        requireActivity().getViewModelFactory<OrdersViewModel>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentMyOrdersBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mBinding.rvList.layoutManager = LinearLayoutManager(activity)
        mAdapter = MyOrdersAdapter(this)
        mBinding.rvList.adapter = mAdapter

        if (Utils.isNetworkConnected(requireContext()))
            makeAPICalls()
        else
            showNoDataView(NO_INTERNET)


        mBinding.swipeLayout.setOnRefreshListener {
            if (Utils.isNetworkConnected(requireContext()))
                viewModel.refresh()
            else
                showNoDataView(NO_INTERNET)

        }

    }

    private fun makeAPICalls() {
        viewModel.orderPagedList.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            mAdapter.setNetworkState(it)
        })

        viewModel.initialLoad.observe(viewLifecycleOwner, Observer {
            if (it?.status == NetStatus.EMPTY) {
                /*mBinding.tvNoData.visibility = View.VISIBLE
                mBinding.rvList.visibility = View.GONE*/
                showNoDataView(EMPTY_DATA)
            } else {
                if (viewModel.pageReq.page == 1) {
                    if (mBinding.swipeLayout.isRefreshing) {
                        mBinding.swipeLayout.isRefreshing = false
                    }
                    mBinding.tvNoData.visibility = View.GONE
                    mBinding.rvList.visibility = View.VISIBLE
                    mBinding.rvList.requestFocus()
                    if(mBinding.swipeLayout.isRefreshing)
                        mBinding.swipeLayout.isRefreshing = false
                }
            }
        })
    }

    private fun showNoDataView(type: Int) {
        when (type) {
            NO_INTERNET -> {
                setNoDataViewsText(
                    R.drawable.ic_no_internet,
                    getString(R.string.no_internet_connection),
                    getString(R.string.semms_you_offline)
                )
                mBinding.noData.btnNext.text = getString(R.string.reload)
                mBinding.llNoData.visibility = View.VISIBLE

                mBinding.noData.btnNext.setOnClickListener {
                    if (Utils.isNetworkConnected(requireContext())) {
                        mBinding.llNoData.visibility = View.GONE
                        if (!mBinding.swipeLayout.isRefreshing)
                            makeAPICalls()
                        else
                            viewModel.refresh()
                    }
                }


            }
            EMPTY_DATA -> {
                setNoDataViewsText(
                    R.drawable.ic_no_data,
                    getString(R.string.no_orders_found),
                    getString(R.string.start_ordering_no_data)
                )
                mBinding.noData.btnNext.text = getString(R.string.order_now)
                mBinding.llNoData.visibility = View.VISIBLE

                mBinding.noData.btnNext.setOnClickListener {
                    (requireActivity() as HomeActivity).openHomeFragment(false)
                }
            }

        }

    }

    private fun setNoDataViewsText(
        icon: Int,
        title: String,
        desc: String
    ) {
        mBinding.noData.ivView.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                icon
            )
        )
        mBinding.noData.tvTitle.text = title
        mBinding.noData.tvDesc.text = desc
    }


    /* private fun getMyOrders() {
         viewModel.getOrders().observe(viewLifecycleOwner, Observer {
             handleResponse(it)

         })
     }

     private fun handleResponse(it: Resource<OrdersResponse>?) {
         it?.let { resource ->
             when (resource.status) {
                 Status.SUCCESS -> {
                     hideProgress(mBinding.progressBar)
                     mAdapter.submitList(it.data?.data)

                     it.data?.data?.run {
                         if (isEmpty()) {
                             mBinding.tvNoData.visibility = View.VISIBLE
                         }
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
     }*/

    override fun onCancelOrder(order: Order, position: Int) {
        Utils.showDialog(
            requireContext(),
            getString(R.string.cancel_order),
            getString(R.string.are_sure_cancel_order),
            getString(R.string.yes),
            getString(R.string.no),
            DialogInterface.OnClickListener { dialogInterface, i ->
                viewModel.cancelOrder(order.order_no)
                    .observe(
                        viewLifecycleOwner,
                        Observer { handleResponseCancelOrder(it, position) })
            },
            null
        )
    }

    override fun onOrderClicked(order: Order) {
        val intent = Intent(requireActivity(), ViewOrderActivity::class.java)
        intent.putExtra(BUN_1, order)
        startActivityForResult(intent, REQUEST_ORDER_DETAIL)
        requireActivity().overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }

    override fun retry() {
        viewModel.retry()
    }

    private fun handleResponseCancelOrder(it: Resource<BaseResponse>?, orderId: Int) {
        it?.let { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    hideProgress(mBinding.progressBar)
                    Utils.showToastShort(requireContext(), it.data?.message)
                    //viewModel.refresh()
                    updateOrder(orderId)


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
    }

    private fun updateOrder(orderId: Int) {
        mAdapter.currentList?.get(orderId)?.status = ORDER_CANCELLED
        mAdapter.notifyItemChanged(orderId)
        /*mAdapter.currentList?.let {
            for (order in it) {
                if (order.id == orderId) {
                    order.status = ORDER_CANCELLED
                    mAdapter.notifyDataSetChanged()
                    break;
                }
            }

        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ORDER_DETAIL && resultCode == RESULT_OK) {
            val orderId = data?.getIntExtra(BUN_1, 0)
            mAdapter.currentList?.let {
                for (order in it) {
                    if (order.id == orderId) {
                        order.status = ORDER_CANCELLED
                        mAdapter.notifyDataSetChanged()
                        break;
                    }
                }

            }
        }
    }
}
