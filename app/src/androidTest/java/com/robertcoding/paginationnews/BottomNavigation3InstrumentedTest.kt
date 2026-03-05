package com.robertcoding.paginationnews

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.robertcoding.paginationnews.navigation.bottom.BottomNewzyNavigation
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BottomNavigation3InstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun pressingBackOnHomeRoot_closesApp() {
        var appClosed = false

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                appClosed = true
            }
        }

        composeTestRule.setContent {
            // We get the real dispatcher from the Activity
            val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

            // SideEffect to attach our "App Killer" callback
            LaunchedEffect(dispatcher) {
                dispatcher?.addCallback(callback)
            }

            BottomNewzyNavigation()
        }

        // Advance clock to skip Splash
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.mainClock.advanceTimeBy(2500)
        composeTestRule.mainClock.autoAdvance = true

        // Verify UI is ready
        composeTestRule.onNodeWithText("Latest").assertIsDisplayed()

        // Trigger back press
        composeTestRule.runOnUiThread {
            composeTestRule.activity.onBackPressedDispatcher.onBackPressed()
        }

        assert(appClosed) { "The back press was intercepted by a BackHandler and didn't reach the Activity!" }
    }

    @Test
    fun navigatingToSettings_hidesBottomNavigation() {
        composeTestRule.setContent {
            BottomNewzyNavigation()
        }

        // Wait for Splash
        composeTestRule.mainClock.advanceTimeBy(2500)

        // Navigate to Settings
        composeTestRule.onNodeWithText("Settings").performClick()

        // Verify Manage Account
        composeTestRule.onNodeWithText("App Settings").assertIsDisplayed()

        composeTestRule.onNodeWithText("Newzy").assertDoesNotExist()
        composeTestRule.onNodeWithText("Latest").assertDoesNotExist()
    }

    @Test
    fun interactWithSearchBar() {
        composeTestRule.setContent {
            BottomNewzyNavigation()
        }

        // Wait for Splash
        composeTestRule.mainClock.autoAdvance = false
        composeTestRule.mainClock.advanceTimeBy(2000)
        composeTestRule.mainClock.autoAdvance = true

        // 2. Wait for Loading to finish
        // We wait until the placeholder is actually in the composition
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag("SearchBar")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Click search bar
        composeTestRule.onNodeWithTag("SearchBar").performClick()

        //Check botto, navigation is gone
        composeTestRule.onNodeWithText("Newzy").assertDoesNotExist()
        composeTestRule.onNodeWithText("Latest").assertDoesNotExist()

        composeTestRule.onNodeWithText("Explore").assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("SearchBar")
            .performClick()

        composeTestRule
            .onNode(hasSetTextAction())
            .performTextInput("Kotlin")

        composeTestRule
            .onNodeWithText("Kotlin")
            .assertIsDisplayed()
    }
}