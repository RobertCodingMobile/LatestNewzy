package com.robertcoding.paginationnews.components.search

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.FlightTakeoff
import androidx.compose.material.icons.outlined.Money
import androidx.compose.material.icons.outlined.NorthWest
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SportsSoccer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robertcoding.paginationnews.animation.NYTColorScheme.Accent
import com.robertcoding.paginationnews.animation.NYTColorScheme.TextPrimary
import com.robertcoding.paginationnews.animation.NYTColorScheme.TextSecondary
import com.robertcoding.paginationnews.ui.theme.ChipIdle

// ─── Data models ──────────────────────────────────────────────────────────────

data class SearchChip(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val color: Color
)


// ─── Static data ──────────────────────────────────────────────────────────────

private val chips = listOf(
    SearchChip("Business", "Business", Icons.Outlined.Business, Color(0xFFE07B6A)),
    SearchChip("Travel", "Travel", Icons.Outlined.FlightTakeoff, Color(0xFF6ABDE0)),
    SearchChip("Tech", "Tech", Icons.Outlined.Computer, Color(0xFF7C6AF7)),
    SearchChip("Sports", "Sports", Icons.Outlined.SportsSoccer, Color(0xFF6AE09A)),
    SearchChip("Food", "Food", Icons.Outlined.Restaurant, Color(0xFFE0C06A)),
    SearchChip("Money", "Travel", Icons.Outlined.Money, Color(0xFFE06AB0)),
)
private val trendingTopics =
    listOf("AI agents", "Dune Part Two", "Euro 2024", "Smash Burger", "Patagonia")

// ─── Screen ───────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdleSearchScreen(onSelectChip: (String) -> Unit = {}) {
    var selectedChipId by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Category chips ───────────────────────────────────────────────
            Text(
                text = "Explore",
                color = TextSecondary,
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Spacer(Modifier.height(20.dp))

            TrendingChips()

            Spacer(Modifier.height(30.dp))

            ChipRow(
                chips = chips,
                selectedId = selectedChipId,
                onSelect = onSelectChip
            )
//            Spacer(Modifier.height(24.dp))

            // ── Dynamic content area ─────────────────────────────────────────
//            AnimatedContent(
//                targetState = uiState,
//                transitionSpec = {
//                    fadeIn(tween(300)) togetherWith fadeOut(tween(200))
//                },
//                label = "content"
//            ) { state ->
//                state.toString()
//            LoadingContent()
//            }
        }
    }
}

// ─── Chip row ─────────────────────────────────────────────────────────────────

@Composable
private fun ChipRow(
    chips: List<SearchChip>,
    selectedId: String?,
    onSelect: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(chips, key = { it.id }) { chip ->
            val selected = chip.id == selectedId
            val chipScale by animateFloatAsState(
                targetValue = if (selected) 1.05f else 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                label = "chipScale"
            )
            val bgColor by animateColorAsState(
                targetValue = if (selected) chip.color.copy(alpha = 0.18f) else ChipIdle,
                label = "chipBg"
            )
            val borderColor by animateColorAsState(
                targetValue = if (selected) chip.color else Color.Transparent,
                label = "chipBorder"
            )

            Box(
                modifier = Modifier
                    .graphicsLayer { scaleX = chipScale; scaleY = chipScale }
                    .clip(RoundedCornerShape(50))
                    .background(bgColor)
                    .border(1.dp, borderColor, RoundedCornerShape(50))
                    .clickable { onSelect(chip.id) }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = chip.icon,
                        contentDescription = chip.label,
                        tint = if (selected) chip.color else TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = chip.label,
                        color = if (selected) chip.color else TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Accent,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            color = TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun TrendingChips() {
    val colors = listOf(
        Color(0xFFE07B6A), Color(0xFF6ABDE0), Color(0xFF7C6AF7),
        Color(0xFF6AE09A), Color(0xFFE0C06A)
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        trendingTopics.forEachIndexed { idx, topic ->
            val color = colors[idx % colors.size]
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(color.copy(alpha = 0.12f))
                    .clickable {}
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "# $topic",
                    color = color,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun RecentSearchRow(term: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {}
            .padding(vertical = 12.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = term,
            color = TextSecondary,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Outlined.NorthWest,
            contentDescription = "Use",
            tint = TextSecondary.copy(alpha = 0.5f),
            modifier = Modifier.size(14.dp)
        )
    }
}

// ─── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF0F0F14)
@Composable
fun IdleSearchScreenPreview() {
    MaterialTheme {
        IdleSearchScreen()
    }
}