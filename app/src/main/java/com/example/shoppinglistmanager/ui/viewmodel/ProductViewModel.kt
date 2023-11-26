package com.example.shoppinglistmanager.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistmanager.data.entity.Product
import com.example.shoppinglistmanager.data.local.ProductDao
import com.example.shoppinglistmanager.data.local.ProductDatabase
import com.example.shoppinglistmanager.data.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository: ProductRepository
    var products: Flow<List<Product>>

    init {
        val productDao: ProductDao = ProductDatabase.getDatabase(application).ProductDao()
        val contentResolver = application.contentResolver
        productRepository = ProductRepository(productDao, contentResolver)
        products = productRepository.allProducts
    }

    fun insertProduct(product: Product) {
        viewModelScope.launch {
            productRepository.insert(product)
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