package com.github.libretube.hyperui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Official HyperOS / MIUIX Design System Color Palette
object HyperColors {
    // HyperOS Signature Electric Blue Accent
    val HyperBlue = Color(0xFF007AFE)
    val HyperBlueDark = Color(0xFF277AF7)
    val HyperBlueLight = Color(0xFF3482FF)

    // Dark Mode Palette (Pure Obsidian / Deep Space)
    val DarkBackground = Color(0xFF0A0A0C)
    val DarkSurface = Color(0xFF141419)
    val DarkCard = Color(0xFF1C1C24)
    val DarkCardBorder = Color(0x1FFFFFFF)
    val DarkTextPrimary = Color(0xFFFFFFFF)
    val DarkTextSecondary = Color(0xFF8E8E93)
    val DarkTextTertiary = Color(0xFF636366)
    val DarkGlassDock = Color(0xD916161D)

    // Light Mode Palette (Ceramic White / Clean Porcelain)
    val LightBackground = Color(0xFFF8F9FA)
    val LightSurface = Color(0xFFFFFFFF)
    val LightCard = Color(0xFFF2F2F7)
    val LightCardBorder = Color(0x14000000)
    val LightTextPrimary = Color(0xFF1A1A1E)
    val LightTextSecondary = Color(0xFF6C6C70)
    val LightTextTertiary = Color(0xFF8E8E93)
    val LightGlassDock = Color(0xD9FFFFFF)
}

private val DarkColorScheme = darkColorScheme(
    primary = HyperColors.HyperBlueDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF00387A),
    onPrimaryContainer = Color(0xFFD0E4FF),
    background = HyperColors.DarkBackground,
    onBackground = HyperColors.DarkTextPrimary,
    surface = HyperColors.DarkSurface,
    onSurface = HyperColors.DarkTextPrimary,
    surfaceVariant = HyperColors.DarkCard,
    onSurfaceVariant = HyperColors.DarkTextSecondary,
    outline = HyperColors.DarkCardBorder
)

private val LightColorScheme = lightColorScheme(
    primary = HyperColors.HyperBlueLight,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEAF2FF),
    onPrimaryContainer = HyperColors.HyperBlueLight,
    background = HyperColors.LightBackground,
    onBackground = HyperColors.LightTextPrimary,
    surface = HyperColors.LightSurface,
    onSurface = HyperColors.LightTextPrimary,
    surfaceVariant = HyperColors.LightCard,
    onSurfaceVariant = HyperColors.LightTextSecondary,
    outline = HyperColors.LightCardBorder
)

@Composable
fun HyperTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
