package com.erbe.architecture.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.erbe.architecture.data.Result
import com.erbe.architecture.data.Result.Error
import com.erbe.architecture.data.Result.Success
import com.erbe.architecture.data.Task
import com.erbe.architecture.data.source.TasksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the statistics screen.
 */
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val tasks: LiveData<Result<List<Task>>> = tasksRepository.observeTasks()
    private val _dataLoading = MutableLiveData<Boolean>(false)
    private val stats: LiveData<StatsResult?> = tasks.map {
        if (it is Success) {
            getActiveAndCompletedStats(it.data)
        } else {
            null
        }
    }

    val activeTasksPercent = stats.map {
        it?.activeTasksPercent ?: 0f }
    val completedTasksPercent: LiveData<Float> = stats.map { it?.completedTasksPercent ?: 0f }
    val dataLoading: LiveData<Boolean> = _dataLoading
    val error: LiveData<Boolean> = tasks.map { it is Error }
    val empty: LiveData<Boolean> = tasks.map { (it as? Success)?.data.isNullOrEmpty() }

    fun refresh() {
        _dataLoading.value = true
        viewModelScope.launch {
            tasksRepository.refreshTasks()
            _dataLoading.value = false
        }
    }
}
