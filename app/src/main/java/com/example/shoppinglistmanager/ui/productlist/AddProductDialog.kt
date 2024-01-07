package com.example.shoppinglistmanager.ui.productlist

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.shoppinglistmanager.data.entity.Product
import com.example.shoppinglistmanager.ui.common.NumberTextField
import com.example.shoppinglistmanager.ui.common.StringTextField
import com.example.shoppinglistmanager.ui.utils.product.ProductManager
import com.example.shoppinglistmanager.ui.utils.product.ProductStates
import com.example.shoppinglistmanager.ui.utils.product.createProductStates
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
    val sharedState: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }

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
                        val newProduct: Product = productManager.createProduct()

                        if (!sharedState.value) {
                            productViewModel.insertProduct(newProduct)
                        } else {
                            productViewModel.insertSharedProduct(newProduct)
                        }

                        productStates.reset()
                        sharedState.value = false

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
                    StringTextField(name, "Name")
                    NumberTextField(price, "Price per unit")
                    NumberTextField(quantity, "Quantity")
                    PurchasedCheckbox(purchased)
                }
                SharedSwitch(sharedState)
            }
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

@Composable
private fun SharedSwitch(sharedState: MutableState<Boolean>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Shared: ")
        Switch(
            checked = sharedState.value,
            onCheckedChange = {
                sharedState.value = it
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.secondary,
                checkedBorderColor = MaterialTheme.colorScheme.secondary,
                uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                uncheckedBorderColor = MaterialTheme.colorScheme.secondary
            )
        )
    }
}