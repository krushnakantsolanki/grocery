package com.company.dreamgroceries.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.company.dreamgroceries.data.BaseResponse
import com.company.dreamgroceries.data.NotificationData
import com.company.dreamgroceries.data.PageReq
import com.company.dreamgroceries.webservices.NetworkState
import com.company.dreamgroceries.webservices.Resource

class NotificationViewModel(val notificationRepo: NotificationRepo) : ViewModel() {

    var initialLoad: LiveData<NetworkState>
    var networkState: LiveData<NetworkState>
    private var itemDataSourceFactory: NotificationSourceFactory
    val pageReq = PageReq()
    var notificationPagedList: LiveData<PagedList<NotificationData>>
    private var liveDataSource: LiveData<NotificationPageSource>

    fun refresh() {
        itemDataSourceFactory.notificationPageSource.value?.invalidate()
    }


    init {
        itemDataSourceFactory = NotificationSourceFactory(pageReq)
        networkState = Transformations.switchMap(
            itemDataSourceFactory.notificationPageSource
        ) { dataSource -> dataSource.networkState }

        initialLoad = Transformations.switchMap(
            itemDataSourceFactory.notificationPageSource
        ) { dataSource -> dataSource.initialLoad }

        liveDataSource = itemDataSourceFactory.notificationPageSource
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(5)
            .build()
        notificationPagedList = LivePagedListBuilder(itemDataSourceFactory, config)
            .build()
    }

    fun retry() {
        val listing = liveDataSource.value
        listing?.retry?.invoke()

    }

    fun clearNotifications(): LiveData<Resource<BaseResponse>> {
        return notificationRepo.clearNotifications()

    }


}