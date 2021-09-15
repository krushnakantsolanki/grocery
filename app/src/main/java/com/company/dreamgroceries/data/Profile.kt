package com.company.dreamgroceries.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class Profile(
    /*@PrimaryKey @SerializedName("id") val id: Int,
    @SerializedName("uid") val uid: String,
    @SerializedName("first_name") val first_name: String,
    @SerializedName("last_name") val last_name: String,
    @SerializedName("email") val email: String,
    @SerializedName("country_code") val country_code: Int,
    @SerializedName("phone_number") val phone_number: Int,
    @SerializedName("is_active") val is_active: Int,
    @SerializedName("login_type") val login_type: Int,
    @SerializedName("role") val role: Int,
    @SerializedName("created_at") val created_at: Int*/
    //@SerializedName("addresses") val addresses : List<String>,
    // @SerializedName("cart") val cart : List<String>

    @PrimaryKey val id: Int,
    val uid: String,
    val first_name: String?,
    val last_name: String?,
    val email: String? = "",
    val country_code: Int,
    val phone_number: String = "",
    val login_type: Int,
    val role: Int,
    val created_at: Int,
    val profile_complete: Int,
    val is_active: Int,
    var notification_status :Int



) {
    @Ignore
    var addresses: List<Addresse> = ArrayList()
}
