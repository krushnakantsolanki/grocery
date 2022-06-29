package com.company.dreamgroceries.notification

import com.company.dreamgroceries.data.NotificationData

interface NotificationClickListener {
    fun onClick(notificationData: NotificationData)
    fun retry()
}