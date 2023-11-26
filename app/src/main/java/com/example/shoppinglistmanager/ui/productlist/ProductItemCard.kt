package com.example.shoppinglistmanager.ui.productlist

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.shoppinglistmanager.data.entity.Product
import com.example.shoppinglistmanager.ui.utils.ProductManager
import com.example.shoppinglistmanager.ui.utils.ProductStates
import com.example.shoppinglistmanager.ui.utils.createProductStates
import com.example.shoppinglistmanager.ui.viewmodel.ProductViewModel
import java.math.BigDecimal

@Composable
fun ProductItemCard(
    productViewModel: ProductViewModel,
    product: Product
) {
    val context: Context = LocalContext.current
    val editProductEnabledState: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val productStates: ProductStates = createProductStates(product)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            var productPurchasedValue: Boolean by remember {
                mutableStateOf(product.purchased)
            }
            val iconColor: Color = MaterialTheme.colorScheme.secondary
            Checkbox(
                checked = productPurchasedValue,
                onCheckedChange = {
                    productPurchasedValue = it

                    product.purchased = it
                    productViewModel.updateProduct(product)
                },
                modifier = Modifier.padding(2.dp),
                colors = CheckboxDefaults.colors(iconColor)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val priceAndQuantityFontSize: TextUnit =
                    MaterialTheme.typography.bodyLarge.fontSize * 0.8

                if (!editProductEnabledState.value) {
                    Text(
                        text = product.name,
                        fontWeight = FontWeight.Bold,
                    )
                    val totalPrice: BigDecimal =
                        product.price * BigDecimal(product.quantity)
                    Text(
                        text = "Total price: $${totalPrice}",
                        fontSize = priceAndQuantityFontSize
                    )
                    Text(
                        text = "Quantity: ${product.quantity}",
                        fontSize = priceAndQuantityFontSize
                    )
                } else {
                    NameTextField(productStates.name)
                    NumberTextField(
                        state = productStates.price,
                        fieldName = "Price per unit",
                        fontSize = priceAndQuantityFontSize
                    )
                    NumberTextField(
                        state = productStates.quantity,
                        fieldName = "Quantity",
                        fontSize = priceAndQuantityFontSize
                    )
                }
            }
            Row(
                modifier = Modifier.padding(2.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                IconButton(onClick = {
                    if (editProductEnabledState.value) {
                        val productManager = ProductManager(productStates)

                        if (productManager.isValidProduct(context)) {
                            productManager.updateProduct(product)
                            productViewModel.updateProduct(product)

                            productStates.reset(product)

                            editProductEnabledState.value = false
                        }
                    } else {
                        editProductEnabledState.value = true
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = iconColor
                    )
                }
                IconButton(onClick = {
                    productViewModel.deleteProduct(product)
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

@Composable
private fun NameTextField(nameState: MutableState<String>) {
    TextField(
        value = nameState.value,
        textStyle = TextStyle(
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = FontWeight.Bold
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        onValueChange = {
            nameState.value = it
        }
    )
}

@Composable
private fun NumberTextField(
    state: MutableState<String>,
    fieldName: String,
    fontSize: TextUnit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$fieldName: ",
            fontSize = fontSize
        )
        TextField(
            value = state.value,
            textStyle = TextStyle(fontSize = fontSize),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            onValueChange = {
                state.value = it
            }
        )
    }
}