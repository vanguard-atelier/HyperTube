package com.github.libretube.hyperui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * HyperOS Progressive Linear Alpha Gradient Blur simulation.
 * Blends content smoothly under top and bottom system bars without harsh lines.
 */
@Composable
fun HyperProgressiveBlurOverlay(
    isTop: Boolean,
    height: Dp = 56.dp,
    baseColor: Color = Color(0xFF0A0A0C),
    modifier: Modifier = Modifier
) {
    val brush = if (isTop) {
        Brush.verticalGradient(
            colors = listOf(
                baseColor.copy(alpha = 0.95f),
                baseColor.copy(alpha = 0.70f),
                baseColor.copy(alpha = 0.30f),
                Color.Transparent
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color.Transparent,
                baseColor.copy(alpha = 0.30f),
                baseColor.copy(alpha = 0.70f),
                baseColor.copy(alpha = 0.95f)
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(brush)
    )
}
