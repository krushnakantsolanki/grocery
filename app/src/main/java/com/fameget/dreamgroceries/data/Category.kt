package com.fameget.dreamgroceries.data

data class Category(
    val id: Int,
    val image: String,
    val name: String,
    var isSelected: Boolean
)