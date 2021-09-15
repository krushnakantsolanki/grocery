package com.company.dreamgroceries.home.ui.orders

import com.company.dreamgroceries.data.Product

interface ProductClickListener {
    fun onClick(product: Product)
    fun retry()
}