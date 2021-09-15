package com.company.dreamgroceries.addorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import com.company.dreamgroceries.MyApp
import com.company.dreamgroceries.base.BaseDataSource
import com.company.dreamgroceries.base.resultLiveDataNoDb
import com.company.dreamgroceries.data.ManualPickUpOrderReq
import com.company.dreamgroceries.webservices.ApiService
import javax.inject.Inject

class ManualOrderRepo : BaseDataSource() {


    @Inject
    lateinit var apiService: ApiService

    init {
        MyApp.getAppComponent().inject(this)
    }

    suspend fun manualPickUpOrderCall(createPickUpOrderReq: ManualPickUpOrderReq) =
        getResult { apiService.manualPickOrderCall(getToken(), createPickUpOrderReq) }


    fun manualPickUpOrderReq(createPickUpOrderReq: ManualPickUpOrderReq) =
        resultLiveDataNoDb { manualPickUpOrderCall(createPickUpOrderReq) }.distinctUntilChanged()

    fun getAddressCount(): LiveData<Int> {
        return MyApp.getInstance().addressDao().getAddressCount()
    }
}
