package com.example.notificationhandler.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val darkColorScheme: ColorScheme = darkColorScheme(
    primary = DarkPurple40,
    secondary = DarkPurple50,
    primaryContainer = DarkPurple40,
    onPrimaryContainer = Purple10,
    background = DarkPurple10,
    onBackground = Purple10,
    surface = DarkPurple10,
    onSurface = Purple10,
    surfaceVariant = DarkPurple30,
    onSurfaceVariant = Purple10
)

private val lightColorScheme: ColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = Purple50,
    onPrimaryContainer = White,
    primaryContainer = Purple40,
    background = Purple10,
    onBackground = Purple70,
    surface = Purple10,
    onSurface = Purple80,
    surfaceVariant = Purple30,
    onSurfaceVariant = Purple80
)

@Composable
fun ShoppingListManagerNotificationHandlerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}