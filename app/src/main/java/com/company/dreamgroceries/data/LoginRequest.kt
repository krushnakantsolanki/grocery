package com.company.dreamgroceries.data

import com.google.gson.annotations.SerializedName

data class LoginRequest
    (

    @SerializedName("country_code") val country_code: Int,
    @SerializedName("phone_number") val phone_number: String,
    @SerializedName("otp") val otp: Int,
    @SerializedName("device_token") val device_token: String,
    @SerializedName("device_type") val device_type: Int
    )
