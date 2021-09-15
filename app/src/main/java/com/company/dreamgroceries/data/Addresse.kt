package com.company.dreamgroceries.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "address")
@Parcelize
data class Addresse(
    var city: String? = "",
    val created_at: String? = "",
    var floor: String?,
    @PrimaryKey
    val id: Int = 0,
    var latitude: String?,
    var longitude: String?,
    var state: String? = "",
    var street: String?,
    val updated_at: String? = "",
    val user_id: Int? = 0,
    var zip_code: String?,
    var is_selected: Int? = 0


) : Parcelable