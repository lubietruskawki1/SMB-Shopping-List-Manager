package com.example.shoppinglistmanager.ui.productlist

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.shoppinglistmanager.data.entity.Product
import com.example.shoppinglistmanager.ui.common.EditNumberTextField
import com.example.shoppinglistmanager.ui.common.EditTitleStringTextField
import com.example.shoppinglistmanager.ui.utils.product.ProductManager
import com.example.shoppinglistmanager.ui.utils.product.ProductStates
import com.example.shoppinglistmanager.ui.utils.product.createProductStates
import com.example.shoppinglistmanager.ui.viewmodel.ProductViewModel
import java.math.BigDecimal

@Composable
fun ProductItemCard(
    productViewModel: ProductViewModel,
    product: Product,
    isShared: Boolean
) {
    val context: Context = LocalContext.current
    val editProductEnabledState: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    val productStates: ProductStates = createProductStates(product)

    // Checking if user clicked the notification about the addition of this
    // product to the shopping list
    val intent = (context as Activity).intent
    val editProductId = intent.getStringExtra("editProductId")
    if (product.id == editProductId) {
        editProductEnabledState.value = true
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        border = when {
            isShared -> BorderStroke (1.dp, MaterialTheme.colorScheme.onSurface)
            else -> null
        },
        colors = when {
            !isShared -> CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
            else -> CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        }
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
                    if (!isShared) {
                        productViewModel.updateProduct(product)
                    } else {
                        productViewModel.updateSharedProduct(product)
                    }
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
                        fontWeight = FontWeight.Bold
                    )
                    val totalPrice: BigDecimal =
                        BigDecimal(product.price) * BigDecimal(product.quantity)
                    Text(
                        text = "Total price: $${totalPrice}",
                        fontSize = priceAndQuantityFontSize
                    )
                    Text(
                        text = "Quantity: ${product.quantity}",
                        fontSize = priceAndQuantityFontSize
                    )
                } else {
                    EditTitleStringTextField(productStates.name)
                    EditNumberTextField(
                        state = productStates.price,
                        fieldName = "Price per unit",
                        fontSize = priceAndQuantityFontSize
                    )
                    EditNumberTextField(
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

                            if (!isShared) {
                                productViewModel.updateProduct(product)
                            } else {
                                productViewModel.updateSharedProduct(product)
                            }

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
                    if (!isShared) {
                        productViewModel.deleteProduct(product)
                    } else {
                        productViewModel.deleteSharedProduct(product)
                    }
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