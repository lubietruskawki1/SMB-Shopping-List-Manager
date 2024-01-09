package com.example.shoppinglistmanager.ui.utils.store

import android.content.Context
import android.location.Location
import com.example.shoppinglistmanager.data.entity.Store
import com.example.shoppinglistmanager.ui.utils.showToast

private const val DUMMY_ID = "DUMMY_ID"

class StoreManager(
    private var name: String,
    private var description: String,
    private var radius: String
) {

    constructor(storeStates: StoreStates) : this(
        name = storeStates.name.value,
        description = storeStates.description.value,
        radius = storeStates.radius.value
    )
    
    private fun isValidFloat(string: String): Boolean {
        return string.toFloatOrNull() != null
    }

    private fun isValidName(context: Context): Boolean {
        if (name.isBlank()) {
            showToast(context, "Name cannot be empty.")
            return false
        }

        // To prevent the UI from not scaling correctly
        if (name.length > 50) {
            showToast(
                context,
                "Name cannot be longer than 50 characters."
            )
            return false
        }

        return true
    }

    private fun isValidRadius(context: Context): Boolean {
        if (radius.isBlank()) {
            showToast(context, "Description cannot be empty.")
            return false
        }

        if (!isValidFloat(radius)) {
            showToast(context, "Radius must be a valid number.")
            return false
        }

        if (radius.toFloat() <= 0) {
            showToast(context, "Radius must be positive.")
            return false
        }

        return true
    }

    fun isValidStore(context: Context): Boolean {
        return isValidName(context) &&
               isValidRadius(context)
    }

    fun createStore(location: Location): Store {
        return Store(
            id = DUMMY_ID,
            name = name.trim(),
            description = description.trim(),
            radius = radius.toFloat(),
            latitude = location.latitude,
            longitude = location.longitude
        )
    }

    fun updateStore(store: Store) {
        store.name = name.trim()
        store.description = description
        store.radius = radius.toFloat()
    }

}