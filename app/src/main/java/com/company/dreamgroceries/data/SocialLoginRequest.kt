package com.company.dreamgroceries.data

data class SocialLoginRequest(
    val uid: String?,
    val first_name: String?,
    val last_name: String?,
    val email: String?,
    val login_type: Int,
    val device_token :String?
)