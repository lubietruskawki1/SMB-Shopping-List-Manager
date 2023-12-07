package com.example.notificationhandler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.notificationhandler.ui.checkNotificationPermission
import com.example.notificationhandler.ui.showNotificationPermissionNotGrantedToast

class NewProductReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.example.NEW_PRODUCT_ADDED") {
            if (!checkNotificationPermission(context)) {
                showNotificationPermissionNotGrantedToast(context)
            } else {
                intent.setClass(context, NewProductNotificationService::class.java)
                context.startService(intent)
            }
        }
    }
}