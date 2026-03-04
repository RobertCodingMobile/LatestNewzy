package com.robertcoding.data.repository

import com.robertcoding.common.CallerStatus
import com.robertcoding.common.CallerStatus.Companion.mapToCallerStatus
import com.robertcoding.data.mapper.toDomain
import com.robertcoding.data.network.NytApiService
import com.robertcoding.domain.model.Article
import com.robertcoding.domain.model.LatestNewsModel
import com.robertcoding.domain.repository.LatestNewsRepository

class LatestNewsRepositoryImpl(private val api: NytApiService) : LatestNewsRepository {
    override suspend fun getLatestNews(topic: String): CallerStatus<List<LatestNewsModel>> =
        try {
            val call = api.getLatestNews(topic)
            val articles = call.results.map { it.toDomain() }
            CallerStatus.Success(articles)
        } catch (exception: Exception) {
            println("AICI, error: ${exception.stackTraceToString()} ${exception.message}")
            exception.mapToCallerStatus()
        }
}