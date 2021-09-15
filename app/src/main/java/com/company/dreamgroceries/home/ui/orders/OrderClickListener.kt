package com.company.dreamgroceries.home.ui.orders

import com.company.dreamgroceries.data.Order

interface OrderClickListener {
    fun onCancelOrder(order: Order, position: Int)
    fun onOrderClicked(order: Order)
    fun retry()
}