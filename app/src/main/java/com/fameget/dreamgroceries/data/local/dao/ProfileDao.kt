package com.fameget.dreamgroceries.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fameget.dreamgroceries.data.Profile

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: Profile): Long

    @Query("select * from profile limit 1")
    fun getUser(): LiveData<Profile>

    @Query("DELETE FROM profile")
    fun delete()
}