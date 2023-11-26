package com.example.shoppinglistmanager.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import com.example.shoppinglistmanager.contentprovider.ProductContentProvider
import com.example.shoppinglistmanager.data.entity.Product
import com.example.shoppinglistmanager.data.local.ProductDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ProductRepository(
    productDao: ProductDao,
    private val contentResolver: ContentResolver
) {

    val allProducts: Flow<List<Product>> = productDao.getProducts()

    suspend fun insert(product: Product) {
        withContext(Dispatchers.IO) {
            val values = createContentValuesFromProduct(product)
            contentResolver.insert(ProductContentProvider.PRODUCTS_URI, values)
        }
    }

    suspend fun update(product: Product) {
        withContext(Dispatchers.IO) {
            val values = createContentValuesFromProduct(product)
            val uri = ContentUris.withAppendedId(
                ProductContentProvider.PRODUCTS_URI,
                product.id
            )
            contentResolver.update(uri, values, null, null)
        }
    }

    suspend fun delete(product: Product) {
        withContext(Dispatchers.IO) {
            val uri = ContentUris.withAppendedId(
                ProductContentProvider.PRODUCTS_URI,
                product.id
            )
            contentResolver.delete(uri, null, null)
        }
    }

    private fun createContentValuesFromProduct(product: Product): ContentValues {
        return ContentValues().apply {
            put("name", product.name)
            put("price", product.price.toString())
            put("quantity", product.quantity)
            put("purchased", product.purchased)
        }
    }
}