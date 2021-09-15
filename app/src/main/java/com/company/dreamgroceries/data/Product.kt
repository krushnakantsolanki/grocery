package com.company.dreamgroceries.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "product")
data class Product(
    val description: String,
    @PrimaryKey
    val id: Int,
    val image: String?,
    val ingredients: String?,
    val mrp: String?,
    val name: String,
    val special_price: String?,
    val tax_percentage: String?,
    val total_tax: String?,
    var cart_count: Int = 0
) : Parcelable {
    @Ignore
    val productimages: MutableList<Productimage> = ArrayList()
}