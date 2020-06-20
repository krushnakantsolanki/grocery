package com.fameget.dreamgroceries.data

data class NotificationData(
    val created: String,
    val id: Int,
    val message: String,
    val order_no: String,
    val sender_id: Int,
    val status: Int,
    val type: Int
)