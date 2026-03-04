package com.robertcoding.paginationnews.components.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.robertcoding.paginationnews.animation.CircularBouncingDots
import com.robertcoding.paginationnews.components.FetchArticlesComponent
import com.robertcoding.paginationnews.viewmodel.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel()
) {
    val searchQuery = viewModel.searchQuery.collectAsStateWithLifecycle()
    val isActive by viewModel.isSearchActive.collectAsStateWithLifecycle()
    val searchArticles = viewModel.articles.collectAsLazyPagingItems()
    val searchState = rememberSearchBarState()

    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBarComponent(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            searchState = searchState,
            query = searchQuery.value,
            onQueryChange = viewModel::onSearchQueryChanged,
            onSearch = { viewModel.onToggleSearchActive(false) },
            onToggleSearchActive = viewModel::onToggleSearchActive,
            isActive = isActive
        )

        FetchArticlesComponent(
            modifier = Modifier, searchArticles,
            searchQuery = { searchQuery.value }
        )
    }
}

