package com.robertcoding.paginationnews.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// A CompositionLocal to pass the colors down the tree implicitly
val LocalNYTColors = staticCompositionLocalOf { LightColorScheme }

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFE8E8E8),           // Light text on dark backgrounds
    secondary = Color(0xFFB0B0B0),         // Medium gray for secondary text
    tertiary = Color(0xFF656565),          // Muted dark accent
    onSurfaceVariant = Color(0xFF9E9E9E), // Light gray for variant text
    surfaceContainer = Color(0xFFEE5A52), // Keep the editorial red
    background = Color(0xFF0A0A0A),       // Pure dark background
    surface = Color(0xFF121212),          // Dark surface
    surfaceTint = Color(0xFF1E1E1E),      // Slightly lighter dark tint
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0D0D0D),           // Slightly darker than dark primary
    secondary = Color(0xFF555555),        // Medium-dark gray for contrast
    tertiary = Color(0xFFD9D9D9),         // Light gray for light backgrounds
    onSurfaceVariant = Color(0xFF666666), // Readable on light surfaces
    surfaceContainer = Color(0xFFEE5A52), // Keep the editorial red accent
    background = Color(0xFFFAF9F7),       // Keep your light beige
    surface = Color(0xFFFFFFFF),          // White
    surfaceTint = Color(0xFFF5F5F5),      // Very light gray tint
)


@Composable
fun PaginationNewsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // We provide these colors down the Compose tree
    CompositionLocalProvider(LocalNYTColors provides colorScheme) {
        MaterialTheme(colorScheme = colorScheme, content = content)
    }
}


object PaginationNewsTheme {
    val colors
        @Composable
        get() = LocalNYTColors.current
}