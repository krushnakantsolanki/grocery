package com.fameget.dreamgroceries.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderDetails(
    val order_id: Int,
    val sub_total: String,
    val tax_amt: String,
    val total_amt: String
) :Parcelable