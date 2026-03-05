package com.robertcoding.paginationnews.navigation.bottom

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.robertcoding.paginationnews.components.account.ManageAccountRoute
import com.robertcoding.paginationnews.components.latestnews.LatestNewzyRoute
import com.robertcoding.paginationnews.components.search.SearchScreen
import com.robertcoding.paginationnews.components.singlearticle.ArticleWebViewScreen
import com.robertcoding.paginationnews.components.splash.SplashScreen
import com.robertcoding.paginationnews.navigation.LatestNews
import com.robertcoding.paginationnews.navigation.ManageAccount
import com.robertcoding.paginationnews.navigation.NewzyBookmarkRoute
import com.robertcoding.paginationnews.navigation.NewzyRemoteRoute
import com.robertcoding.paginationnews.navigation.SearchNews
import com.robertcoding.paginationnews.navigation.SingleArticleDetail


@Composable
fun BottomNewzyNavigation(modifier: Modifier = Modifier) {
    val splashDone = rememberSaveable { mutableStateOf(false) }

    if (!splashDone.value) {
        SplashScreen(onFinished = { splashDone.value = true })
    } else {
        MultiStackNavScaffold(
            tabs = NEWS_TABS,
            modifier = modifier,
            showBottomBarFor = ::showBottomBarFor
        ) { navigator ->

            // ── Home tab ─────────────────────────────────────────────────────
            entry<LatestNews> {
                LatestNewzyRoute(
                    onArticleClick = { navigator.navigate(SingleArticleDetail(it)) },
                    onSearchIconClick = { navigator.navigate(SearchNews) },
                )
            }

            // ── Newzy tab ─────────────────────────────────────────────────────
            entry<NewzyRemoteRoute> {
                Text("Newzy")
            }

            // ── Local tab ─────────────────────────────────────────────────────
            entry<NewzyBookmarkRoute> {
                Text("Your Local screen here")
                // Your Local screen here
            }

            // ── Settings tab ──────────────────────────────────────────────────
            entry<ManageAccount> {
                ManageAccountRoute()
            }

            entry<SearchNews> {
                SearchScreen()
            }

            entry<SingleArticleDetail> { key ->
                ArticleWebViewScreen(key.articleId) { navigator.goBack() }
            }
        }
    }
}