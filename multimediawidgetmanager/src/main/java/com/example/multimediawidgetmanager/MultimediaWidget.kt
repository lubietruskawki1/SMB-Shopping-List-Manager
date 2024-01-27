package com.example.multimediawidgetmanager

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class MultimediaWidget : AppWidgetProvider() {

    private var requestCode: Int = 0

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager,
                            appWidgetId, requestCode++)
        }
    }

    override fun onEnabled(context: Context) {
        Log.i("MultimediaWidgetManager", "First widget added.")
    }

    override fun onDisabled(context: Context) {
        Log.i("MultimediaWidgetManager", "Last widget removed.")
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    requestCode: Int
) {
    val views = RemoteViews(context.packageName, R.layout.multimedia_widget)

    // Open browser
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("https://www.pja.edu.pl")
    val pendingIntent: PendingIntent = PendingIntent.getActivity(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    views.setOnClickPendingIntent(R.id.open_browser_button, pendingIntent)

    appWidgetManager.updateAppWidget(appWidgetId, views)
}