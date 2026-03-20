package com.robertcoding.data.repository

import android.util.Log
import com.robertcoding.common.CallerStatus
import com.robertcoding.common.CallerStatus.Companion.mapToCallerStatus
import com.robertcoding.data.mapper.toDomain
import com.robertcoding.data.network.NytApiService
import com.robertcoding.domain.model.LatestNewsModel
import com.robertcoding.domain.repository.LatestNewsRepository

class LatestNewsRepositoryImpl(private val api: NytApiService) : LatestNewsRepository {
    override suspend fun getLatestNews(topic: String): CallerStatus<List<LatestNewsModel>> =
        try {
            val call = api.getLatestNews(topic)
            val domainModel = call.results.map { it.toDomain() }
            CallerStatus.Success(domainModel)
        } catch (exception: Exception) {
            Log.e(
                "LatestNewsRepositoryImpl",
                "getLatestNews: ${exception.stackTraceToString()} ${exception.message}"
            )
            exception.mapToCallerStatus()
        }
}