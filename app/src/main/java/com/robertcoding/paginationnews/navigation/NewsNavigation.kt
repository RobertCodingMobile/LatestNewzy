package com.robertcoding.paginationnews.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.robertcoding.paginationnews.animation.NewzyLoadingAnimation
import com.robertcoding.paginationnews.components.account.ManageAccountRoute
import com.robertcoding.paginationnews.components.search.SearchScreen
import com.robertcoding.paginationnews.components.latestnews.LatestNewzyRoute
import com.robertcoding.paginationnews.components.singlearticle.ArticleWebViewScreen
import com.robertcoding.paginationnews.ui.theme.CardOcean
import kotlinx.coroutines.delay

@Composable
fun NewsNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(AppStarter)

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<AppStarter> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = CardOcean),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NewzyLoadingAnimation()
                    LaunchedEffect(Unit) {
                        delay(2000)
                        backStack.apply {
                            clear()
                            add(LatestNews)
                        }
                    }
                }
            }
            entry<LatestNews> {
                LatestNewzyRoute(
                    onArticleClick = {
                        backStack.add(SingleArticleDetail(it))
                    },
                    onSearchIconClick = {
                        backStack.removeLastOrNull()
                        backStack.add(SearchNews)
                    }
                )
            }
            entry<SearchNews> {
                val navigateBackToLatest = {
                    backStack.remove(SearchNews)
                    backStack.add(LatestNews)
                }
                BackHandler { navigateBackToLatest() }
                SearchScreen()
            }
            entry<SingleArticleDetail> { key ->
                ArticleWebViewScreen(key.articleId) {
                    backStack.removeLastOrNull()
                }
            }
            entry<ManageAccount> {
               ManageAccountRoute()
            }
        }
    )
}