package com.consica.code.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    secondary = RiverBlue,
    tertiary = SunYellow,
    background = LeafLight,
    surface = CleanWhite,
    onPrimary = CleanWhite,
    onSecondary = CleanWhite,
    onTertiary = SoilDark,
    onBackground = SoilDark,
    onSurface = SoilDark,
)

// We primarily use a light nature theme, but provide a dark theme fallback if needed
private val DarkColorScheme = darkColorScheme(
    primary = ForestGreen,
    secondary = RiverBlue,
    tertiary = SunYellow,
    background = DarkEditorBackground,
    surface = DarkEditorSurface,
    onPrimary = CleanWhite,
    onSecondary = CleanWhite,
    onTertiary = SoilDark,
    onBackground = CleanWhite,
    onSurface = CleanWhite,
)

@Composable
fun ConsicaCodeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        // typography = Typography, // Will add later
        content = content
    )
}
