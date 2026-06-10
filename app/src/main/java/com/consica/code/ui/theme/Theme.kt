package com.consica.code.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    onPrimary = TextOnPrimary,
    primaryContainer = ForestGreenContainer,
    onPrimaryContainer = ForestGreenDark,
    secondary = RiverBlue,
    onSecondary = TextOnPrimary,
    secondaryContainer = RiverBlueLight.copy(alpha = 0.3f),
    onSecondaryContainer = RiverBlueDark,
    tertiary = MossGreen,
    onTertiary = TextOnPrimary,
    tertiaryContainer = ForestGreenContainer,
    onTertiaryContainer = ForestGreenDark,
    error = ErrorRed,
    onError = TextOnPrimary,
    errorContainer = ErrorRedSoft,
    onErrorContainer = ErrorRed,
    background = LeafLight,
    onBackground = SoilDark,
    surface = CleanWhite,
    onSurface = SoilDark,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = SoilDarkVariant,
    outline = StoneGray,
    outlineVariant = StoneGrayLight,
    inverseSurface = DarkSurface,
    inverseOnSurface = DarkText,
    inversePrimary = ForestGreenLight
)

private val HighContrastLightColorScheme = lightColorScheme(
    primary = ForestGreenDark,
    onPrimary = TextOnPrimary,
    primaryContainer = ForestGreen,
    onPrimaryContainer = TextOnPrimary,
    secondary = RiverBlueDark,
    onSecondary = TextOnPrimary,
    secondaryContainer = RiverBlue,
    onSecondaryContainer = TextOnPrimary,
    tertiary = ForestGreenDark,
    onTertiary = TextOnPrimary,
    tertiaryContainer = MossGreen,
    onTertiaryContainer = TextOnPrimary,
    error = ErrorRed,
    onError = TextOnPrimary,
    errorContainer = ErrorRedSoft,
    onErrorContainer = Color.Black,
    background = CleanWhite,
    onBackground = Color.Black,
    surface = CleanWhite,
    onSurface = Color.Black,
    surfaceVariant = CleanWhite,
    onSurfaceVariant = Color.Black,
    outline = Color.Black,
    outlineVariant = Color.Black,
    inverseSurface = Color.Black,
    inverseOnSurface = CleanWhite,
    inversePrimary = ForestGreen
)

private val DarkColorScheme = darkColorScheme(
    primary = ForestGreenLight,
    onPrimary = ForestGreenDark,
    primaryContainer = ForestGreen,
    onPrimaryContainer = ForestGreenContainer,
    secondary = RiverBlueLight,
    onSecondary = RiverBlueDark,
    secondaryContainer = RiverBlue,
    onSecondaryContainer = TextOnPrimary,
    tertiary = MossGreen,
    onTertiary = ForestGreenDark,
    tertiaryContainer = ForestGreen,
    onTertiaryContainer = ForestGreenContainer,
    error = ErrorRed,
    onError = TextOnPrimary,
    errorContainer = ErrorRed.copy(alpha = 0.3f),
    onErrorContainer = ErrorRed,
    background = DarkBackground,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = StoneGray,
    outlineVariant = StoneGrayLight,
    inverseSurface = CleanWhite,
    inverseOnSurface = SoilDark,
    inversePrimary = ForestGreen
)

@Composable
fun ConsicaCodeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    highContrast: Boolean = false,
    reducedMotion: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        highContrast -> HighContrastLightColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ConsicaTypography,
        shapes = ConsicaShapes,
        content = content
    )
}
