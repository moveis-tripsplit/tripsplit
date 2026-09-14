package com.imd.tripsplit.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ==========================================
// 🎨 TripSplit Design System - Tema Principal
// ==========================================

private val LightColorScheme = lightColorScheme(
    primary = BlueOcean,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE), // Azul suave
    onPrimaryContainer = BlueOcean,

    secondary = GreenTravel,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDCFCE7), // Verde suave
    onSecondaryContainer = GreenTravel,

    tertiary = OrangeAccent,
    onTertiary = Color.White,

    background = BackgroundLight,
    onBackground = TextPrimary,

    surface = SurfaceLight,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundLight,
    onSurfaceVariant = TextSecondary,

    outline = BorderLight,
    outlineVariant = TextDisabled,

    error = ErrorRed,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1E3A8A),
    onPrimaryContainer = Color(0xFFBFDBFE),

    secondary = SuccessDark,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF14532D),
    onSecondaryContainer = Color(0xFFBBF7D0),

    tertiary = OrangeAccent,
    onTertiary = Color.White,

    background = BackgroundDark,
    onBackground = TextPrimaryDark,

    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceDark,
    onSurfaceVariant = TextSecondaryDark,

    outline = BorderDark,
    outlineVariant = TextSecondaryDark,

    error = ErrorDark,
    onError = Color.White
)

@Composable
fun TripSplitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = TripSplitShapes,
        typography = TripSplitTypography,
        content = content
    )
}
