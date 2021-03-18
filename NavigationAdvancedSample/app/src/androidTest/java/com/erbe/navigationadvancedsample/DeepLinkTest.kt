package com.erbe.navigationadvancedsample

import android.content.Intent
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test

class DeepLinkTest {

    private val userName = "Person 2"

    private val url = "http://www.example.com/user/$userName"

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var activityTestRule = object : ActivityTestRule<MainActivity>(MainActivity::class.java) {
        override fun getActivityIntent(): Intent {
            return Intent(Intent.ACTION_VIEW, Uri.parse(url))
        }
    }

    @Test
    fun bottomNavView_DeepLink_HandlesIntent_BackGoesToList() {
        // Opening the app with the proper Intent should start it in the profile screen.
        assertInProfile()

        pressBack()

        // The list should be shown
        assertList()

        pressBack()

        // Home destination should be shown
        assertInHome()
    }

    private fun assertInProfile() {
        onView(withText(userName))
                .check(matches(isDisplayed()))
    }

    private fun assertList() {
        onView(allOf(withText(R.string.title_list), isDescendantOfA(withId(R.id.action_bar))))
                .check(matches(isDisplayed()))
    }

    private fun assertInHome() {
        onView(withText(R.string.welcome))
                .check(matches(isDisplayed()))
    }
}
