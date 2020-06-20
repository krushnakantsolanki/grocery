package com.fameget.dreamgroceries.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fameget.dreamgroceries.data.Addresse
import com.fameget.dreamgroceries.data.Product
import com.fameget.dreamgroceries.data.Profile
import com.fameget.dreamgroceries.data.local.dao.AddressDao
import com.fameget.dreamgroceries.data.local.dao.ProductDao
import com.fameget.dreamgroceries.data.local.dao.ProfileDao

/**
 * The Room Database that contains all tables.
 *
 * Note that exportSchema should be true in production databases.
 */
@Database(
    entities = [Profile::class, Addresse::class, Product::class],
    version = 1,
    exportSchema = false
)
//@TypeConverters(MenuConverter::class)
abstract class AppDB : RoomDatabase() {
    abstract fun toInfoDao(): ProfileDao
    abstract fun addressDao(): AddressDao
    abstract fun cartDao(): ProductDao


}