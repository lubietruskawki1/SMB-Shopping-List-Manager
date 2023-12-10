package com.example.shoppinglistmanager.ui.utils

import android.content.Context
import com.example.shoppinglistmanager.data.entity.Product
import java.math.BigDecimal

private const val DUMMY_ID = "DUMMY_ID"

class ProductManager(
    var name: String,
    private var price: String,
    private var quantity: String,
    private var purchased: Boolean
) {

    constructor(productStates: ProductStates) : this(
        name = productStates.name.value,
        price = productStates.price.value,
        quantity = productStates.quantity.value,
        purchased = productStates.purchased.value
    )

    private fun isValidBigDecimal(string: String): Boolean {
        return string.toBigDecimalOrNull() != null
    }

    private fun isValidInt(string: String): Boolean {
        return string.toIntOrNull() != null
    }

    private fun hasMaxTwoDecimalPlaces(string: String): Boolean {
        val parts: List<String> = string.trim().split(".")
        if (parts.size == 2) {
            return parts[1].length <= 2
        }
        return true
    }

    private fun isValidName(context: Context): Boolean {
        if (name.isBlank()) {
            showToast(context, "Name cannot be empty.")
            return false
        }

        // To prevent the UI from not scaling correctly
        if (name.length > 50) {
            showToast(context,
                "Name cannot be longer than 50 characters.")
            return false
        }

        return true
    }

    private fun isValidPrice(context: Context): Boolean {
        if (price.isBlank()) {
            showToast(context, "Price cannot be empty.")
            return false
        }

        if (!isValidBigDecimal(price)) {
            showToast(context, "Price must be a valid number.")
            return false
        }

        if (price.toBigDecimal() < BigDecimal.ZERO) {
            showToast(context, "Price cannot be negative.")
            return false
        }

        if (!hasMaxTwoDecimalPlaces(price)) {
            showToast(context,
                "Price cannot have more than two decimal places.")
            return false
        }

        return true
    }

    private fun isValidQuantity(context: Context): Boolean {
        if (quantity.isBlank()) {
            showToast(context, "Quantity cannot be empty.")
            return false
        }

        if (!isValidInt(quantity)) {
            showToast(context, "Quantity must be a valid integer.")
            return false
        }

        if (quantity.toInt() <= 0) {
            showToast(context, "Quantity must be positive.")
            return false
        }

        return true
    }

    fun isValidProduct(context: Context): Boolean {
        return isValidName(context) &&
               isValidPrice(context) &&
               isValidQuantity(context)
    }

    fun createProduct(): Product {
        return Product(
            id = DUMMY_ID,
            name = name.trim(),
            price = formattedPrice(),
            quantity = quantity.toInt(),
            purchased = purchased
        )
    }

    fun updateProduct(product: Product) {
        product.name = name.trim()
        product.price = formattedPrice()
        product.quantity = quantity.toInt()
        product.purchased = purchased
    }

    private fun formattedPrice(): String {
        // Format the price to have exactly two decimal places
        return String.format("%.2f", price.toDouble())
    }

}