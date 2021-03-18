package com.erbe.viewbindingsample

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Simple UI test.
 */
@RunWith(AndroidJUnit4::class)
class SimpleUiTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun activityLaunches() {
        onView(withText(R.string.hello_from_vb_activity)).check(matches(isDisplayed()))
        onView(withText(R.string.hello_from_vb_bindfragment)).check(matches(isDisplayed()))
        onView(withText(R.string.hello_from_vb_inflatefragment)).check(matches(isDisplayed()))
    }
}