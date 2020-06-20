package com.fameget.dreamgroceries.notification

import com.fameget.dreamgroceries.data.NotificationData
import com.fameget.dreamgroceries.data.Product

interface NotificationClickListener {
    fun onClick(notificationData: NotificationData)
    fun retry()
}