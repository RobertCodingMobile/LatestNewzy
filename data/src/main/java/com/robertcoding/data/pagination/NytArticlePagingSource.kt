package com.robertcoding.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.robertcoding.data.mapper.toDomain
import com.robertcoding.data.network.NytApiService
import com.robertcoding.domain.model.Article

class NytArticlePagingSource(
    private val api: NytApiService,
    private val query: String
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val currentPage = params.key ?: 0
            val response = api.getArticles(query = query, page = currentPage)

            val articlesDto = response.response.docs
            val totalHits = response.response.meta?.hits ?: 0

            // Map DTOs to Domain models
            val articles = articlesDto.map { it.toDomain() }

            // Math to determine if we should stop paginating
            val hasMoreData = (currentPage * 10) + articles.size < totalHits
            val isWithinApiLimit = currentPage < 99 // NYT max page is 100 (0-indexed)

            val nextKey = if (hasMoreData && isWithinApiLimit && articles.isNotEmpty()) {
                currentPage + 1
            } else {
                null // The stop sign 🛑
            }

            LoadResult.Page(
                data = articles,
                prevKey = if (currentPage == 0) null else currentPage - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}