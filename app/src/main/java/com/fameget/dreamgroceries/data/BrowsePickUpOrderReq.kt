package com.fameget.dreamgroceries.data

data class BrowsePickUpOrderReq(
    val pickup_date: String,
    val order_type: Int,
    val payment_type: Int
)