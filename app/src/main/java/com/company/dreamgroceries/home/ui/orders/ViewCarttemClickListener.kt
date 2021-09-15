package com.company.dreamgroceries.home.ui.orders

import com.company.dreamgroceries.data.Category
import com.company.dreamgroceries.data.Product

interface ViewCarttemClickListener {
    fun removeProduct(product: Product)
    fun updateProduct(product: Product)
}