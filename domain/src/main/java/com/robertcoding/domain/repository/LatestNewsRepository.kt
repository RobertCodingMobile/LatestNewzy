package com.robertcoding.domain.repository

import com.robertcoding.common.CallerStatus
import com.robertcoding.domain.model.Article
import com.robertcoding.domain.model.LatestNewsModel

interface LatestNewsRepository {
    suspend fun getLatestNews(topic: String = "world"): CallerStatus<List<LatestNewsModel>>
}