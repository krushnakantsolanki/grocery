package com.fameget.dreamgroceries.notification

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.fameget.dreamgroceries.data.NotificationData
import com.fameget.dreamgroceries.data.PageReq


class NotificationSourceFactory(val pageReq: PageReq) :
    DataSource.Factory<Int, NotificationData>() {

    val notificationPageSource = MutableLiveData<NotificationPageSource>()

    override fun create(): NotificationPageSource {
        val productsRequest = NotificationPageSource(pageReq)
        notificationPageSource.postValue(productsRequest)
        return productsRequest
    }


}