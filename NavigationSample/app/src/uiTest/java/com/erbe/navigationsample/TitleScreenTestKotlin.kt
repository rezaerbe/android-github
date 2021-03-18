package com.erbe.navigationsample

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

/**
 * A simple test class that can be run both on device (or emulator) or on the host (as a JVM test
 * using Robolectric).
 */
@RunWith(AndroidJUnit4::class)
class TitleScreenTestKotlin {

    @Test
    fun testNavigateToPlay() {
        // Create a TestNavHostController
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.setGraph(R.navigation.navigation)

        // Create a graphical FragmentScenario for the TitleScreen
        val titleScenario = launchFragmentInContainer<TitleScreen>()

        // Set the NavController property on the fragment
        titleScenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // Verify that performing a click changes the NavController's state
        onView(withId(R.id.play_btn)).perform(click())

        val currentDestination = navController.backStack.last()
        assertThat(currentDestination.destination.id).isEqualTo(R.id.register)
    }

    @Test
    fun testNavigateToLeaderboard() {
        // Create a TestNavHostController
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        navController.setGraph(R.navigation.navigation)

        // Create a graphical FragmentScenario for the TitleScreen
        val titleScenario = launchFragmentInContainer<TitleScreen>()

        // Set the NavController property on the fragment
        titleScenario.onFragment { fragment: TitleScreen ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        // Verify that performing a click changes the NavController's state
        onView(withId(R.id.leaderboard_btn)).perform(click())

        val currentDestination = navController.backStack.last()
        assertThat(currentDestination.destination.id).isEqualTo(R.id.leaderboard)
    }
}
