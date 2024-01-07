package com.example.shoppinglistmanager.ui.storelist

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.shoppinglistmanager.data.entity.Store
import com.example.shoppinglistmanager.ui.common.EditNumberTextField
import com.example.shoppinglistmanager.ui.common.EditStringTextField
import com.example.shoppinglistmanager.ui.common.EditTitleStringTextField
import com.example.shoppinglistmanager.ui.utils.store.StoreManager
import com.example.shoppinglistmanager.ui.utils.store.StoreStates
import com.example.shoppinglistmanager.ui.utils.store.createStoreStates
import com.example.shoppinglistmanager.ui.viewmodel.StoreViewModel

@Composable
fun StoreItemCard(
    storeViewModel: StoreViewModel,
    store: Store
) {
    val context: Context = LocalContext.current
    val editStoreEnabledState: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val storeStates: StoreStates = createStoreStates(store)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val iconColor: Color = MaterialTheme.colorScheme.secondary
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val descriptionAndRadiusFontSize: TextUnit =
                    MaterialTheme.typography.bodyLarge.fontSize * 0.8

                if (!editStoreEnabledState.value) {
                    Text(
                        text = store.name,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Description: ${store.description}",
                        fontSize = descriptionAndRadiusFontSize
                    )
                    Text(
                        text = "Radius: ${store.radius}",
                        fontSize = descriptionAndRadiusFontSize
                    )
                    Text(
                        text = "Location: ${store.latitude}, ${store.longitude}",
                        fontSize = descriptionAndRadiusFontSize
                    )
                } else {
                    EditTitleStringTextField(storeStates.name)
                    EditStringTextField(
                        state = storeStates.description,
                        fieldName = "Description",
                        fontSize = descriptionAndRadiusFontSize
                    )
                    EditNumberTextField(
                        state = storeStates.radius,
                        fieldName = "Radius",
                        fontSize = descriptionAndRadiusFontSize
                    )
                }
            }
            Row(
                modifier = Modifier.padding(2.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                IconButton(onClick = {
                    if (editStoreEnabledState.value) {
                        val storeManager = StoreManager(storeStates)

                        if (storeManager.isValidStore(context)) {
                            storeManager.updateStore(store)
                            storeViewModel.updateStore(store)

                            storeStates.reset(store)
                            editStoreEnabledState.value = false
                        }
                    } else {
                        editStoreEnabledState.value = true
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = iconColor
                    )
                }
                IconButton(onClick = {
                    storeViewModel.deleteStore(store)
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = iconColor
                    )
                }
            }
        }
    }
}