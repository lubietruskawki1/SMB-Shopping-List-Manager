package com.example.multimediawidgetmanager

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
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

    companion object {
        const val CHANGE_IMAGE_ACTION: String =
            "com.example.multimediawidgetmanager.CHANGE_IMAGE"
        val images: Array<Int> = arrayOf(
            R.drawable.kitty,
            R.drawable.duck,
            R.drawable.axolotl,
            R.drawable.frog,
            R.drawable.dinosaur
        )
        var imagesIndex: Int = 0
    }

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

        when (intent?.action) {
            CHANGE_IMAGE_ACTION -> {
                imagesIndex = (imagesIndex + 1) % images.size
            }
        }

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(context!!, MultimediaWidget::class.java)
        )
        onUpdate(context, appWidgetManager, appWidgetIds)
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
    val openBrowserIntent = Intent(Intent.ACTION_VIEW)
    openBrowserIntent.data = Uri.parse("https://www.pja.edu.pl")
    val openBrowserPendingIntent: PendingIntent = PendingIntent.getActivity(
        context,
        requestCode,
        openBrowserIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    views.setOnClickPendingIntent(
        R.id.open_browser_button,
        openBrowserPendingIntent
    )

    // Change image
    val changeImageIntent = Intent(context, MultimediaWidget::class.java)
    changeImageIntent.action = MultimediaWidget.CHANGE_IMAGE_ACTION
    val changeImagePendingIntent: PendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        changeImageIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    views.setOnClickPendingIntent(
        R.id.change_image_button,
        changeImagePendingIntent
    )

    // Set the current image in the ImageView
    views.setImageViewResource(
        R.id.image_view,
        MultimediaWidget.images[MultimediaWidget.imagesIndex]
    )

    appWidgetManager.updateAppWidget(appWidgetId, views)
}