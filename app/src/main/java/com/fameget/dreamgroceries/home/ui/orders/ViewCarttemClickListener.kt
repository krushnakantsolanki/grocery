package com.fameget.dreamgroceries.home.ui.orders

import com.fameget.dreamgroceries.data.Category
import com.fameget.dreamgroceries.data.Product

interface ViewCarttemClickListener {
    fun removeProduct(product: Product)
    fun updateProduct(product: Product)
}