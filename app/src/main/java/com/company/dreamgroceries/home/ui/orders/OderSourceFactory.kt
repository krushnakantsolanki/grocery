package com.company.dreamgroceries.home.ui.orders

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.company.dreamgroceries.data.Order
import com.company.dreamgroceries.data.PageReq
import com.company.dreamgroceries.data.Product
import com.company.dreamgroceries.data.ProductsRequest


class OderSourceFactory(val pageReq: PageReq) :
    DataSource.Factory<Int, Order>() {

    val orderPageSource = MutableLiveData<OrdersPageSource>()

    override fun create(): OrdersPageSource {
        val ordersReq = OrdersPageSource(pageReq)
        orderPageSource.postValue(ordersReq)
        return ordersReq
    }


}