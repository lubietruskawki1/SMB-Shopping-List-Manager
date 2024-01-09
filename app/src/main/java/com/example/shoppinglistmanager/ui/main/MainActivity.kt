package com.example.shoppinglistmanager.ui.main

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.example.shoppinglistmanager.R
import com.example.shoppinglistmanager.receiver.GeofenceReceiver
import com.example.shoppinglistmanager.ui.common.TopAppBar
import com.example.shoppinglistmanager.ui.map.MapActivity
import com.example.shoppinglistmanager.ui.options.OptionsActivity
import com.example.shoppinglistmanager.ui.productlist.ProductListActivity
import com.example.shoppinglistmanager.ui.storelist.StoreListActivity
import com.example.shoppinglistmanager.ui.theme.ShoppingListManagerTheme
import com.example.shoppinglistmanager.ui.utils.checkBackgroundLocationPermission
import com.example.shoppinglistmanager.ui.viewmodel.OptionsViewModel

private const val ACCESS_BACKGROUND_LOCATION_REQUEST_CODE = 111

class MainActivity : ComponentActivity() {

    private lateinit var receiver: GeofenceReceiver

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val optionsViewModel = OptionsViewModel(application)
            ShoppingListManagerTheme(optionsViewModel) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
        setupBackgroundLocationPermission()
        createChannel()
        receiver = GeofenceReceiver()
    }

    private fun setupBackgroundLocationPermission() {
        if (!checkBackgroundLocationPermission(this)) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                ACCESS_BACKGROUND_LOCATION_REQUEST_CODE
            )
        }
    }

    private fun createChannel() {
        val channel = NotificationChannel(
            getString(R.string.geofence_channel),
            "Channel for geofence notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        NotificationManagerCompat.from(applicationContext)
            .createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStart() {
        super.onStart()
        registerReceiver(receiver, IntentFilter(), RECEIVER_EXPORTED)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}

@Composable
private fun HomeScreen() {
    val items = listOf(
        HomeItem(
            name = "Shopping List",
            intent = { context ->
                Intent(context, ProductListActivity::class.java)
            },
            icon = painterResource(R.drawable.shopping_list)
        ),
        HomeItem(
            name = "Map",
            intent = { context ->
                Intent(context, MapActivity::class.java)
            },
            icon = painterResource(R.drawable.map)
        ),
        HomeItem(
            name = "Stores",
            intent = { context ->
                Intent(context, StoreListActivity::class.java)
            },
            icon = painterResource(R.drawable.store)
        ),
        HomeItem(
            name = "Options",
            intent = { context ->
                Intent(context, OptionsActivity::class.java)
            },
            icon = painterResource(R.drawable.settings)
        )
    )

    Scaffold(
        topBar = { TopAppBar("Home") }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            HomeList(items)
        }
    }
}

@Composable
private fun HomeList(homeItems: List<HomeItem>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(homeItems) { menuItem ->
            MenuItemCard(menuItem)
        }
    }
}