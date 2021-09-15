package com.company.dreamgroceries.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Productimage(
    var image: String,
    var product_id: Int
) :Parcelable