package com.example.shoppinglistmanager.contentprovider

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.shoppinglistmanager.data.entity.Product
import com.example.shoppinglistmanager.data.local.ProductDao
import com.example.shoppinglistmanager.data.local.ProductDatabase
import java.math.BigDecimal

@SuppressLint("ProviderReadPermissionOnly")
class ProductContentProvider : ContentProvider() {

    private lateinit var productDao: ProductDao

    companion object {
        private const val AUTHORITY = "com.example.shoppinglistmanager"
        private const val TABLE_NAME = "products"

        private const val URI_CODE_PRODUCTS = 1
        private const val URI_CODE_PRODUCT = 2

        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        val PRODUCTS_URI = Uri.parse("content://$AUTHORITY/$TABLE_NAME")!!

        init {
            uriMatcher.addURI(AUTHORITY, TABLE_NAME, URI_CODE_PRODUCTS)
            uriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", URI_CODE_PRODUCT)
        }
    }

    override fun onCreate(): Boolean {
        val context = context ?: return false
        productDao = ProductDatabase.getDatabase(context).ProductDao()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val context = context ?: return null
        val cursor: Cursor = when (uriMatcher.match(uri)) {
            URI_CODE_PRODUCTS -> {
                productDao.getProductsCursor()
            }
            URI_CODE_PRODUCT -> {
                productDao.getProductCursorById(ContentUris.parseId(uri))
            }
            else -> return null
        }
        cursor.setNotificationUri(context.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            URI_CODE_PRODUCTS -> "vnd.android.cursor.dir/$AUTHORITY.$TABLE_NAME"
            URI_CODE_PRODUCT -> "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val context: Context = context ?: return null
        return when (uriMatcher.match(uri)) {
            URI_CODE_PRODUCTS -> {
                val product: Product = createProductFromContentValues(values!!)
                val id: Long = productDao.insertProduct(product)
                if (id >= 0) {
                    val finalUri = ContentUris.withAppendedId(uri, id)
                    context.contentResolver.notifyChange(finalUri, null)
                    finalUri
                } else {
                    null
                }
            }
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val context: Context = context ?: return 0
        return when (uriMatcher.match(uri)) {
            URI_CODE_PRODUCT -> {
                val product: Product = createProductFromContentValues(values!!)
                product.id = ContentUris.parseId(uri)
                val count: Int = productDao.updateProduct(product)
                context.contentResolver.notifyChange(uri, null)
                return count
            }
            else -> 0
        }
    }

    override fun delete(
        uri: Uri, selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val context: Context = context ?: return 0
        return when (uriMatcher.match(uri)) {
            URI_CODE_PRODUCT -> {
                val id: Long = ContentUris.parseId(uri)
                val count: Int = productDao.deleteProductById(id)
                context.contentResolver.notifyChange(uri, null)
                count
            }
            else -> 0
        }
    }

    private fun createProductFromContentValues(values: ContentValues): Product {
        return Product(
            name = values.getAsString("name"),
            price = BigDecimal(values.getAsString("price")),
            quantity = values.getAsInteger("quantity"),
            purchased = values.getAsBoolean("purchased")
        )
    }
}