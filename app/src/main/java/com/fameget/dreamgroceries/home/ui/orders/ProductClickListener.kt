package com.fameget.dreamgroceries.home.ui.orders

import com.fameget.dreamgroceries.data.Product

interface ProductClickListener {
    fun onClick(product: Product)
    fun retry()
}