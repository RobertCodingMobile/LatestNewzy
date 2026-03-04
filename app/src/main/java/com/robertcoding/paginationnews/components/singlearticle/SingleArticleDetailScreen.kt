//package com.robertcoding.paginationnews.components.singlearticle
//
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.robertcoding.paginationnews.animation.NYTColorScheme
import com.robertcoding.paginationnews.ui.theme.DividerColor
import org.koin.androidx.compose.koinViewModel
//
//@Composable
//fun SingleArticleScreen(
//    id: String,
//    viewModel: SingleArticleViewModel = koinViewModel(),
//    onBack: () -> Unit
//) {
//
//    val scrollState = rememberScrollState()
//    val imageHeight = 360.dp
//    val imageHeightPx = with(LocalDensity.current) { imageHeight.toPx() }
//    val imageParallax by remember {
//        derivedStateOf { (scrollState.value * 0.4f).coerceAtMost(imageHeightPx * 0.4f) }
//    }
//
//    val singleArticleState by viewModel.state.collectAs
//
//    Box(modifier = Modifier.fillMaxSize()) {
//
//        // ── Scrollable body ──────────────────────────────────────────────────
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(scrollState)
//        ) {
//
//            // ── Hero image ───────────────────────────────────────────────────
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(imageHeight)
//                    .clipToBounds()
//            ) {
//                AsyncImage(
//                    model = article.multimediaUrl,
//                    contentDescription = article.headline,
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(imageHeight + 80.dp)
//                        .graphicsLayer { translationY = imageParallax }
//                )
//                // Bottom gradient fade into background
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(160.dp)
//                        .align(Alignment.BottomCenter)
//                        .background(
//                            Brush.verticalGradient(
//                                colors = listOf(
//                                    NYTColorScheme.TextTertiary,
//                                    NYTColorScheme.Background
//                                )
//                            )
//                        )
//                )
//            }
//
//            // ── Article content card ─────────────────────────────────────────
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .offset(y = (-32).dp)
//                    .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp))
//                    .background(NYTColorScheme.Background)
//                    .padding(horizontal = 20.dp)
//            ) {
//
//                // Category badge
//                Box(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(4.dp))
//                        .background(NYTColorScheme.Surface)
//                        .padding(horizontal = 10.dp, vertical = 5.dp)
//                ) {
//                    Text(
//                        text = article.section.uppercase(),
//                        color = NYTColorScheme.TextPrimary,
//                        fontSize = 11.sp,
//                        fontWeight = FontWeight.Bold,
//                        letterSpacing = 1.2.sp
//                    )
//                }
//
//                Spacer(Modifier.height(16.dp))
//
//                // Headline
//                Text(
//                    text = article.headline,
//                    color = NYTColorScheme.TextPrimary,
//                    fontSize = 26.sp,
//                    fontWeight = FontWeight.Bold,
//                    lineHeight = 33.sp
//                )
//
//                Spacer(Modifier.height(12.dp))
//
//                // Abstract / standfirst
//                Text(
//                    text = article.abstract,
//                    color = Color(0xFFB0B0B0),
//                    fontSize = 16.sp,
//                    lineHeight = 24.sp
//                )
//
//                Spacer(Modifier.height(20.dp))
//                HorizontalDivider(color = NYTColorScheme.Border, thickness = 0.5.dp)
//                Spacer(Modifier.height(14.dp))
//
//                // Author • read time row
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = article.byline,
//                        color = NYTColorScheme.TextSecondary,
//                        fontSize = 14.sp,
//                        fontStyle = FontStyle.Italic,
//                        modifier = Modifier.weight(1f)
//                    )
//                    Text(
//                        text = "•",
//                        color = NYTColorScheme.TextSecondary,
//                        fontSize = 14.sp,
//                        modifier = Modifier.padding(horizontal = 8.dp)
//                    )
//                    Text(
//                        text = article.readTime,
//                        color = NYTColorScheme.TextSecondary,
//                        fontSize = 14.sp
//                    )
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
//                        contentDescription = null,
//                        tint = NYTColorScheme.TextSecondary,
//                        modifier = Modifier.size(20.dp)
//                    )
//                }
//
//                Spacer(Modifier.height(6.dp))
//
//                // Date
//                Text(
//                    text = article.publishedDate,
//                    color = NYTColorScheme.TextSecondary,
//                    fontSize = 13.sp
//                )
//
//                Spacer(Modifier.height(28.dp))
//                HorizontalDivider(color = DividerColor, thickness = 0.5.dp)
//                Spacer(Modifier.height(24.dp))
//
//                // Body paragraphs
////                article.body.forEachIndexed { index, paragraph ->
////                    Text(
////                        text = paragraph,
////                        color = Color(0xFFD8D8D8),
////                        fontSize = 17.sp,
////                        lineHeight = 28.sp
////                    )
////                    if (index < article.body.lastIndex) {
////                        Spacer(Modifier.height(20.dp))
////                    }
////                }
//
//                Spacer(Modifier.height(40.dp))
//                HorizontalDivider(color = DividerColor, thickness = 0.5.dp)
//                Spacer(Modifier.height(20.dp))
//
//                // Footer: source credit
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Icon(
//                        imageVector = Icons.Outlined.OpenInBrowser,
//                        contentDescription = "Read full article",
//                        tint = NYTColorScheme.TextSecondary,
//                        modifier = Modifier.size(16.dp)
//                    )
//                    Spacer(Modifier.width(8.dp))
//                    Text(
//                        text = "Read full story on NYT",
//                        color = NYTColorScheme.TextSecondary,
//                        fontSize = 13.sp
//                    )
//                }
//
//                Spacer(Modifier.height(80.dp))
//            }
//        }
//
//        // ── Sticky top bar (fades in on scroll) ─────────────────────────────
////        Box(
////            modifier = Modifier
////                .fillMaxWidth()
////                .statusBarsPadding()
////                .background(BackgroundColor.copy(alpha = toolbarAlpha))
////                .padding(horizontal = 8.dp, vertical = 6.dp)
////        ) {
////            IconButton(onClick = onBack) {
////                Icon(
////                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
////                    contentDescription = "Back",
////                    tint = TextPrimary
////                )
////            }
////            if (toolbarAlpha > 0.5f) {
////                Text(
////                    text = article.section.uppercase(),
////                    color = TextPrimary,
////                    fontSize = 13.sp,
////                    fontWeight = FontWeight.SemiBold,
////                    letterSpacing = 0.8.sp,
////                    modifier = Modifier.align(Alignment.Center)
////                )
////            }
////            IconButton(
////                onClick = { /* share */ },
////                modifier = Modifier.align(Alignment.CenterEnd)
////            ) {
////                Icon(
////                    imageVector = Icons.Outlined.Share,
////                    contentDescription = "Share",
////                    tint = TextPrimary
////                )
////            }
////        }
//    }
//}