package com.fameget.dreamgroceries.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    val additional_instructions: String?,
    val created_at: String,
    val id: Int,
    val order_address: Addresse?,
    val invoice :String?,
    val order_delivered_date: String?,
    val order_details: OrderDetails?,
    val order_manual_products: List<OrderManualProduct>,
    val order_no: String,
    val order_products: List<OrderProduct>?,
    val order_type: Int,
    val payment_type: Int,
    val pickup_date: String?,
    var status: Int
) : Parcelable