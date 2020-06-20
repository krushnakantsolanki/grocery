package com.fameget.dreamgroceries.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateProfileReq(
    var country_code: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val otp: String = "",
    val phone_number: String ,
    val update_profile: Int = 1
) : Parcelable