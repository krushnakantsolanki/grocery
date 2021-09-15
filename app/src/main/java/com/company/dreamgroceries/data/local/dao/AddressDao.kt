package com.company.dreamgroceries.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.company.dreamgroceries.data.Addresse

@Dao
interface AddressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAddress(address: List<Addresse>): List<Long>


    @Query("select * from address order by id desc")
    fun getAddresses(): LiveData<List<Addresse>>

    @Query("select count(id) from address")
    fun getAddressCount(): LiveData<Int>

    @Query("select * from address where is_selected = 1 limit 1")
    fun geCurrentAddress(): LiveData<Addresse>

    @Query("DELETE from address")
    fun deleteAll()

    @Delete
    fun deleteAddress(addresse: Addresse)

    @Update
    fun updateAddress(address: Addresse)

    @Query("DELETE FROM address")
    fun delete()


    @Query("UPDATE address set is_selected =0")
    fun markAllAddressUnSelected()

    @Query("UPDATE address set is_selected =1 where id = :addressId")
    fun markAddressSelected(addressId: Int)

}