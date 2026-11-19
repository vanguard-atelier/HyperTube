package com.github.libretube.hyperui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.libretube.hyperui.theme.HyperColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Authentic HyperOS Orbiting Satellite Dot Progress Indicator.
 * Exactly replicated from compose-miuix-ui InfiniteProgressIndicator.
 */
@Composable
fun HyperInfiniteProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = HyperColors.HyperBlue,
    size: Dp = 32.dp,
    trackWidth: Dp = 2.5.dp,
    satelliteRadius: Dp = 2.5.dp
) {
    val transition = rememberInfiniteTransition(label = "HyperProgress")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation"
    )

    Spacer(
        modifier = modifier
            .size(size)
            .drawWithCache {
                val trackWidthPx = trackWidth.toPx()
                val dotRadiusPx = satelliteRadius.toPx()
                val radius = (this.size.minDimension - (dotRadiusPx * 2)) / 2f
                val center = Offset(this.size.width / 2f, this.size.height / 2f)
                val trackStroke = Stroke(width = trackWidthPx, cap = StrokeCap.Round)

                onDrawBehind {
                    // Muted background track ring
                    drawCircle(
                        color = color.copy(alpha = 0.2f),
                        radius = radius,
                        center = center,
                        style = trackStroke
                    )

                    // Orbiting satellite dot
                    val angleRad = (rotation * PI / 180f).toFloat()
                    val dotX = center.x + radius * cos(angleRad)
                    val dotY = center.y + radius * sin(angleRad)

                    drawCircle(
                        color = color,
                        radius = dotRadiusPx,
                        center = Offset(dotX, dotY)
                    )
                }
            }
    )
}
