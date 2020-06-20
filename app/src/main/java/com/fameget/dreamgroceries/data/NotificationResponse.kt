package com.fameget.dreamgroceries.data

data class NotificationResponse(
    val data: List<NotificationData>,
    val message: String,
    val per_page: Int,
    val total: Int
)