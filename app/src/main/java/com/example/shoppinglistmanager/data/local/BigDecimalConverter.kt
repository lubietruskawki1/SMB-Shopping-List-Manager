package com.example.shoppinglistmanager.data.local

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.math.RoundingMode

class BigDecimalConverter {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal): String {
        return value.toString()
    }

    @TypeConverter
    fun toBigDecimal(value: String): BigDecimal {
        // Set scale to store values with two exactly decimal places
        return BigDecimal(value).setScale(2, RoundingMode.HALF_UP)
    }
}