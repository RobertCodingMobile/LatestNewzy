package com.robertcoding.paginationnews.animation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun NewzyLoadingAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "bouncing_dots")
    val letters = "NEWZY"

    // Track visibility state for each letter
    val visibleLetters = remember { mutableStateListOf(*Array(letters.length) { false }) }

    // Trigger each letter with a staggered delay
    LaunchedEffect(Unit) {
        letters.forEachIndexed { index, _ ->
            delay(index * 100L)   // 100ms between each letter
            visibleLetters[index] = true
        }
    }

    val dotOffsets = List(3) { index ->
        val offsetY by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = -20f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 1200
                    0f at 0 using FastOutSlowInEasing   // start at rest
                    -20f at 300 using FastOutSlowInEasing   // bounce up
                    0f at 600 using FastOutSlowInEasing   // return to rest
                    0f at 1200 using LinearEasing          // hold at rest until next cycle
                },
                repeatMode = RepeatMode.Restart,
                initialStartOffset = StartOffset(index * 150)
            ),
            label = "dot_${index}_offset"
        )
        offsetY
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {

        letters.forEachIndexed { index, letter ->
            AnimatedLetter(
                letter = letter.toString(),
                visible = visibleLetters[index]
            )
        }

        dotOffsets.forEach { offsetY ->
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        translationY = offsetY.dp.toPx()
                    }
                    .padding(horizontal = 2.dp)
            ) {
                Text(
                    text = ".",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun AnimatedLetter(letter: String, visible: Boolean) {
    val transition = updateTransition(targetState = visible, label = "letter_transition")

    // Slide in from right → center
    val offsetX by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 500, easing = FastOutSlowInEasing)
        },
        label = "offsetX"
    ) { isVisible ->
        if (isVisible) 0f else 80f    // starts 80px to the right
    }

    // Fade in
    val alpha by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 200, easing = LinearEasing)
        },
        label = "alpha"
    ) { isVisible ->
        if (isVisible) 1f else 0f
    }

    Text(
        text = "$letter ",
        fontSize = 26.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.Red,               // Netflix-ish red
        modifier = Modifier
            .graphicsLayer {
                translationX = offsetX.dp.toPx()
            }
            .alpha(alpha)
    )
}

@Preview
@Composable
private fun NewzyLoadingAnimationPreview() {
    NewzyLoadingAnimation()
}