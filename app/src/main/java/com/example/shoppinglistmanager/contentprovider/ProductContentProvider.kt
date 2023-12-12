package com.example.shoppinglistmanager.contentprovider

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.example.shoppinglistmanager.data.entity.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

private const val DUMMY_ID = "DUMMY_ID"

@SuppressLint("ProviderReadPermissionOnly")
class ProductContentProvider : ContentProvider() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var path: String

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
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        path = "users/${firebaseUser.uid}/products"
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val context = context ?: return null
        val cursor = MatrixCursor(arrayOf(
            "id", "name", "price", "quantity", "purchased"
        ))
        when (uriMatcher.match(uri)) {
            URI_CODE_PRODUCTS -> {
                firebaseDatabase.getReference(path)
                    .get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val dataSnapshot: DataSnapshot = task.result
                            for (snapshot in dataSnapshot.children) {
                                addProductRow(cursor, dataSnapshot)
                            }
                        }
                }
            }
            URI_CODE_PRODUCT -> {
                val productId: Long = ContentUris.parseId(uri)
                firebaseDatabase.getReference(path).child(productId.toString())
                    .get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val dataSnapshot: DataSnapshot = task.result
                            addProductRow(cursor, dataSnapshot)
                        }
                    }
            }
            else -> return null
        }
        cursor.setNotificationUri(context.contentResolver, uri)
        return cursor
    }

    private fun addProductRow(cursor: MatrixCursor, snapshot: DataSnapshot) {
        val product: Product? = snapshot.getValue(Product::class.java)
        product?.let {
            cursor.addRow(arrayOf(
                it.id, it.name, it.price, it.quantity, it.purchased
            ))
        }
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
                val id: String? = firebaseDatabase.getReference(path).push().key
                if (id != null) {
                    product.id = id
                    firebaseDatabase.getReference(path).child(id).setValue(product)
                    val finalUri = Uri.withAppendedPath(uri, id)
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
                val id: String = uri.lastPathSegment ?: return 0
                product.id = id
                firebaseDatabase.getReference(path).child(id).setValue(product)
                context.contentResolver.notifyChange(uri, null)
                1
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
                val id: String = uri.lastPathSegment ?: return 0
                firebaseDatabase.getReference(path).child(id).removeValue()
                context.contentResolver.notifyChange(uri, null)
                1
            }
            else -> 0
        }
    }

    private fun createProductFromContentValues(values: ContentValues): Product {
        return Product(
            id = DUMMY_ID,
            name = values.getAsString("name"),
            price = values.getAsString("price"),
            quantity = values.getAsInteger("quantity"),
            purchased = values.getAsBoolean("purchased")
        )
    }
}