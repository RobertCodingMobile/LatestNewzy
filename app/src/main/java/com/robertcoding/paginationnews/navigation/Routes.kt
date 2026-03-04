package com.robertcoding.paginationnews.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object AppStarter: NavKey

@Serializable
data object LatestNews: NavKey

@Serializable
data object SearchNews: NavKey

@Serializable
data class SingleArticleDetail(val articleId: String): NavKey

@Serializable
data object ManageAccount: NavKey