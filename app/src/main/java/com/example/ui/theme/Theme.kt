package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val HollamDarkColorScheme = darkColorScheme(
    primary = HollamNeonLime,
    onPrimary = HollamBlack,
    primaryContainer = HollamDeepViolet,
    onPrimaryContainer = Color.White,
    secondary = HollamElectricViolet,
    onSecondary = Color.White,
    secondaryContainer = HollamDarkElevated,
    onSecondaryContainer = HollamNeonLime,
    tertiary = HollamCyberCyan,
    onTertiary = HollamBlack,
    background = HollamBlack,
    onBackground = HollamTextPrimary,
    surface = HollamDarkSurface,
    onSurface = HollamTextPrimary,
    surfaceVariant = HollamDarkElevated,
    onSurfaceVariant = HollamTextSecondary,
    outline = HollamBorder
)

@Composable
fun HollamTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = HollamDarkColorScheme,
        typography = Typography,
        content = content
    )
}

