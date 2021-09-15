package com.company.dreamgroceries.data

import com.google.gson.annotations.SerializedName

data class SocialLoginResponse(
    @SerializedName("token_type") val token_type: String,
    @SerializedName("access_token") val access_token: String,
    @SerializedName("refresh_token") val refresh_token: String,
    @SerializedName("data") val data: Profile,
    @SerializedName("message") val message: String,
    @SerializedName("store") val store: Store
)