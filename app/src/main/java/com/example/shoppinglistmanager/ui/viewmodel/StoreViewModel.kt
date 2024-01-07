package com.example.shoppinglistmanager.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistmanager.data.entity.Store
import com.example.shoppinglistmanager.data.repository.StoreRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoreViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val storeRepository: StoreRepository
    private var firebaseDatabase: FirebaseDatabase
    private var firebaseUser: FirebaseUser
    val stores: StateFlow<HashMap<String, Store>>

    init {
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        storeRepository = StoreRepository(firebaseDatabase, firebaseUser)
        stores = storeRepository.allStoresMutable
    }

    fun insertStore(store: Store) {
        viewModelScope.launch {
            storeRepository.insert(store)
        }
    }

    fun updateStore(store: Store) {
        viewModelScope.launch {
            storeRepository.update(store)
        }
    }

    fun deleteStore(store: Store) {
        viewModelScope.launch {
            storeRepository.delete(store)
        }
    }

}