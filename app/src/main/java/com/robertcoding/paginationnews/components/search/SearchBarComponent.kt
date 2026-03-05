package com.robertcoding.paginationnews.components.search

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarComponent(
    modifier: Modifier = Modifier,
    searchState: SearchBarState = rememberSearchBarState(),
    query: String = "",
    onQueryChange: (String) -> Unit = {},
    onSearch: () -> Unit = {},
    onToggleSearchActive: (Boolean) -> Unit = {},
    isActive: Boolean = true,
    isEnabled: Boolean = true,
    placeholder: String = "Search NYT Articles..."
) {
    SearchBar(
        modifier = modifier
            .testTag(tag = "SearchBar"),
        state = searchState,
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = { onSearch() },
                expanded = isActive,
                onExpandedChange = onToggleSearchActive,
                enabled = isEnabled,
                placeholder = { Text(text = placeholder) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (isActive) {
                        IconButton(
                            onClick = {
                                if (query.isNotEmpty()) {
                                    onQueryChange("")
                                } else {
                                    onToggleSearchActive(false)
                                }
                            }
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Clear search")
                        }
                    }
                },
                colors = SearchBarDefaults.colors().inputFieldColors,
                interactionSource = null,
            )
        }
    )
}