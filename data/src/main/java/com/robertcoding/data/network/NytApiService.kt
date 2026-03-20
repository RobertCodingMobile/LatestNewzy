package com.robertcoding.data.network

import com.robertcoding.data.BuildConfig
import com.robertcoding.data.dto.LatestNewsResponse
import com.robertcoding.data.dto.NytResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class NytApiService(private val client: HttpClient) {

    suspend fun getArticles(query: String, page: Int): NytResponseDto {
        val call = client.get("https://api.nytimes.com/svc/search/v2/articlesearch.json") {
            url {
                parameters.append("q", query)
                parameters.append("page", page.toString())
                parameters.append("api-key", BuildConfig.API_KEY)
            }
        }
        val result = call.body<NytResponseDto>()
        return result
    }

    suspend fun getLatestNews(topic: String): LatestNewsResponse {
        val call = client.get("https://api.nytimes.com/svc/topstories/v2/$topic.json") {
            url {
                parameters.append("api-key", BuildConfig.API_KEY)
            }
        }
        val result = call.body<LatestNewsResponse>()
        return result
    }
}