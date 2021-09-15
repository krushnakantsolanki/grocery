package com.company.dreamgroceries.home.ui.orders

import androidx.lifecycle.distinctUntilChanged
import com.company.dreamgroceries.MyApp
import com.company.dreamgroceries.base.BaseDataSource
import com.company.dreamgroceries.base.resultLiveDataNoDb
import com.company.dreamgroceries.data.CancelOrderReq
import com.company.dreamgroceries.data.PageReq
import com.company.dreamgroceries.webservices.ApiService
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