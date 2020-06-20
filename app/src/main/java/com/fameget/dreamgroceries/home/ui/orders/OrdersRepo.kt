package com.fameget.dreamgroceries.home.ui.orders

import androidx.lifecycle.distinctUntilChanged
import com.fameget.dreamgroceries.MyApp
import com.fameget.dreamgroceries.base.BaseDataSource
import com.fameget.dreamgroceries.base.resultLiveDataNoDb
import com.fameget.dreamgroceries.data.CancelOrderReq
import com.fameget.dreamgroceries.data.OrderReq
import com.fameget.dreamgroceries.data.PageReq
import com.fameget.dreamgroceries.webservices.ApiService
import javax.inject.Inject

class OrdersRepo : BaseDataSource() {

    @Inject
    lateinit var apiService: ApiService

    init {
        MyApp.getAppComponent().inject(this)
    }


    suspend fun getOrdersCall(pageReq: PageReq) = getResult {
        apiService.getOrdersCall(getToken(), pageReq)
    }


    fun getOrdersReq(pageReq: PageReq) = resultLiveDataNoDb { getOrdersCall(pageReq) }
        .distinctUntilChanged()


    suspend fun cancelOrderCall(cancelOrderReq: CancelOrderReq) = getResult {
        apiService.cancelOrderCall(getToken(), cancelOrderReq)
    }


    fun cancelOrderReq(cancelOrderReq: CancelOrderReq) =
        resultLiveDataNoDb { cancelOrderCall(cancelOrderReq) }
            .distinctUntilChanged()

    fun getOrderReq(orderReq: OrderReq) = resultLiveDataNoDb { getOrderCall(orderReq) }
        .distinctUntilChanged()

    suspend fun getOrderCall(orderReq: OrderReq) = getResult {
        apiService.getOrder(getToken(), orderReq)
    }

}