package com.example.shoppinglistmanager.data.entity

data class Store(
    var id: String,
    var name: String,
    var description: String,
    var radius: Float,
    var latitude: Double,
    var longitude: Double
)