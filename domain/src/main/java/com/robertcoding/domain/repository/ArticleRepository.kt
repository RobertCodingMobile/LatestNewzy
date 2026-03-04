package com.robertcoding.domain.repository

import androidx.paging.PagingData
import com.robertcoding.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    fun getArticles(query: String): Flow<PagingData<Article>>
}