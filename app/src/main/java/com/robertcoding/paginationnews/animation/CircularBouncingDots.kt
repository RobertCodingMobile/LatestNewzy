package com.robertcoding.paginationnews.animation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircularBouncingDots() {
    val dotCount = 6
    val radius = 80f          // orbit radius in dp
    val dotRadius = 10f       // dot size in dp

    // ── 1. Continuous rotation of the whole ring ──────────────────────────
    val infiniteTransition = rememberInfiniteTransition(label = "circle_anim")

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // ── 2. Pulse: dots travel inward and back ─────────────────────────────
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 2500
                0f at 0 using FastOutSlowInEasing   // at orbit
                1f at 1000 using FastOutSlowInEasing   // squish to center
                0f at 2000 using FastOutSlowInEasing   // bounce back out
                0f at 2500 using LinearEasing          // brief rest
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse"
    )

    Canvas(
        modifier = Modifier
            .size((radius * 2 + dotRadius * 2 + 16).dp)
    ) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val orbitPx = radius.dp.toPx()
        val dotPx = dotRadius.dp.toPx()

        repeat(dotCount) { index ->
            // Base angle: evenly spaced around the circle
            val baseAngleDeg = (360f / dotCount) * index
            // Add rotation offset so the whole ring spins
            val angleDeg = baseAngleDeg + rotationAngle
            val angleRad = Math.toRadians(angleDeg.toDouble())

            // Current orbit radius shrinks toward 0 during pulse
            val currentOrbit = orbitPx * (1f - pulseRadius)

            val x = centerX + (currentOrbit * cos(angleRad)).toFloat()
            val y = centerY + (currentOrbit * sin(angleRad)).toFloat()

            // Dots grow slightly as they converge inward
            val dotScale = 1f + (pulseRadius * 0.4f)

            // Color shifts from vivid to white at center
            val color = lerpColor(
                start = dotColors[index % dotColors.size],
                stop = Color.Magenta,
                fraction = pulseRadius
            )

            drawCircle(
                color = color,
                radius = dotPx * dotScale,
                center = Offset(x, y)
            )
        }
    }
}

// ── Color palette for each dot ────────────────────────────────────────────
val dotColors = listOf(
    Color(0xFF4FC3F7),  // sky blue
    Color(0xFF81C784),  // green
    Color(0xFFFFB74D),  // orange
    Color(0xFFF06292),  // pink
    Color(0xFFBA68C8),  // purple
    Color(0xFF4DD0E1),  // cyan
)

// ── Linear interpolation between two colors ───────────────────────────────
fun lerpColor(start: Color, stop: Color, fraction: Float): Color {
    val f = fraction.coerceIn(0f, 1f)
    return Color(
        red = start.red + (stop.red - start.red) * f,
        green = start.green + (stop.green - start.green) * f,
        blue = start.blue + (stop.blue - start.blue) * f,
        alpha = 1f
    )
}