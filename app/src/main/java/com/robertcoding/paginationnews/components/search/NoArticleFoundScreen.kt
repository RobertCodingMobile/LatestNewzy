package com.robertcoding.paginationnews.components.search

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoArticlesFound(
    query: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // ── Glow ring + icon ─────────────────────────────────────────────────
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(
                        Color(0xFF7C6AF7).copy(alpha = glowAlpha * 0.15f)
                    )
            )
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1A1A24))
                    .border(1.dp, Color(0xFF7C6AF7).copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.SearchOff,
                    contentDescription = null,
                    tint = Color(0xFF7C6AF7),
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // ── Headline ─────────────────────────────────────────────────────────
        Text(
            text = "No results found",
            color = Color(0xFFF0EFF8),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(8.dp))

        // ── Sub-copy with query highlight ────────────────────────────────────
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = Color(0xFF8A8AA0), fontSize = 14.sp)) {
                    append("We couldn't find any articles for ")
                }
                withStyle(
                    SpanStyle(
                        color = Color(0xFF9B8DFF),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                ) {
                    append("\"$query\"")
                }
            },
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(Modifier.height(28.dp))

        // ── Suggestion pills ─────────────────────────────────────────────────
        Text(
            text = "Try searching for",
            color = Color(0xFF8A8AA0),
            fontSize = 12.sp,
            letterSpacing = 0.8.sp
        )

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Top stories", "Breaking news", "Latest").forEach { suggestion ->
                SuggestionPill(label = suggestion)
            }
        }
    }
}

@Composable
private fun SuggestionPill(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFF1A1A24))
            .border(1.dp, Color(0xFF2A2A3A), RoundedCornerShape(50))
            .clickable {}
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = Color(0xFF8A8AA0),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}