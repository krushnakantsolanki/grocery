package com.fameget.dreamgroceries.home

import androidx.lifecycle.distinctUntilChanged
import com.fameget.dreamgroceries.MyApp
import com.fameget.dreamgroceries.base.BaseDataSource
import com.fameget.dreamgroceries.base.resultLiveDataNoDb
import com.fameget.dreamgroceries.webservices.ApiService
import javax.inject.Inject

class HomeRepo : BaseDataSource() {


    @Inject
    lateinit var apiService: ApiService

    init {
        MyApp.getAppComponent().inject(this)
    }


    fun getNotCount() =
        resultLiveDataNoDb { getNotCountReq() }
            .distinctUntilChanged()


    suspend fun getNotCountReq() =
        getResult { apiService.getNotCount(getToken()) }


}
