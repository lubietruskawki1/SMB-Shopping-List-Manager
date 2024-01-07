package com.example.shoppinglistmanager.ui.storelist

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.shoppinglistmanager.data.entity.Store
import com.example.shoppinglistmanager.ui.common.NumberTextField
import com.example.shoppinglistmanager.ui.common.StringTextField
import com.example.shoppinglistmanager.ui.utils.store.StoreManager
import com.example.shoppinglistmanager.ui.utils.store.StoreStates
import com.example.shoppinglistmanager.ui.utils.store.createStoreStates
import com.example.shoppinglistmanager.ui.utils.showToast
import com.example.shoppinglistmanager.ui.viewmodel.StoreViewModel
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
@Composable
fun AddStoreDialog(
    storeViewModel: StoreViewModel,
    openAddStoreDialogState: MutableState<Boolean>
) {
    if (!openAddStoreDialogState.value) {
        return
    }

    val context: Context = LocalContext.current
    val storeStates: StoreStates = createStoreStates()

    AlertDialog(
        onDismissRequest = { openAddStoreDialogState.value = false },
        title = {
            Text(
                text = "Add store",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    LocationServices.getFusedLocationProviderClient(context)
                        .lastLocation
                            .addOnSuccessListener {
                                val location: Location = it
                                val storeManager = StoreManager(storeStates)

                                if (storeManager.isValidStore(context)) {
                                    val newStore: Store =
                                        storeManager.createStore(location)
                                    storeViewModel.insertStore(newStore)

                                    storeStates.reset()
                                    openAddStoreDialogState.value = false
                                }
                            }
                            .addOnFailureListener {
                                // showToast(context, "No location information available.")
                                showToast(context,  it.message.toString())
                            }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { openAddStoreDialogState.value = false },
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            ) {
                Text("Cancel")
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                with (storeStates) {
                    StringTextField(name, "Name")
                    StringTextField(description, "Description")
                    NumberTextField(radius, "Radius")
                }
            }
        }
    )
}