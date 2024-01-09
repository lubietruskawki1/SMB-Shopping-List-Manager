package com.example.shoppinglistmanager.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistmanager.data.entity.Store
import com.example.shoppinglistmanager.data.repository.StoreRepository
import com.example.shoppinglistmanager.receiver.GeofenceReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StoreViewModel(
    application: Application
) : AndroidViewModel(application) {

    private var firebaseDatabase: FirebaseDatabase
    private var firebaseUser: FirebaseUser

    private val storeRepository: StoreRepository
    val stores: StateFlow<HashMap<String, Store>>

    private var geofencingClient: GeofencingClient
    private var storesGeofenceIds: MutableMap<String, Int>

    companion object {
        private var geofenceId: Int = 0
    }

    init {
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        storeRepository = StoreRepository(firebaseDatabase, firebaseUser)
        stores = storeRepository.allStoresMutable

        geofencingClient = LocationServices.getGeofencingClient(application)
        storesGeofenceIds = mutableMapOf()
    }

    fun insertStore(store: Store) {
        viewModelScope.launch {
            val storeId: String = storeRepository.insert(store)
            addGeofence(store, storeId)
        }
    }

    @SuppressLint("MissingPermission")
    private fun addGeofence(store: Store, storeId: String) {
        val context: Context = getApplication<Application>().applicationContext

        val requestId: String = getRequestId(geofenceId)
        val geofence: Geofence = Geofence.Builder()
            .setCircularRegion(
                store.latitude,
                store.longitude,
                store.radius
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setRequestId(requestId)
            .setTransitionTypes(
                Geofence.GEOFENCE_TRANSITION_ENTER or
                        Geofence.GEOFENCE_TRANSITION_EXIT
            )
            .build()

        val request: GeofencingRequest = GeofencingRequest.Builder()
            .addGeofence(geofence)
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .build()

        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context,
            geofenceId,
            Intent(context, GeofenceReceiver::class.java).also {
                it.putExtra("storeName", store.name)
            },
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        geofencingClient.addGeofences(request, pendingIntent)
            .addOnSuccessListener {
                storesGeofenceIds[storeId] = geofenceId
                Log.i("Geofence",
                    "Added geofence with ID: ${geofenceId++}")
            }
            .addOnFailureListener { exception ->
                Log.e("GeofenceError", exception.message.toString())
            }
    }

    fun updateStore(store: Store) {
        viewModelScope.launch {
            storeRepository.update(store)
            removeGeofence(store)
            addGeofence(store, store.id)
        }
    }

    fun deleteStore(store: Store) {
        viewModelScope.launch {
            storeRepository.delete(store)
            removeGeofence(store)
        }
    }

    @SuppressLint("MissingPermission")
    private fun removeGeofence(store: Store) {
        val storeGeofenceId: Int? = storesGeofenceIds[store.id]

        if (storeGeofenceId != null) {
            val requestId: String = getRequestId(storeGeofenceId)
            geofencingClient.removeGeofences(listOf(requestId))
                .addOnSuccessListener {
                    storesGeofenceIds.remove(store.id)
                    Log.i(
                        "Geofence",
                        "Removed geofence with ID: $storeGeofenceId"
                    )
                }
                .addOnFailureListener { exception ->
                    Log.e("GeofenceError", exception.message.toString())
                }
        }
    }

    private fun getRequestId(storeGeofenceId: Int): String {
        return "GeoId:${storeGeofenceId}"
    }

}