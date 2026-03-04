package com.robertcoding.paginationnews.components.latestnews

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.robertcoding.common.CallerStatus
import com.robertcoding.domain.model.LatestNewsModel
import com.robertcoding.paginationnews.R
import com.robertcoding.paginationnews.animation.CircularBouncingDots
import com.robertcoding.paginationnews.components.FullScreenError
import com.robertcoding.paginationnews.components.search.SearchBarComponent
import com.robertcoding.paginationnews.ui.theme.PaginationNewsTheme
import kotlinx.coroutines.launch

@Composable
fun LatestNewzyScreen(
    modifier: Modifier = Modifier,
    state: CallerStatus<List<LatestNewsModel>>,
    onArticleClick: (String) -> Unit,
    onSearchIconClick: () -> Unit,
) {
    PaginationNewsTheme {
        val context = LocalContext.current


        when (state) {
            is CallerStatus.Loading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularBouncingDots()
            }

            is CallerStatus.Success -> ArticlesList(
                articles = state.data,
                onArticleClick = {
                    onArticleClick(it)
                },
                onSearchIconClick = onSearchIconClick,
            )

            is CallerStatus.Error -> FullScreenError(message = state.message.asString(context)) {
            }
        }
    }
}


/**
 * Main articles list with smooth animations and sophisticated layout
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArticlesList(
    articles: List<LatestNewsModel>,
    onArticleClick: (String) -> Unit,
    onSearchIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PaginationNewsTheme {

        val lazyListState = rememberLazyListState()
        val scope = rememberCoroutineScope()
        val scrollPosition = remember { derivedStateOf { lazyListState.firstVisibleItemIndex > 5 } }

        Scaffold(
            topBar = {
                SearchBarComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            enabled = true,
                            onClick = onSearchIconClick
                        )
                        .padding(horizontal = 10.dp),
                    isEnabled = false
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                AnimatedVisibility(scrollPosition.value) {
                    FloatingActionButton(onClick = {
                        scope.launch {
                            lazyListState.animateScrollToItem(0)
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Navigation, contentDescription = null)
                    }
                }
            }
        ) { innerPadding ->


            LazyColumn(
                state = lazyListState,
                modifier = modifier
                    .fillMaxSize()
                    .background(PaginationNewsTheme.colors.background)
                    .padding(paddingValues = innerPadding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header
                item {
                    NYTHeader()
                }

                // Articles with staggered animations
                items(items = articles, key = {
                    "${it.title}_${it.abstract}"
                }, contentType = {
                    "ArticleCard"
                }) { article ->
                    ArticleCard(
                        article = article,
                        onClick = { onArticleClick(article.url) },
                    )
                }

                // Footer spacing
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

/**
 * Editorial header with sophisticated typography
 */
@Composable
private fun NYTHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Accent line
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(3.dp)
                .background(
                    color = PaginationNewsTheme.colors.surfaceContainer,
                    shape = RoundedCornerShape(2.dp)
                )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "THE NEW YORK TIMES",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 2.sp,
            color = PaginationNewsTheme.colors.secondary,
        )

        Text(
            text = "Top Stories",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = PaginationNewsTheme.colors.primary,
            lineHeight = 40.sp,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Breaking news and stories from around the world",
            fontSize = 15.sp,
            color = PaginationNewsTheme.colors.secondary,
            lineHeight = 22.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Individual article card with image, gradient overlay, and metadata
 */
@Composable
private fun ArticleCard(
    article: LatestNewsModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isHovered by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(PaginationNewsTheme.colors.surface)
            .border(
                width = 1.dp,
                color = PaginationNewsTheme.colors.surfaceTint,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
    ) {
        // Article Image with gradient overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(PaginationNewsTheme.colors.secondary.copy(alpha = 0.1f))
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
        ) {
            // Main image
            article.multimedia?.firstOrNull()?.let { media ->
                AsyncImage(
                    model = media.url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            } ?: run {
                AsyncImage(
                    model = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
        }

            // Gradient overlay for text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.6f),
                                Color.Black.copy(alpha = 0.8f)
                            ),
                            startY = 100f
                        )
                    )
            )

            // Metadata over image
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                // Section badge
                Surface(
                    modifier = Modifier.wrapContentSize(),
                    shape = RoundedCornerShape(4.dp),
                    color = PaginationNewsTheme.colors.surfaceContainer
                ) {
                    Text(
                        text = article.section.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        letterSpacing = 1.2.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Title over image
                Text(
                    text = article.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    lineHeight = 24.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Article details section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Abstract
            article.abstract?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    color = PaginationNewsTheme.colors.secondary,
                    lineHeight = 20.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Byline and metadata row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Author
                    article.byline?.let { byline ->
                        Text(
                            text = byline.replace("By ", ""),
                            fontSize = 12.sp,
                            color = PaginationNewsTheme.colors.tertiary,
                            fontStyle = FontStyle.Italic,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                    }

                    // Dot separator
                    Box(
                        modifier = Modifier
                            .size(3.dp)
                            .background(
                                color = PaginationNewsTheme.colors.tertiary,
                                shape = CircleShape
                            )
                    )

                    // Reading time estimate
                    Text(
                        text = estimateReadingTime(article.abstract ?: ""),
                        fontSize = 12.sp,
                        color = PaginationNewsTheme.colors.tertiary,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Arrow icon
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = PaginationNewsTheme.colors.tertiary,
                    modifier = Modifier
                        .size(20.dp)
                        .alpha(if (isHovered) 1f else 0.6f)
                )
            }

            // Date
            Text(
                text = formatDate(article.publishedDate),
                fontSize = 11.sp,
                color = PaginationNewsTheme.colors.tertiary,
                letterSpacing = 0.5.sp
            )
        }
    }
}

/**
 * Utility: Estimate reading time
 */
private fun estimateReadingTime(text: String): String {
    val words = text.split("\\s+".toRegex()).size
    val minutes = maxOf(1, words / 200)
    return "$minutes min read"
}

/**
 * Utility: Format date
 */
private fun formatDate(date: String?): String {
    return date?.take(10) ?: "Recently"
}