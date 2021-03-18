package com.erbe.livedata

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Simple UI test.
 */
@RunWith(AndroidJUnit4::class)
class BasicUiTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(LiveDataActivity::class.java)

    private lateinit var dataBindingIdlingResource: IdlingResource

    @Before
    fun registerIdlingResources() {
        dataBindingIdlingResource = DataBindingIdlingResource(activityScenarioRule)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResources() {
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun activityLaunches() {
        onView(withId(R.id.time_transformed)).check(matches(isDisplayed()))
    }
}
