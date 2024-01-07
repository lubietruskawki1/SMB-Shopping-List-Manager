package com.example.shoppinglistmanager.ui.storelist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shoppinglistmanager.data.entity.Store
import com.example.shoppinglistmanager.ui.common.TopAppBar
import com.example.shoppinglistmanager.ui.theme.ShoppingListManagerTheme
import com.example.shoppinglistmanager.ui.viewmodel.OptionsViewModel
import com.example.shoppinglistmanager.ui.viewmodel.StoreViewModel

class StoreListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val optionsViewModel = OptionsViewModel(application)
            ShoppingListManagerTheme(optionsViewModel) {
                val storeViewModel = StoreViewModel(application)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StoreListScreen(storeViewModel)
                }
            }
        }
    }
}

@Composable
fun StoreListScreen(storeViewModel: StoreViewModel) {
    val openAddStoreDialogState: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = { TopAppBar("Favorite Stores List") },
        floatingActionButton = { AddStoreButton(openAddStoreDialogState) }
    ) { innerPadding ->
        StoreList(storeViewModel, innerPadding)
        AddStoreDialog(storeViewModel, openAddStoreDialogState)
    }
}

@Composable
private fun StoreList(
    storeViewModel: StoreViewModel,
    innerPadding: PaddingValues
) {
    val stores: Map<String, Store> by storeViewModel.stores
        .collectAsState(initial = emptyMap())

    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        items(stores.values.toList()) { store ->
            StoreItemCard(storeViewModel, store)
        }
    }
}

@Composable
private fun AddStoreButton(openDialog: MutableState<Boolean>) {
    FloatingActionButton(
        onClick = { openDialog.value = true },
        contentColor = contentColorFor(MaterialTheme.colorScheme.primary),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        content = { Icon(Icons.Default.Add, "Add store") }
    )
}