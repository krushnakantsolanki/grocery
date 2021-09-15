package com.company.dreamgroceries.home.ui.orders

import com.company.dreamgroceries.data.Category

interface CategoryClickListener {
    fun onCategoryClicked(category: Category, position: Int)
}