package com.company.dreamgroceries.data

data class OrdersResponse(
    val data: List<Order> = ArrayList(),
    val per_page: Int,
    val total: Int
)