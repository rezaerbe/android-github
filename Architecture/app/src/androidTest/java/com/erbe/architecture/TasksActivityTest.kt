package com.erbe.architecture

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.erbe.architecture.R.string
import com.erbe.architecture.data.Task
import com.erbe.architecture.data.source.TasksRepository
import com.erbe.architecture.tasks.TasksActivity
import com.erbe.architecture.util.EspressoIdlingResource
import com.erbe.architecture.util.saveTaskBlocking
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.allOf
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * Large End-to-End test for the tasks module.
 *
 * UI tests usually use [ActivityTestRule] but there's no API to perform an action before
 * each test. The workaround is to use `ActivityScenario.launch()` and `ActivityScenario.close()`.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class TasksActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: TasksRepository

    // An Idling Resource that waits for Data Binding to have no pending bindings
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun init() {
        // Populate @Inject fields in test class
        hiltRule.inject()
    }

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun editTask() {
        repository.saveTaskBlocking(Task("TITLE1", "DESCRIPTION"))

        // start up Tasks screen
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the task on the list and verify that all the data is correct
        onView(withText("TITLE1")).perform(click())
        onView(withId(R.id.task_detail_title_text)).check(matches(withText("TITLE1")))
        onView(withId(R.id.task_detail_description_text)).check(matches(withText("DESCRIPTION")))
        onView(withId(R.id.task_detail_complete_checkbox)).check(matches(not(isChecked())))

        // Click on the edit button, edit, and save
        onView(withId(R.id.edit_task_fab)).perform(click())
        onView(withId(R.id.add_task_title_edit_text)).perform(replaceText("NEW TITLE"))
        onView(withId(R.id.add_task_description_edit_text)).perform(replaceText("NEW DESCRIPTION"))
        onView(withId(R.id.save_task_fab)).perform(click())

        // Verify task is displayed on screen in the task list.
        onView(withText("NEW TITLE")).check(matches(isDisplayed()))
        // Verify previous task is not displayed
        onView(withText("TITLE1")).check(doesNotExist())
        // Make sure the activity is closed before resetting the db:
        activityScenario.close()
    }

    @Test
    fun createOneTask_deleteTask() {

        // start up Tasks screen
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Add active task
        onView(withId(R.id.add_task_fab)).perform(click())
        onView(withId(R.id.add_task_title_edit_text))
            .perform(typeText("TITLE1"), closeSoftKeyboard())
        onView(withId(R.id.add_task_description_edit_text)).perform(typeText("DESCRIPTION"))
        onView(withId(R.id.save_task_fab)).perform(click())

        // Open it in details view
        onView(withText("TITLE1")).perform(click())
        // Click delete task in menu
        onView(withId(R.id.menu_delete)).perform(click())

        // Verify it was deleted
        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(string.nav_all)).perform(click())
        onView(withText("TITLE1")).check(doesNotExist())
        // Make sure the activity is closed before resetting the db:
        activityScenario.close()
    }

    @Test
    fun createTwoTasks_deleteOneTask() {
        repository.saveTaskBlocking(Task("TITLE1", "DESCRIPTION"))
        repository.saveTaskBlocking(Task("TITLE2", "DESCRIPTION"))

        // start up Tasks screen
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Open the second task in details view
        onView(withText("TITLE2")).perform(click())
        // Click delete task in menu
        onView(withId(R.id.menu_delete)).perform(click())

        // Verify only one task was deleted
        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(string.nav_all)).perform(click())
        onView(withText("TITLE1")).check(matches(isDisplayed()))
        onView(withText("TITLE2")).check(doesNotExist())
        // Make sure the activity is closed before resetting the db:
        activityScenario.close()
    }

    @Test
    fun markTaskAsCompleteOnDetailScreen_taskIsCompleteInList() {
        // Add 1 active task
        val taskTitle = "COMPLETED"
        repository.saveTaskBlocking(Task(taskTitle, "DESCRIPTION"))

        // start up Tasks screen
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the task on the list
        onView(withText(taskTitle)).perform(click())

        // Click on the checkbox in task details screen
        onView(withId(R.id.task_detail_complete_checkbox)).perform(click())

        // Click on the navigation up button to go back to the list
        onView(
            withContentDescription(
                activityScenario.getToolbarNavigationContentDescription()
            )
        ).perform(click())

        // Check that the task is marked as completed
        onView(allOf(withId(R.id.complete_checkbox), hasSibling(withText(taskTitle))))
            .check(matches(isChecked()))
        // Make sure the activity is closed before resetting the db:
        activityScenario.close()
    }

    @Test
    fun markTaskAsActiveOnDetailScreen_taskIsActiveInList() {
        // Add 1 completed task
        val taskTitle = "ACTIVE"
        repository.saveTaskBlocking(Task(taskTitle, "DESCRIPTION", true))

        // start up Tasks screen
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the task on the list
        onView(withText(taskTitle)).perform(click())
        // Click on the checkbox in task details screen
        onView(withId(R.id.task_detail_complete_checkbox)).perform(click())

        // Click on the navigation up button to go back to the list
        onView(
            withContentDescription(
                activityScenario.getToolbarNavigationContentDescription()
            )
        ).perform(click())

        // Check that the task is marked as active
        onView(allOf(withId(R.id.complete_checkbox), hasSibling(withText(taskTitle))))
            .check(matches(not(isChecked())))
        // Make sure the activity is closed before resetting the db:
        activityScenario.close()
    }

    @Test
    fun markTaskAsCompleteAndActiveOnDetailScreen_taskIsActiveInList() {
        // Add 1 active task
        val taskTitle = "ACT-COMP"
        repository.saveTaskBlocking(Task(taskTitle, "DESCRIPTION"))

        // start up Tasks screen
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the task on the list
        onView(withText(taskTitle)).perform(click())
        // Click on the checkbox in task details screen
        onView(withId(R.id.task_detail_complete_checkbox)).perform(click())
        // Click again to restore it to original state
        onView(withId(R.id.task_detail_complete_checkbox)).perform(click())

        // Click on the navigation up button to go back to the list
        onView(
            withContentDescription(
                activityScenario.getToolbarNavigationContentDescription()
            )
        ).perform(click())

        // Check that the task is marked as active
        onView(allOf(withId(R.id.complete_checkbox), hasSibling(withText(taskTitle))))
            .check(matches(not(isChecked())))
        // Make sure the activity is closed before resetting the db:
        activityScenario.close()
    }

    @Test
    fun markTaskAsActiveAndCompleteOnDetailScreen_taskIsCompleteInList() {
        // Add 1 completed task
        val taskTitle = "COMP-ACT"
        repository.saveTaskBlocking(Task(taskTitle, "DESCRIPTION", true))

        // start up Tasks screen
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the task on the list
        onView(withText(taskTitle)).perform(click())
        // Click on the checkbox in task details screen
        onView(withId(R.id.task_detail_complete_checkbox)).perform(click())
        // Click again to restore it to original state
        onView(withId(R.id.task_detail_complete_checkbox)).perform(click())

        // Click on the navigation up button to go back to the list
        onView(
            withContentDescription(
                activityScenario.getToolbarNavigationContentDescription()
            )
        ).perform(click())

        // Check that the task is marked as active
        onView(allOf(withId(R.id.complete_checkbox), hasSibling(withText(taskTitle))))
            .check(matches(isChecked()))
        // Make sure the activity is closed before resetting the db:
        activityScenario.close()
    }

    @Test
    fun createTask() {
        // start up Tasks screen
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the "+" button, add details, and save
        onView(withId(R.id.add_task_fab)).perform(click())
        onView(withId(R.id.add_task_title_edit_text))
            .perform(typeText("title"), closeSoftKeyboard())
        onView(withId(R.id.add_task_description_edit_text)).perform(typeText("description"))
        onView(withId(R.id.save_task_fab)).perform(click())

        // Then verify task is displayed on screen
        onView(withText("title")).check(matches(isDisplayed()))
        // Make sure the activity is closed before resetting the db:
        activityScenario.close()
    }
}
