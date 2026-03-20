package com.robertcoding.paginationnews.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.robertcoding.common.CallerStatus
import com.robertcoding.common.CallerStatus.Companion.mapToCallerStatus
import com.robertcoding.domain.repository.ArticleRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

private const val DEBOUNCE_TIME = 800L

class SearchViewModel(
    private val articlesRepository: ArticleRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive = _isSearchActive.asStateFlow()

    fun onSearchQueryChanged(query: String) {
        _searchQuery.update { query }
    }

    fun onToggleSearchActive(isActive: Boolean) {
        _isSearchActive.value = isActive
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val articles = _searchQuery
        .debounce(DEBOUNCE_TIME)
        .filter { it.isNotEmpty() }
        .onStart {
            _uiState.value = UiState.Loading
        }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            articlesRepository.getArticles(query)
                .catch { e ->
                    _uiState.update { UiState.Error(e.message ?: e.localizedMessage) }
                }
        }.cachedIn(viewModelScope)
}