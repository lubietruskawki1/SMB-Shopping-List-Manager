package com.example.shoppinglistmanager.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private const val LINE_HEIGHT_MULTIPLIER = 1.15

fun getPersonalizedTypography(fontSizePreferences: FontSizePreferences): Typography {
    val fontSizeExtra: Int = fontSizePreferences.fontSizeExtra
    return Typography(
        headlineSmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = (20 + fontSizeExtra).sp,
            lineHeight = ((20 + fontSizeExtra) * LINE_HEIGHT_MULTIPLIER).sp,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = (16 + fontSizeExtra).sp,
            lineHeight = ((16 + fontSizeExtra) * LINE_HEIGHT_MULTIPLIER).sp,
            letterSpacing = 0.5.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = (16 + fontSizeExtra).sp,
            lineHeight = ((16 + fontSizeExtra) * LINE_HEIGHT_MULTIPLIER).sp,
            letterSpacing = 0.5.sp
        ),
        titleLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = (22 + fontSizeExtra).sp,
            lineHeight = ((22 + fontSizeExtra) * LINE_HEIGHT_MULTIPLIER).sp,
            letterSpacing = 0.sp
        ),
        labelLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = (16 + fontSizeExtra).sp,
            lineHeight = ((16 + fontSizeExtra) * LINE_HEIGHT_MULTIPLIER).sp,
            letterSpacing = 0.5.sp
        )
    )
}