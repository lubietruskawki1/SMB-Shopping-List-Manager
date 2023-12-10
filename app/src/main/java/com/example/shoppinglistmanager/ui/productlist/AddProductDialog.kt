package com.example.shoppinglistmanager.ui.productlist

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.shoppinglistmanager.ui.utils.ProductManager
import com.example.shoppinglistmanager.ui.utils.ProductStates
import com.example.shoppinglistmanager.ui.utils.createProductStates
import com.example.shoppinglistmanager.ui.viewmodel.ProductViewModel

@Composable
fun AddProductDialog(
    productViewModel: ProductViewModel,
    openAddProductDialogState: MutableState<Boolean>
) {
    if (!openAddProductDialogState.value) {
        return
    }

    val context: Context = LocalContext.current
    val productStates: ProductStates = createProductStates()

    AlertDialog(
        onDismissRequest = { openAddProductDialogState.value = false },
        title = {
            Text(
                text = "Add product",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    val productManager = ProductManager(productStates)

                    if (productManager.isValidProduct(context)) {
                        val newProduct = productManager.createProduct()
                        productViewModel.insertProduct(newProduct)

                        productStates.reset()

                        openAddProductDialogState.value = false
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { openAddProductDialogState.value = false },
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            ) {
                Text("Cancel")
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                with (productStates) {
                    NameTextField(name)
                    NumberTextField(price, "Price per unit")
                    NumberTextField(quantity, "Quantity")
                    PurchasedCheckbox(purchased)
                }
            }
        }
    )
}

@Composable
private fun NameTextField(nameState: MutableState<String>) {
    TextField(
        value = nameState.value,
        onValueChange = {
            nameState.value = it
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        placeholder = {
            Text(
                text = "Name",
                color = Color.Gray
            )
        }
    )
}

@Composable
private fun NumberTextField(
    state: MutableState<String>,
    placeholderText: String
) {
    TextField(
        value = state.value,
        onValueChange = {
            state.value = it
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        placeholder = {
            Text(
                text = placeholderText,
                color = Color.Gray
            )
        }
    )
}

@Composable
private fun PurchasedCheckbox(purchasedState: MutableState<Boolean>) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Already purchased: ")
        Checkbox(
            checked = purchasedState.value,
            onCheckedChange = {
                purchasedState.value = it
            },
            colors = CheckboxDefaults.colors(MaterialTheme.colorScheme.secondary)
        )
    }
}