package com.fameget.dreamgroceries.home.ui.orders

import com.fameget.dreamgroceries.data.Order

interface OrderClickListener {
    fun onCancelOrder(order: Order, position: Int)
    fun onOrderClicked(order: Order)
    fun retry()
}