package com.erbe.architecture.tasks

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.erbe.architecture.R
import com.erbe.architecture.data.Task
import com.erbe.architecture.data.source.TasksRepository
import com.erbe.architecture.launchFragmentInHiltContainer
import com.erbe.architecture.util.saveTaskBlocking
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

/**
 * Integration test for the Task List screen.
 */
@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class TasksFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: TasksRepository

    @Before
    fun init() {
        // Populate @Inject fields in test class
        hiltRule.inject()
    }

    @Test
    fun displayTask_whenRepositoryHasData() {
        // GIVEN - One task already in the repository
        repository.saveTaskBlocking(Task("TITLE1", "DESCRIPTION1"))

        // WHEN - On startup
        launchActivity()

        // THEN - Verify task is displayed on screen
        onView(withText("TITLE1")).check(matches(isDisplayed()))
    }

    @Test
    fun displayActiveTask() {
        repository.saveTaskBlocking(Task("TITLE1", "DESCRIPTION1"))

        launchActivity()

        onView(withText("TITLE1")).check(matches(isDisplayed()))

        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_active)).perform(click())
        onView(withText("TITLE1")).check(matches(isDisplayed()))

        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_completed)).perform(click())
        onView(withText("TITLE1")).check(matches(not(isDisplayed())))
    }

    @Test
    fun displayCompletedTask() {
        repository.saveTaskBlocking(Task("TITLE1", "DESCRIPTION1", true))

        launchActivity()

        onView(withText("TITLE1")).check(matches(isDisplayed()))

        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_active)).perform(click())
        onView(withText("TITLE1")).check(matches(not(isDisplayed())))

        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_completed)).perform(click())

        onView(withText("TITLE1")).check(matches(isDisplayed()))
    }

    @Test
    fun deleteOneTask() {
        repository.saveTaskBlocking(Task("TITLE1", "DESCRIPTION1"))

        launchActivity()

        // Open it in details view
        onView(withText("TITLE1")).perform(click())

        // Click delete task in menu
        onView(withId(R.id.menu_delete)).perform(click())

        // Verify it was deleted
        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_all)).perform(click())
        onView(withText("TITLE1")).check(doesNotExist())
    }

    @Test
    fun deleteOneOfTwoTasks() {
        repository.saveTaskBlocking(Task("TITLE1", "DESCRIPTION1"))
        repository.saveTaskBlocking(Task("TITLE2", "DESCRIPTION2"))

        launchActivity()

        // Open it in details view
        onView(withText("TITLE1")).perform(click())

        // Click delete task in menu
        onView(withId(R.id.menu_delete)).perform(click())

        // Verify it was deleted
        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_all)).perform(click())
        onView(withText("TITLE1")).check(doesNotExist())
        // but not the other one
        onView(withText("TITLE2")).check(matches(isDisplayed()))
    }

    @Test
    fun markTaskAsComplete() {
        repository.saveTaskBlocking(Task("TITLE1", "DESCRIPTION1"))

        launchActivity()

        // Mark the task as complete
        onView(checkboxWithText("TITLE1")).perform(click())

        // Verify task is shown as complete
        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_all)).perform(click())
        onView(withText("TITLE1")).check(matches(isDisplayed()))
        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_active)).perform(click())
        onView(withText("TITLE1")).check(matches(not(isDisplayed())))
        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_completed)).perform(click())
        onView(withText("TITLE1")).check(matches(isDisplayed()))
    }

    @Test
    fun markTaskAsActive() {
        repository.saveTaskBlocking(Task("TITLE1", "DESCRIPTION1", true))

        launchActivity()

        // Mark the task as active
        onView(checkboxWithText("TITLE1")).perform(click())

        // Verify task is shown as active
        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_all)).perform(click())
        onView(withText("TITLE1")).check(matches(isDisplayed()))
        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_active)).perform(click())
        onView(withText("TITLE1")).check(matches(isDisplayed()))
        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_completed)).perform(click())
        onView(withText("TITLE1")).check(matches(not(isDisplayed())))
    }

    @Test
    fun showAllTasks() {
        // Add one active task and one completed task
        repository.saveTaskBlocking(Task("TITLE1", "DESCRIPTION1"))
        repository.saveTaskBlocking(Task("TITLE2", "DESCRIPTION2", true))

        launchActivity()

        // Verify that both of our tasks are shown
        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_all)).perform(click())
        onView(withText("TITLE1")).check(matches(isDisplayed()))
        onView(withText("TITLE2")).check(matches(isDisplayed()))
    }

    @Test
    fun showActiveTasks() {
        // Add 2 active tasks and one completed task
        repository.saveTaskBlocking(Task("TITLE1", "DESCRIPTION1"))
        repository.saveTaskBlocking(Task("TITLE2", "DESCRIPTION2"))
        repository.saveTaskBlocking(Task("TITLE3", "DESCRIPTION3", true))

        launchActivity()

        // Verify that the active tasks (but not the completed task) are shown
        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_active)).perform(click())
        onView(withText("TITLE1")).check(matches(isDisplayed()))
        onView(withText("TITLE2")).check(matches(isDisplayed()))
        onView(withText("TITLE3")).check(doesNotExist())
    }

    @Test
    fun showCompletedTasks() {
        // Add one active task and 2 completed tasks
        repository.saveTaskBlocking(Task("TITLE1", "DESCRIPTION1"))
        repository.saveTaskBlocking(Task("TITLE2", "DESCRIPTION2", true))
        repository.saveTaskBlocking(Task("TITLE3", "DESCRIPTION3", true))

        launchActivity()

        // Verify that the completed tasks (but not the active task) are shown
        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_completed)).perform(click())
        onView(withText("TITLE1")).check(doesNotExist())
        onView(withText("TITLE2")).check(matches(isDisplayed()))
        onView(withText("TITLE3")).check(matches(isDisplayed()))
    }

    @Test
    fun clearCompletedTasks() {
        // Add one active task and one completed task
        repository.saveTaskBlocking(Task("TITLE1", "DESCRIPTION1"))
        repository.saveTaskBlocking(Task("TITLE2", "DESCRIPTION2", true))

        launchActivity()

        // Click clear completed in menu
        openActionBarOverflowOrOptionsMenu(getApplicationContext())
        onView(withText(R.string.menu_clear)).perform(click())

        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_all)).perform(click())
        // Verify that only the active task is shown
        onView(withText("TITLE1")).check(matches(isDisplayed()))
        onView(withText("TITLE2")).check(doesNotExist())
    }

    @Test
    fun noTasks_AllTasksFilter_AddTaskViewVisible() {
        launchActivity()

        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_all)).perform(click())

        // Verify the "You have no tasks!" text is shown
        onView(withText("You have no tasks!")).check(matches(isDisplayed()))
    }

    @Test
    fun noTasks_CompletedTasksFilter_AddTaskViewNotVisible() {
        launchActivity()

        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_completed)).perform(click())

        // Verify the "You have no completed tasks!" text is shown
        onView(withText("You have no completed tasks!")).check(matches((isDisplayed())))
    }

    @Test
    fun noTasks_ActiveTasksFilter_AddTaskViewNotVisible() {
        launchActivity()

        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_active)).perform(click())

        // Verify the "You have no active tasks!" text is shown
        onView(withText("You have no active tasks!")).check(matches((isDisplayed())))
    }

    @Test
    fun clickAddTaskButton_navigateToAddEditFragment() {
        // GIVEN - On the home screen
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<TasksFragment>(Bundle(), R.style.AppTheme) {
            Navigation.setViewNavController(this.view!!, navController)
        }

        // WHEN - Click on the "+" button
        onView(withId(R.id.add_task_fab)).perform(click())

        // THEN - Verify that we navigate to the add screen
        verify(navController).navigate(
            TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(
                null, getApplicationContext<Context>().getString(R.string.add_task)
            )
        )
    }

    private fun launchActivity(): ActivityScenario<TasksActivity>? {
        val activityScenario = launch(TasksActivity::class.java)
        activityScenario.onActivity { activity ->
            // Disable animations in RecyclerView
            (activity.findViewById(R.id.tasks_list) as RecyclerView).itemAnimator = null
        }
        return activityScenario
    }

    private fun checkboxWithText(text: String): Matcher<View> {
        return allOf(withId(R.id.complete_checkbox), hasSibling(withText(text)))
    }
}
