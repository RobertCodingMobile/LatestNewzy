package com.robertcoding.paginationnews.components.latestnews

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.robertcoding.domain.model.LatestNewsModel
import com.robertcoding.paginationnews.viewmodel.LatestNewzyViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LatestNewzyRoute(
    modifier: Modifier = Modifier,
    viewModel: LatestNewzyViewModel = koinViewModel(),
    onArticleClick: (String) -> Unit,
    onSearchIconClick: () -> Unit,
) {
    val articlesState by viewModel.latestNewzy.collectAsStateWithLifecycle()


    // Pass state and lambdas down
    LatestNewzyScreen(
        state = articlesState,
        onArticleClick = onArticleClick,
        onSearchIconClick = onSearchIconClick,
        modifier = modifier,
    )
}