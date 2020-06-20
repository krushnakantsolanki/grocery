package com.fameget.dreamgroceries.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderProduct(
    val description: String,
    val image: String,
    val ingredients: String?,
    val name: String,
    val order_id: Int,
    val price: String,
    val quantity: Int,
    val sku: String,
    val total_price: String
) : Parcelable