package com.example.shoppinglistmanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String,
    var price: BigDecimal,
    var quantity: Int,
    var purchased: Boolean
)