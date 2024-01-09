package com.example.shoppinglistmanager.receiver

import android.annotation.SuppressLint
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.example.shoppinglistmanager.R
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceReceiver : BroadcastReceiver() {

    companion object {
        private var index: Int = 0
    }

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        if (geofencingEvent == null) {
            Log.e("GeofenceError", "Geofencing event is null.")
            return
        }

        val storeName = intent.getStringExtra("storeName")

        for (geofence: Geofence in geofencingEvent.triggeringGeofences!!) {
            when (geofencingEvent.geofenceTransition) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                    sendNotification(
                        context = context,
                        contentTitle = "Welcome to $storeName!",
                        contentText = "Check out today's special " +
                                      "promotion just for you."
                    )
                }
                Geofence.GEOFENCE_TRANSITION_EXIT -> {
                    sendNotification(
                        context = context,
                        contentTitle = "Thank you for visiting $storeName!",
                        contentText = "See you next time. " +
                                      "Don't miss out on our future offers!"
                    )
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(
        context: Context,
        contentTitle: String,
        contentText: String
    ) {
        val notification: Notification = Notification.Builder(
            context,
            context.getString(R.string.geofence_channel)
        )
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(index++, notification)
    }
}