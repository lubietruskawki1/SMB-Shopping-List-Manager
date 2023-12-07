package com.example.notificationhandler.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun checkNotificationPermission(context: Context): Boolean {
    return ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED
}

fun showNotificationPermissionNotGrantedToast(context: Context) {
    Toast.makeText(
        context,
        "Notification permission not granted. Please enable it in settings.",
        Toast.LENGTH_SHORT
    ).show()
}
