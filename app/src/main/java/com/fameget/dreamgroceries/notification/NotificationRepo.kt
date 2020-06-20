package com.fameget.dreamgroceries.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import com.fameget.dreamgroceries.MyApp
import com.fameget.dreamgroceries.base.BaseDataSource
import com.fameget.dreamgroceries.base.resultLiveDataNoDb
import com.fameget.dreamgroceries.data.BaseResponse
import com.fameget.dreamgroceries.data.IdReq
import com.fameget.dreamgroceries.webservices.ApiService
import com.fameget.dreamgroceries.webservices.Resource
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
