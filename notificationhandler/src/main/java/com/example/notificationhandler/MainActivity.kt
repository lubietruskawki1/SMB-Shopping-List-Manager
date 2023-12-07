package com.example.notificationhandler

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationhandler.ui.checkNotificationPermission
import com.example.notificationhandler.ui.theme.ShoppingListManagerNotificationHandlerTheme

class MainActivity : ComponentActivity() {

    private lateinit var receiver: NewProductReceiver
    private val postNotificationsRequestCode = 101

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListManagerNotificationHandlerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
        setupNotificationsPermission()
        createChannel()
        receiver = NewProductReceiver()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setupNotificationsPermission() {
        if (!checkNotificationPermission(this)) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                postNotificationsRequestCode
            )
        }
    }

    private fun createChannel() {
        val channel = NotificationChannel(
            getString(R.string.new_product_channel),
            "Channel for new product notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        NotificationManagerCompat.from(applicationContext)
            .createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStart() {
        super.onStart()
        val filter = IntentFilter("com.example.NEW_PRODUCT_ADDED")
        registerReceiver(receiver, filter, RECEIVER_EXPORTED)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}

@Composable
fun Greeting() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome!", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "This application is handling notifications " +
                   "for ShoppingListManager.",
            textAlign = TextAlign.Center
        )
    }
}