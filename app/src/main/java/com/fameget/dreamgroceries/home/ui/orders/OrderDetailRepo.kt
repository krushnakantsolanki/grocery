package com.fameget.dreamgroceries.home.ui.orders

import androidx.lifecycle.distinctUntilChanged
import com.fameget.dreamgroceries.MyApp
import com.fameget.dreamgroceries.base.BaseDataSource
import com.fameget.dreamgroceries.base.resultLiveDataNoDb
import com.fameget.dreamgroceries.data.CancelOrderReq
import com.fameget.dreamgroceries.data.PageReq
import com.fameget.dreamgroceries.webservices.ApiService
import javax.inject.Inject

class OrderDetailRepo : BaseDataSource() {

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



}