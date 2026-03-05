package com.robertcoding.paginationnews.navigation.bottom

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.serialization.NavKeySerializer
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer

// ─── Tab definition ───────────────────────────────────────────────────────────

data class NavTab(
    val key: NavKey,
    val label: String,
    val icon: ImageVector,
)

// ─── State ────────────────────────────────────────────────────────────────────

class MultiStackNavState(
    val startRoute: NavKey,
    topLevelRoute: MutableState<NavKey>,
    val backStacks: Map<NavKey, NavBackStack<NavKey>>,
) {
    var currentTab: NavKey by topLevelRoute

    /** Live top-of-stack for the active tab — drives bottom bar visibility. */
    val currentDestination: NavKey?
        get() = backStacks[currentTab]?.lastOrNull()

    val activeStacks: List<NavKey>
        get() = if (currentTab == startRoute) listOf(startRoute)
        else listOf(startRoute, currentTab)
}

@Composable
fun rememberMultiStackNavState(tabs: List<NavTab>): MultiStackNavState {
    val startRoute = tabs.first().key
    val keys = tabs.map { it.key }.toSet()

    val currentTab = rememberSerializable(
        serializer = MutableStateSerializer(NavKeySerializer())
    ) { mutableStateOf(startRoute) }

    val backStacks = keys.associateWith { rememberNavBackStack(it) }

    return remember { MultiStackNavState(startRoute, currentTab, backStacks) }
}

// ─── Navigator ────────────────────────────────────────────────────────────────

class MultiStackNavigator(private val state: MultiStackNavState) {

    /**
     * General navigation.
     * - Tab root key → switch tab, or pop to root if already selected.
     * - Any other key → push onto the current tab's back stack.
     */
    fun navigate(key: NavKey) {
        if (key in state.backStacks) {
            if (key == state.currentTab) popToRoot() else state.currentTab = key
        } else {
            state.backStacks[state.currentTab]?.add(key)
        }
    }

    /**
     *
     * Always pushes [key] onto [targetTab]'s stack and switches to that tab,
     * regardless of which tab is currently open. This prevents cross-contamination
     * of unrelated back stacks.
     *
     * Example — article notification arrives while user is in Settings:
     * ```
     * navigator.navigateTo(ArticleDetail("123"), targetTab = HomeRoute)
     * ```
     * Back from the article lands back in Home, not Settings.
     */
    fun navigateTo(key: NavKey, targetTab: NavKey) {
        require(targetTab in state.backStacks) {
            "targetTab $targetTab is not a registered tab root"
        }
        state.backStacks[targetTab]?.add(key)
        state.currentTab = targetTab
    }

    fun goBack() {
        val stack = state.backStacks[state.currentTab] ?: return
        if (stack.last() == state.currentTab) {
            if (state.currentTab != state.startRoute) state.currentTab = state.startRoute
        } else {
            stack.removeLastOrNull()
        }
    }

    private fun popToRoot() {
        state.backStacks[state.currentTab]?.let { stack ->
            if (stack.size > 1) {
                stack.clear(); stack.add(state.currentTab)
            }
        }
    }
}

// ─── Entries converter ────────────────────────────────────────────────────────

@Composable
fun MultiStackNavState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>,
): SnapshotStateList<NavEntry<NavKey>> {
    val decorated = backStacks.mapValues { (_, stack) ->
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider,
        )
    }
    return activeStacks.flatMap { decorated[it] ?: emptyList() }.toMutableStateList()
}

// ─── Scaffold ─────────────────────────────────────────────────────────────────

/**
 * Drop-in multi-tab scaffold with:
 * - Independent per-tab back stacks with full state retention
 * - Correct system back behaviour (exits app at home root)
 * - Conditional bottom bar visibility per destination  [Flaw 2 fix]
 * - Targeted deep-link navigation via [navigateTo]     [Flaw 3 fix]
 *
 * @param showBottomBarFor Return `false` for destinations that should be
 *   full-screen (e.g. ArticleDetail, video player). Defaults to showing the
 *   bar only when the user is sitting at a tab root.
 *
 * Usage:
 * ```kotlin
 * MultiStackNavScaffold(
 *     tabs = NEWS_TABS,
 *     showBottomBarFor = { it is HomeRoute || it is NewzyRoute
 *                       || it is LocalRoute || it is SettingsRoute },
 * ) { navigator ->
 *     entry<HomeRoute>     { HomeScreen(onArticle = { navigator.navigate(ArticleDetail(it)) }) }
 *     entry<ArticleDetail> { key -> ArticleScreen(key.id) }  // bar hidden automatically
 *     entry<SearchNews>    { SearchScreen() }
 * }
 * ```
 */
@Composable
fun MultiStackNavScaffold(
    tabs: List<NavTab>,
    modifier: Modifier = Modifier,
    showBottomBarFor: (NavKey) -> Boolean = { key -> tabs.any { it.key == key } },
    content: EntryProviderScope<NavKey>.(MultiStackNavigator) -> Unit,
) {
    val state = rememberMultiStackNavState(tabs)
    val navigator = remember { MultiStackNavigator(state) }

    val canGoBack = state.currentTab != state.startRoute
            || (state.backStacks[state.startRoute]?.size ?: 0) > 1
    BackHandler(enabled = canGoBack) { navigator.goBack() }

    val entries = state.toEntries(entryProvider { content(navigator) })
    val showBottomBar = state.currentDestination?.let { showBottomBarFor(it) } ?: true

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    tabs.forEach { tab ->
                        val selected = tab.key == state.currentTab
                        NavigationBarItem(
                            selected = selected,
                            onClick = { navigator.navigate(tab.key) },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) },
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavDisplay(
            entries = entries,
            onBack = { navigator.goBack() },
            modifier = Modifier.padding(padding),
        )
    }
}