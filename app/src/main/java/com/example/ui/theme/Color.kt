package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Hollam High-Contrast Dark Canvas
val HollamBlack = Color(0xFF07070A)
val HollamDarkSurface = Color(0xFF0F0F16)
val HollamDarkElevated = Color(0xFF181824)
val HollamCard = Color(0xFF1F1F2E)
val HollamBorder = Color(0xFF2A2A3D)

// Hollam Vibrant Neon & Spectral Accents
val HollamNeonLime = Color(0xFF00FF87)      // فسفوري عصري
val HollamElectricViolet = Color(0xFFA855F7) // بنفسجي كهربائي
val HollamDeepViolet = Color(0xFF7928CA)
val HollamCyberCyan = Color(0xFF00F0FF)
val HollamHotPink = Color(0xFFFF007A)
val HollamAmber = Color(0xFFFFB703)
val HollamTextPrimary = Color(0xFFF8F9FA)
val HollamTextSecondary = Color(0xFFA0A0B2)
val HollamTextMuted = Color(0xFF636378)

// Spectral ring gradient colors
val HollamSpectralColors = listOf(
    HollamNeonLime,
    HollamCyberCyan,
    HollamElectricViolet,
    HollamHotPink,
    HollamNeonLime
)

val HollamGradientSpectral = Brush.linearGradient(
    colors = listOf(HollamNeonLime, HollamCyberCyan, HollamElectricViolet)
)

val HollamGradientVioletLime = Brush.horizontalGradient(
    colors = listOf(HollamElectricViolet, HollamNeonLime)
)

// Iconic Snapchat Yellow for Hollam Auth Screen
val HollamSnapYellow = Color(0xFFFFFC00)
val HollamSnapYellowDark = Color(0xFFE6E300)


