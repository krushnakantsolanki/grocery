package com.fameget.dreamgroceries.data

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("data")
    val categoryList: List<Category> = ArrayList(),
    val message: String
)