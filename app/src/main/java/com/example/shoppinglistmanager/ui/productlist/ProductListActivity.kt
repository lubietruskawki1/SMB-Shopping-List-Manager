package com.example.shoppinglistmanager.ui.productlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.shoppinglistmanager.data.entity.Product
import com.example.shoppinglistmanager.ui.common.TopAppBar
import com.example.shoppinglistmanager.ui.theme.ShoppingListManagerTheme
import com.example.shoppinglistmanager.ui.viewmodel.OptionsViewModel
import com.example.shoppinglistmanager.ui.viewmodel.ProductViewModel

private val DUMMY_LAZY_COLUMN_HEIGHT = 10000.dp

class ProductListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val optionsViewModel = OptionsViewModel(application)
            ShoppingListManagerTheme(optionsViewModel) {
                val productViewModel = ProductViewModel(application)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProductListScreen(productViewModel)
                }
            }
        }
    }
}

@Composable
fun ProductListScreen(productViewModel: ProductViewModel) {
    val openAddProductDialogState: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = { TopAppBar("Shopping List") },
        floatingActionButton = { AddProductButton(openAddProductDialogState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            item { ProductList(productViewModel) }
            item {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Shared products",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
            item { SharedProductList(productViewModel) }
        }
        AddProductDialog(productViewModel, openAddProductDialogState)
    }
}

@Composable
private fun ProductList(productViewModel: ProductViewModel) {
    val products: Map<String, Product> by productViewModel.products
        .collectAsState(initial = emptyMap())

    LazyColumn(modifier = Modifier.heightIn(max = DUMMY_LAZY_COLUMN_HEIGHT)) {
        items(products.values.toList()) { product ->
            ProductItemCard(
                productViewModel = productViewModel,
                product = product,
                isShared = false
            )
        }
    }
}

@Composable
private fun SharedProductList(productViewModel: ProductViewModel) {
    val products: Map<String, Product> by productViewModel.sharedProducts
        .collectAsState(initial = emptyMap())

    LazyColumn(modifier = Modifier.heightIn(max = DUMMY_LAZY_COLUMN_HEIGHT)) {
        items(products.values.toList()) { product ->
            ProductItemCard(
                productViewModel = productViewModel,
                product = product,
                isShared = true
            )
        }
        // Adding a spacer, so that the AddProductButton doesn't cover the
        // delete button of the last product on the list
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun AddProductButton(openDialog: MutableState<Boolean>) {
    FloatingActionButton(
        onClick = { openDialog.value = true },
        contentColor = contentColorFor(MaterialTheme.colorScheme.primary),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        content = { Icon(Icons.Default.Add, "Add product") }
    )
}