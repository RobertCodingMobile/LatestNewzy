package com.robertcoding.paginationnews.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.robertcoding.domain.model.Article
import com.robertcoding.paginationnews.animation.CircularBouncingDots
import com.robertcoding.paginationnews.components.search.IdleSearchScreen
import com.robertcoding.paginationnews.components.search.NoArticlesFound

@Composable
fun FetchArticlesComponent(
    modifier: Modifier = Modifier,
    articles: LazyPagingItems<Article>,
    searchQuery: () -> String
) {

    Box(
        modifier = modifier
    ) {
        when {

            // IDLE
            searchQuery().isEmpty() -> IdleSearchScreen()

            articles.loadState.refresh is LoadState.Loading -> CircularBouncingDots()

            articles.loadState.refresh is LoadState.Error -> {
                val error = articles.loadState.refresh as LoadState.Error
                FullScreenError(
                    message = error.error.localizedMessage ?: "Unknown error occurred",
                    onRetry = { articles.retry() },
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            articles.itemCount == 0 -> NoArticlesFound(
                searchQuery(),
                modifier = Modifier.padding(top = 20.dp)
            )

            // 2. The Happy Path (The List)
            else -> {
                ArticleListContent(articles = articles)
            }
        }
    }
}

@Composable
private fun ArticleListContent(articles: LazyPagingItems<Article>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // The Data Items
        items(
            count = articles.itemCount,
            key = articles.itemKey { it.id }
        ) { index ->
            val article = articles[index]
            if (article != null) {
                ArticleCard(article = article)
            }
        }

        // 3. Handle Pagination States (Appending at the bottom)
        item {
            when (val appendState = articles.loadState.append) {
                is LoadState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                    }
                }

                is LoadState.Error -> {
                    PaginationErrorRow(
                        message = appendState.error.localizedMessage ?: "Failed to load more",
                        onRetry = { articles.retry() }
                    )
                }

                else -> Unit // Idle or NotLoading
            }
        }
    }
}

// --- Reusable UI Components ---

@Composable
fun ArticleCard(article: Article) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Read more at NYT",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun FullScreenError(modifier: Modifier = Modifier, message: String, onRetry: () -> Unit) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Try Again")
        }
    }
}

@Composable
fun PaginationErrorRow(message: String, onRetry: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        TextButton(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Preview
@Composable
private fun ArticleCardPreview() {
    ArticleCard(
        Article(
            id = "1",
            title = "Visit Romania...",
            url = ""
        )
    )
}

@Preview
@Composable
private fun PaginationErrorRowPreview() {
    PaginationErrorRow(message = "Bad network connection") {

    }
}

@Preview
@Composable
private fun FullScreenErrorPreview() {
    FullScreenError(message = "Bad network connection") {

    }
}