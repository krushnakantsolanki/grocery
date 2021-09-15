package com.company.dreamgroceries.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderManualProduct(
    val order_id: Int,
    val product_name: String
) : Parcelable