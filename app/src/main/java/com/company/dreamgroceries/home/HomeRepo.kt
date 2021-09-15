package com.company.dreamgroceries.home

import androidx.lifecycle.distinctUntilChanged
import com.company.dreamgroceries.MyApp
import com.company.dreamgroceries.base.BaseDataSource
import com.company.dreamgroceries.base.resultLiveDataNoDb
import com.company.dreamgroceries.webservices.ApiService
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
