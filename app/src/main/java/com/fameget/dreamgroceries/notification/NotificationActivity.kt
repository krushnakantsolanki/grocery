package com.fameget.dreamgroceries.notification

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.base.BaseActivity
import com.fameget.dreamgroceries.data.BaseResponse
import com.fameget.dreamgroceries.data.NotificationData
import com.fameget.dreamgroceries.databinding.ActivityNotificationBinding
import com.fameget.dreamgroceries.extensions.getViewModelFactory
import com.fameget.dreamgroceries.home.ui.orders.ViewOrderActivity
import com.fameget.dreamgroceries.utilities.BUN_2
import com.fameget.dreamgroceries.utilities.Utils
import com.fameget.dreamgroceries.webservices.NetStatus
import com.fameget.dreamgroceries.webservices.Resource
import com.fameget.dreamgroceries.webservices.Status

class NotificationActivity : BaseActivity(), NotificationClickListener {

    private val EMPTY_DATA = 2
    private val NO_INTERNET = 1
    private lateinit var mAdapter: NotificationAdapter
    val viewModel by lazy {
        getViewModelFactory<NotificationViewModel>()
    }

    private lateinit var mBinding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.rvList.layoutManager = LinearLayoutManager(this)
        mAdapter = NotificationAdapter(this)
        mBinding.rvList.adapter = mAdapter

        mBinding.ivBack.setOnClickListener { onBackPressed() }


        mBinding.swipeLayout.setOnRefreshListener {
            if (Utils.isNetworkConnected(this))
                viewModel.refresh()
            else
                showNoDataView(NO_INTERNET)
        }

        handleAPICall()

        mBinding.tvClear.setOnClickListener {
            viewModel.clearNotifications().observe(this, Observer { handleRemoveNotification(it) })
        }


    }

    private fun handleRemoveNotification(resource: Resource<BaseResponse>?) {
        when (resource?.status) {
            Status.SUCCESS -> {
                hideProgress(mBinding.progressBar)
                mBinding.swipeLayout.visibility = View.GONE
                mBinding.tvClear.visibility = View.GONE

                showNoDataView(EMPTY_DATA)

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

    private fun handleAPICall() {
        if (Utils.isNetworkConnected(this))
            getNotifications()
        else
            showNoDataView(NO_INTERNET)
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
                    if (Utils.isNetworkConnected(this)) {
                        mBinding.llNoData.visibility = View.GONE
                        if (!mBinding.swipeLayout.isRefreshing)
                            getNotifications()
                        else
                            viewModel.refresh()
                    }
                }

            }
            EMPTY_DATA -> {
                setNoDataViewsText(
                    R.drawable.ic_no_data,
                    getString(R.string.no_notifications_found),
                    getString(R.string.you_will_see_it_here)
                )
                mBinding.noData.btnNext.text = getString(R.string.order_now)
                mBinding.llNoData.visibility = View.VISIBLE

                mBinding.noData.btnNext.visibility = View.GONE
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
                this,
                icon
            )
        )
        mBinding.noData.tvTitle.text = title
        mBinding.noData.tvDesc.text = desc
    }


    private fun getNotifications() {
        viewModel.notificationPagedList.observe(this, Observer {

            mAdapter.submitList(it)


        })

        viewModel.networkState.observe(this, Observer {
            mAdapter.setNetworkState(it)
        })

        viewModel.initialLoad.observe(this, Observer {
            if (it?.status == NetStatus.EMPTY) {
                showNoDataView(EMPTY_DATA)
                mBinding.tvClear.visibility = View.GONE
            } else {
                if (viewModel.pageReq.page == 1) {
                    if (mBinding.swipeLayout.isRefreshing) {
                        mBinding.swipeLayout.isRefreshing = false
                    }
                    //mBinding.tvNoData.visibility = View.GONE
                    mBinding.llNoData.visibility = View.GONE
                    mBinding.rvList.visibility = View.VISIBLE
                    if (it.status == NetStatus.SUCCESS)
                        mBinding.tvClear.visibility = View.VISIBLE
                    mBinding.rvList.requestFocus()


                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onClick(notificationData: NotificationData) {
        startViewOrderActivity(notificationData.order_no)
    }

    override fun retry() {
        viewModel.retry()
    }

    private fun startViewOrderActivity(orderNo: String) {
        val intent = Intent(this, ViewOrderActivity::class.java)
        intent.putExtra(BUN_2, orderNo)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}
