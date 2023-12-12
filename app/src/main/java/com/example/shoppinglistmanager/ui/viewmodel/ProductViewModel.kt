package com.example.shoppinglistmanager.ui.viewmodel

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistmanager.data.entity.Product
import com.example.shoppinglistmanager.data.repository.ProductRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val application: Application
) : AndroidViewModel(application) {

    private val productRepository: ProductRepository
    private var firebaseDatabase: FirebaseDatabase
    private var firebaseUser: FirebaseUser
    val products: StateFlow<HashMap<String, Product>>

    init {
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        productRepository = ProductRepository(firebaseDatabase, firebaseUser)
        products = productRepository.allProductsMutable
    }

    fun insertProduct(product: Product) {
        viewModelScope.launch {
            val productId: String = productRepository.insert(product)

            val broadcastIntent = Intent("com.example.NEW_PRODUCT_ADDED")
            val extras = Bundle().apply {
                putString("productId", productId)
                putString("productName", product.name)
                putString("productPrice", product.price)
                putInt("productQuantity", product.quantity)
            }
            broadcastIntent.putExtras(extras)
            application.applicationContext.sendBroadcast(broadcastIntent)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            productRepository.update(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            productRepository.delete(product)
        }
    }

}