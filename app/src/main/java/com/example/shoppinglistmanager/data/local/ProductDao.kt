package com.example.shoppinglistmanager.data.local

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.shoppinglistmanager.data.entity.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    fun getProducts(): Flow<List<Product>>

    @Insert
    fun insertProduct(product: Product): Long

    @Update
    fun updateProduct(product: Product): Int

    @Delete
    suspend fun deleteProduct(product: Product): Int

    @Query("SELECT * FROM products")
    fun getProductsCursor(): Cursor

    @Query("SELECT * FROM products WHERE id = :productId")
    fun getProductCursorById(productId: Long): Cursor

    @Query("DELETE FROM products WHERE id = :productId")
    fun deleteProductById(productId: Long): Int
}