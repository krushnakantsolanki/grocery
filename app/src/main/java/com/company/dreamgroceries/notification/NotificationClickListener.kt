package com.company.dreamgroceries.notification

import com.company.dreamgroceries.data.NotificationData
import com.company.dreamgroceries.data.Product

interface NotificationClickListener {
    fun onClick(notificationData: NotificationData)
    fun retry()
}