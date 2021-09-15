package com.company.dreamgroceries.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import com.company.dreamgroceries.MyApp
import com.company.dreamgroceries.base.BaseDataSource
import com.company.dreamgroceries.base.resultLiveDataNoDb
import com.company.dreamgroceries.data.BaseResponse
import com.company.dreamgroceries.data.IdReq
import com.company.dreamgroceries.webservices.ApiService
import com.company.dreamgroceries.webservices.Resource
import javax.inject.Inject

class NotificationRepo  : BaseDataSource() {

    @Inject
    lateinit var apiService: ApiService

    init {
        MyApp.getAppComponent().inject(this)
    }



    fun clearNotifications() = resultLiveDataNoDb {
        clearNotificationsReq()
    }.distinctUntilChanged()

    suspend fun clearNotificationsReq() = getResult {
        apiService.clearNotifications(getToken(), IdReq(0))
    }

}
