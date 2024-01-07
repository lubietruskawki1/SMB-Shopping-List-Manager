package com.example.shoppinglistmanager.ui.utils.product

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.shoppinglistmanager.data.entity.Product

class ProductStates(
    var name: MutableState<String>,
    val price: MutableState<String>,
    val quantity: MutableState<String>,
    val purchased: MutableState<Boolean>
) {
    fun reset() {
        name.value = ""
        price.value = ""
        quantity.value = ""
        purchased.value = false
    }

    fun reset(product: Product) {
        name.value = product.name
        price.value = product.price
        quantity.value = product.quantity.toString()
        purchased.value = product.purchased
    }
}

@Composable
fun createProductStates(): ProductStates {
    return ProductStates(
        name = remember { mutableStateOf("") },
        price = remember { mutableStateOf("") },
        quantity = remember { mutableStateOf("") },
        purchased = remember { mutableStateOf(false) }
    )
}

@Composable
fun createProductStates(product: Product): ProductStates {
    return ProductStates(
        name = remember { mutableStateOf(product.name) },
        price = remember { mutableStateOf(product.price) },
        quantity = remember { mutableStateOf(product.quantity.toString()) },
        purchased = remember { mutableStateOf(product.purchased) }
    )
}