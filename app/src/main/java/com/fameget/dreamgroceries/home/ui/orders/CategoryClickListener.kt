package com.fameget.dreamgroceries.home.ui.orders

import com.fameget.dreamgroceries.data.Category

interface CategoryClickListener {
    fun onCategoryClicked(category: Category, position: Int)
}