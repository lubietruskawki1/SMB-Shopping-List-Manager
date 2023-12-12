package com.example.shoppinglistmanager.data.repository

import android.util.Log
import com.example.shoppinglistmanager.data.entity.Product
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow

class ProductRepository(
    private val firebaseDatabase: FirebaseDatabase,
    firebaseUser: FirebaseUser
) {

    val allProductsMutable = MutableStateFlow(HashMap<String, Product>())
    private val path: String = "users/${firebaseUser.uid}/products"

    init {
        firebaseDatabase.getReference(path).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val product: Product = createProductFromSnapshot(snapshot)
                allProductsMutable.value = allProductsMutable.value.toMutableMap().apply {
                    put(product.id, product)
                } as HashMap<String, Product>
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val product: Product = createProductFromSnapshot(snapshot)
                allProductsMutable.value = allProductsMutable.value.toMutableMap().apply {
                    put(product.id, product)
                } as HashMap<String, Product>
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val product: Product = createProductFromSnapshot(snapshot)
                allProductsMutable.value = allProductsMutable.value.toMutableMap().apply {
                    remove(product.id, product)
                } as HashMap<String, Product>
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                val product: Product = createProductFromSnapshot(snapshot)
                allProductsMutable.value = allProductsMutable.value.toMutableMap().apply {
                    remove(product.id, product)
                    put(product.id, product)
                } as HashMap<String, Product>
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ProductRepository", "Database operation cancelled: ${error.message}")
            }
        })
    }

    private fun createProductFromSnapshot(snapshot: DataSnapshot): Product {
        return Product(
            id = snapshot.ref.key as String,
            name = snapshot.child("name").value as String,
            price = snapshot.child("price").value as String,
            quantity = (snapshot.child("quantity").value as Long).toInt(),
            purchased = snapshot.child("purchased").value as Boolean
        )
    }

    fun insert(product: Product): String {
        firebaseDatabase.getReference(path).push().also {
            product.id = it.ref.key!!
            it.setValue(product)
            return product.id
        }
    }

    fun update(product: Product) {
        val ref = firebaseDatabase.getReference("$path/${product.id}")
        ref.child("name").setValue(product.name)
        ref.child("price").setValue(product.price)
        ref.child("quantity").setValue(product.quantity)
        ref.child("purchased").setValue(product.purchased)
    }

    fun delete(product: Product) {
        firebaseDatabase.getReference("$path/${product.id}").removeValue()
    }

}