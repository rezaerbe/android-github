package com.erbe.architecture.taskdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.erbe.architecture.*
import com.erbe.architecture.data.Result.Success
import com.erbe.architecture.data.Task
import com.erbe.architecture.data.source.FakeRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for the implementation of [TaskDetailViewModel]
 */
@ExperimentalCoroutinesApi
class TaskDetailViewModelTest {

    // Subject under test
    private lateinit var taskDetailViewModel: TaskDetailViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var tasksRepository: FakeRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    val task = Task("Title1", "Description1")

    @Before
    fun setupViewModel() {
        tasksRepository = FakeRepository()
        tasksRepository.addTasks(task)

        taskDetailViewModel = TaskDetailViewModel(tasksRepository)
    }

    @Test
    fun getActiveTaskFromRepositoryAndLoadIntoView() {
        taskDetailViewModel.start(task.id)

        // Then verify that the view was notified
        assertThat(taskDetailViewModel.task.getOrAwaitValue()?.title).isEqualTo(task.title)
        assertThat(taskDetailViewModel.task.getOrAwaitValue()?.description)
            .isEqualTo(task.description)
    }

    @Test
    fun completeTask() {
        // Load the ViewModel
        taskDetailViewModel.start(task.id)
        // Start observing to compute transformations
        taskDetailViewModel.task.getOrAwaitValue()

        // Verify that the task was active initially
        assertThat(tasksRepository.tasksServiceData[task.id]?.isCompleted).isFalse()

        // When the ViewModel is asked to complete the task
        taskDetailViewModel.setCompleted(true)

        // Then the task is completed and the snackbar shows the correct message
        assertThat(tasksRepository.tasksServiceData[task.id]?.isCompleted).isTrue()
        assertSnackbarMessage(taskDetailViewModel.snackbarText, R.string.task_marked_complete)
    }

    @Test
    fun activateTask() {
        task.isCompleted = true

        // Load the ViewModel
        taskDetailViewModel.start(task.id)
        // Start observing to compute transformations
        taskDetailViewModel.task.observeForTesting {

            // Verify that the task was completed initially
            assertThat(tasksRepository.tasksServiceData[task.id]?.isCompleted).isTrue()

            // When the ViewModel is asked to complete the task
            taskDetailViewModel.setCompleted(false)

            mainCoroutineRule.runBlockingTest {
                // Then the task is not completed and the snackbar shows the correct message
                val newTask = (tasksRepository.getTask(task.id) as Success).data
                assertTrue(newTask.isActive)
                assertSnackbarMessage(taskDetailViewModel.snackbarText, R.string.task_marked_active)
            }
        }
    }

    @Test
    fun taskDetailViewModel_repositoryError() {
        // Given a repository that returns errors
        tasksRepository.setReturnError(true)

        // Given an initialized ViewModel with an active task
        taskDetailViewModel.start(task.id)
        // Get the computed LiveData value
        taskDetailViewModel.task.observeForTesting {
            // Then verify that data is not available
            assertThat(taskDetailViewModel.isDataAvailable.getOrAwaitValue()).isFalse()
        }
    }

    @Test
    fun updateSnackbar_nullValue() {
        // Before setting the Snackbar text, get its current value
        val snackbarText = taskDetailViewModel.snackbarText.value

        // Check that the value is null
        assertThat(snackbarText).isNull()
    }

    @Test
    fun clickOnEditTask_SetsEvent() {
        // When opening a new task
        taskDetailViewModel.editTask()

        // Then the event is triggered
        val value = taskDetailViewModel.editTaskEvent.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()).isNotNull()
    }

    @Test
    fun deleteTask() {
        assertThat(tasksRepository.tasksServiceData.containsValue(task)).isTrue()
        taskDetailViewModel.start(task.id)

        // When the deletion of a task is requested
        taskDetailViewModel.deleteTask()

        assertThat(tasksRepository.tasksServiceData.containsValue(task)).isFalse()
    }

    @Test
    fun loadTask_loading() {
        // Pause dispatcher so we can verify initial values
        mainCoroutineRule.pauseDispatcher()

        // Load the task in the viewmodel
        taskDetailViewModel.start(task.id)
        // Start observing to compute transformations
        taskDetailViewModel.task.observeForTesting {
            // Force a refresh to show the loading indicator
            taskDetailViewModel.refresh()

            // Then progress indicator is shown
            assertThat(taskDetailViewModel.dataLoading.getOrAwaitValue()).isTrue()

            // Execute pending coroutines actions
            mainCoroutineRule.resumeDispatcher()

            // Then progress indicator is hidden
            assertThat(taskDetailViewModel.dataLoading.getOrAwaitValue()).isFalse()
        }
    }
}
