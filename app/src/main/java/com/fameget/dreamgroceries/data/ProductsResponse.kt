package com.fameget.dreamgroceries.data

import com.google.gson.annotations.SerializedName

data class ProductsResponse(
    @SerializedName("data")
    val productsList: List<Product> = ArrayList(),
    val message: String
)