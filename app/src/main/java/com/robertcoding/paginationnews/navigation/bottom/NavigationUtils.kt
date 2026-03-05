package com.robertcoding.paginationnews.navigation.bottom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.navigation3.runtime.NavKey
import com.robertcoding.paginationnews.navigation.LatestNews
import com.robertcoding.paginationnews.navigation.ManageAccount
import com.robertcoding.paginationnews.navigation.NewzyBookmarkRoute
import com.robertcoding.paginationnews.navigation.NewzyRemoteRoute
import com.robertcoding.paginationnews.navigation.SearchNews
import com.robertcoding.paginationnews.navigation.SingleArticleDetail

fun showBottomBarFor(key: NavKey) =
    key !is SingleArticleDetail && key !is ManageAccount && key !is SearchNews

val NEWS_TABS = listOf(
    NavTab(LatestNews, "Latest", Icons.Default.Home),
    NavTab(NewzyRemoteRoute, "Newzy", Icons.Default.Newspaper),
    NavTab(NewzyBookmarkRoute, "Bookmark", Icons.Default.Save),
    NavTab(ManageAccount, "Settings", Icons.Default.Settings),
)
