package com.fameget.dreamgroceries.home.ui.orders

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.fameget.dreamgroceries.data.Order
import com.fameget.dreamgroceries.data.PageReq
import com.fameget.dreamgroceries.data.Product
import com.fameget.dreamgroceries.data.ProductsRequest


class OderSourceFactory(val pageReq: PageReq) :
    DataSource.Factory<Int, Order>() {

    val orderPageSource = MutableLiveData<OrdersPageSource>()

    override fun create(): OrdersPageSource {
        val ordersReq = OrdersPageSource(pageReq)
        orderPageSource.postValue(ordersReq)
        return ordersReq
    }


}