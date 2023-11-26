package com.example.shoppinglistmanager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.shoppinglistmanager.data.entity.Product

@Database(entities = [Product::class], version = 1)
@TypeConverters(BigDecimalConverter::class)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun ProductDao(): ProductDao

    // Singleton
    companion object {
        private var instance: ProductDatabase? = null

        fun getDatabase(context: Context): ProductDatabase {
            if (instance != null) {
                return instance as ProductDatabase
            }

            instance = Room.databaseBuilder(
                context,
                ProductDatabase::class.java,
                "productDatabase"
            ).build()
            return instance as ProductDatabase
        }
    }

}