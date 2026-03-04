package com.robertcoding.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.robertcoding.data.network.NytApiService
import com.robertcoding.data.pagination.NytArticlePagingSource
import com.robertcoding.domain.model.Article
import com.robertcoding.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow

class ArticleRepositoryImpl(
    private val api: NytApiService
) : ArticleRepository {

    override fun getArticles(query: String): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NytArticlePagingSource(api, query) }
        ).flow
    }
}