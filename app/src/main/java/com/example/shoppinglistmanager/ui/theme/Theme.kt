package com.example.shoppinglistmanager.ui.theme

import android.app.Activity
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.shoppinglistmanager.ui.viewmodel.OptionsViewModel

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

class FontSizePreferences(
    val fontSizeExtra: Int
) {
    companion object {
        fun getFontSizeOptions(): List<String> {
            return FontSizeOption.values().map { it.key }
        }
    }

    enum class FontSizeOption(val key: String, val value: Int) {
        SMALL("Small", -2),
        MEDIUM("Medium", 0),
        LARGE("Large", 2),
        EXTRA_LARGE("Extra large", 4)
    }

    constructor(key: String) : this(
        when (key) {
            FontSizeOption.SMALL.key -> FontSizeOption.SMALL.value
            FontSizeOption.MEDIUM.key -> FontSizeOption.MEDIUM.value
            FontSizeOption.LARGE.key -> FontSizeOption.LARGE.value
            FontSizeOption.EXTRA_LARGE.key -> FontSizeOption.EXTRA_LARGE.value
            else -> FontSizeOption.MEDIUM.value // Medium is the default
        }
    )
}

@Composable
fun ShoppingListManagerTheme(
    optionsViewModel: OptionsViewModel,
    content: @Composable () -> Unit
) {
    val darkThemeEnabled: Boolean by optionsViewModel.darkThemeFlow
        .collectAsState(initial = false)
    val colorScheme = when {
        darkThemeEnabled -> darkColorScheme
        else -> lightColorScheme
    }

    val fontSize: String by optionsViewModel.fontSizeFlow
        .collectAsState(initial = "Medium")
    val fontSizePreferences = FontSizePreferences(fontSize)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = darkThemeEnabled
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getPersonalizedTypography(fontSizePreferences),
        content = content
    )
}