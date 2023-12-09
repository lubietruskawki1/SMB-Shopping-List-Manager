package com.example.shoppinglistmanager.data.entity

data class Product(
    var id: String,
    var name: String,
    var price: String,
    var quantity: Int,
    var purchased: Boolean
)