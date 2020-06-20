package com.fameget.dreamgroceries.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fameget.dreamgroceries.data.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductsToCart(address: List<Product>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: Product): Long


    @Query("select * from product")
    fun getCartProducts(): LiveData<List<Product>>

    @Query("select cart_count from product where id=:productId")
    fun getProductCartCount(productId: Int): LiveData<Int>


    @Query("DELETE from product")
    fun deleteAll()

    @Delete
    fun deleteProduct(addresse: Product)

    @Update
    fun updateProduct(address: Product)
}