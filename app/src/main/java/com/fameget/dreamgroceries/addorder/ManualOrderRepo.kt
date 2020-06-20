package com.fameget.dreamgroceries.addorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import com.fameget.dreamgroceries.MyApp
import com.fameget.dreamgroceries.base.BaseDataSource
import com.fameget.dreamgroceries.base.resultLiveDataNoDb
import com.fameget.dreamgroceries.data.ManualPickUpOrderReq
import com.fameget.dreamgroceries.webservices.ApiService
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
