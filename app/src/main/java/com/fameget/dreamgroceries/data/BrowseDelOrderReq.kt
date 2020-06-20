package com.fameget.dreamgroceries.data

data class BrowseDelOrderReq(
    val address_id: Int,
    val order_type: Int,
    val payment_type: Int
)