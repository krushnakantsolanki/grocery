package com.company.dreamgroceries.data

data class UserRegRequest(
    val city: String = "",
    val email: String = "",
    val first_name: String,
    val floor: String = "",
    val last_name: String,
    val latitude: String = "",
    val longitude: String = "",
    val state: String = "",
    val street: String = "",
    val zip_code: String = ""
)