package com.fameget.dreamgroceries.home.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.fameget.dreamgroceries.data.*
import com.fameget.dreamgroceries.webservices.NetworkState
import com.fameget.dreamgroceries.webservices.Resource

class OrdersViewModel(val ordersRepo: OrdersRepo) : ViewModel() {

    var orderPagedList: LiveData<PagedList<Order>>
    private var liveDataSource: MutableLiveData<OrdersPageSource>
    var initialLoad: LiveData<NetworkState>
    var networkState: LiveData<NetworkState>
    private var itemDataSourceFactory: OderSourceFactory
    val pageReq = PageReq()

    fun getOrders(): LiveData<Resource<OrdersResponse>> {
        return ordersRepo.getOrdersReq(pageReq)
    }

    fun cancelOrder(orderNo: String): LiveData<Resource<BaseResponse>> {
        return ordersRepo.cancelOrderReq(getCancelOrderReq(orderNo))
    }

    private fun getCancelOrderReq(orderNo: String): CancelOrderReq {
        return CancelOrderReq(orderNo)
    }


    init {
        itemDataSourceFactory = OderSourceFactory(pageReq)
        networkState = Transformations.switchMap(
            itemDataSourceFactory.orderPageSource
        ) { dataSource -> dataSource.networkState }

        initialLoad = Transformations.switchMap(
            itemDataSourceFactory.orderPageSource
        ) { dataSource -> dataSource.initialLoad }

        liveDataSource = itemDataSourceFactory.orderPageSource
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(5)
            .build()
        orderPagedList = LivePagedListBuilder(itemDataSourceFactory, config)
            .build()
    }

    fun retry() {
        val listing = liveDataSource.value
        listing?.retry?.invoke()

    }

    fun refresh() {
        itemDataSourceFactory.orderPageSource.value?.invalidate()
    }


    fun resetOrdersReq() {
        pageReq.page = 1

    }

    fun getOrder(orderReq: OrderReq): LiveData<Resource<OrderResponse>> {
        return ordersRepo.getOrderReq(orderReq)
    }
}