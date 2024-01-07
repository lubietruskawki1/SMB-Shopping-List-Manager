package com.example.shoppinglistmanager.data.repository

import android.util.Log
import com.example.shoppinglistmanager.data.entity.Store
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow

class StoreRepository(
    private val firebaseDatabase: FirebaseDatabase,
    firebaseUser: FirebaseUser
) {

    val allStoresMutable = MutableStateFlow(HashMap<String, Store>())

    private val path: String = "users/${firebaseUser.uid}/stores"

    init {
        firebaseDatabase.getReference(path)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot,
                                          previousChildName: String?) {
                    val store: Store = createStoreFromSnapshot(snapshot)
                    allStoresMutable.value = allStoresMutable.value
                        .toMutableMap()
                        .apply {
                            put(store.id, store)
                        } as HashMap<String, Store>
                }

                override fun onChildChanged(snapshot: DataSnapshot,
                                            previousChildName: String?) {
                    val store: Store = createStoreFromSnapshot(snapshot)
                    allStoresMutable.value = allStoresMutable.value
                        .toMutableMap()
                        .apply {
                            put(store.id, store)
                        } as HashMap<String, Store>
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val store: Store = createStoreFromSnapshot(snapshot)
                    allStoresMutable.value = allStoresMutable.value
                        .toMutableMap()
                        .apply {
                            remove(store.id, store)
                        } as HashMap<String, Store>
                }

                override fun onChildMoved(snapshot: DataSnapshot,
                                          previousChildName: String?) {
                    val store: Store = createStoreFromSnapshot(snapshot)
                    allStoresMutable.value = allStoresMutable.value
                        .toMutableMap()
                        .apply {
                            remove(store.id, store)
                            put(store.id, store)
                        } as HashMap<String, Store>
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("StoreRepository",
                        "Database operation cancelled: ${error.message}")
                }
            })
    }

    private fun createStoreFromSnapshot(snapshot: DataSnapshot): Store {
        return Store(
            id = snapshot.ref.key as String,
            name = snapshot.child("name").value as String,
            description = snapshot.child("description").value as String,
            radius = (snapshot.child("radius").value as Number).toDouble(),
            latitude = (snapshot.child("latitude").value as Number).toDouble(),
            longitude = (snapshot.child("longitude").value as Number).toDouble()
        )
    }

    fun insert(store: Store): String {
        firebaseDatabase.getReference(path).push().also {
            store.id = it.ref.key!!
            it.setValue(store)
            return store.id
        }
    }

    fun update(store: Store) {
        val ref = firebaseDatabase.getReference("$path/${store.id}")
        ref.child("name").setValue(store.name)
        ref.child("description").setValue(store.description)
        ref.child("radius").setValue(store.radius)
    }

    fun delete(store: Store) {
        firebaseDatabase.getReference("$path/${store.id}").removeValue()
    }

}