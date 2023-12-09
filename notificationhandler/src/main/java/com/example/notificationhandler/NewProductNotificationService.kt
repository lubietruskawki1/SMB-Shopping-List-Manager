package com.example.notificationhandler

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.example.notificationhandler.ui.checkNotificationPermission
import com.example.notificationhandler.ui.showNotificationPermissionNotGrantedToast

class NewProductNotificationService : Service() {

    companion object {
        private var index: Int = 0
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotification(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun createNotification(intent: Intent?) {
        if (intent == null) {
            return
        }

        val productId = intent.getStringExtra("productId")
        val productName = intent.getStringExtra("productName")
        val productPrice = intent.getStringExtra("productPrice")
        val productQuantity = intent.getIntExtra("productQuantity", -1)

        val activityIntent = Intent().also {
            it.component = ComponentName(
                "com.example.shoppinglistmanager",
                "com.example.shoppinglistmanager" +
                        ".ui.productlist.ProductListActivity"
            )
            // Putting received product ID to open edit panel for the right one
            it.putExtra("editProductId", productId)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            index, // Non-constant so that clicking the notification
                   // opens the edit panel of a correct product
            activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = Notification.Builder(
            this,
            getString(R.string.new_product_channel)
        )
            .setContentTitle("New Product Added")
            .setContentText("$productQuantity x $productName added to your " +
                            "shopping list with a price of $$productPrice " +
                            "per unit. Tap to edit it.")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setAutoCancel(true)
            .build()

        if (!checkNotificationPermission(this)) {
            showNotificationPermissionNotGrantedToast(this)
        } else {
            NotificationManagerCompat.from(this)
                .notify(index++, notification)
        }
    }
}