package com.example.shoppinglistmanager.ui.utils.store

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.shoppinglistmanager.data.entity.Store

class StoreStates(
    var name: MutableState<String>,
    val description: MutableState<String>,
    val radius: MutableState<String>
) {
    fun reset() {
        name.value = ""
        description.value = ""
        radius.value = ""
    }

    fun reset(store: Store) {
        name.value = store.name
        description.value = store.description
        radius.value = store.radius.toString()
    }
}

@Composable
fun createStoreStates(): StoreStates {
    return StoreStates(
        name = remember { mutableStateOf("") },
        description = remember { mutableStateOf("") },
        radius = remember { mutableStateOf("") }
    )
}

@Composable
fun createStoreStates(store: Store): StoreStates {
    return StoreStates(
        name = remember { mutableStateOf(store.name) },
        description = remember { mutableStateOf(store.description) },
        radius = remember { mutableStateOf(store.radius.toString()) }
    )
}