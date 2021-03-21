package com.erbe.architecture

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.erbe.architecture.data.Result
import com.erbe.architecture.data.Task
import com.erbe.architecture.data.source.TasksDataSource

object FakeFailingTasksRemoteDataSource : TasksDataSource {
    override suspend fun getTasks(): Result<List<Task>> {
        return Result.Error(Exception("Test"))
    }

    override suspend fun getTask(taskId: String): Result<Task> {
        return Result.Error(Exception("Test"))
    }

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        return liveData { emit(getTasks()) }
    }

    override suspend fun refreshTasks() {
        TODO("not implemented")
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        return liveData { emit(getTask(taskId)) }
    }

    override suspend fun refreshTask(taskId: String) {
        TODO("not implemented")
    }

    override suspend fun saveTask(task: Task) {
        TODO("not implemented")
    }

    override suspend fun completeTask(task: Task) {
        TODO("not implemented")
    }

    override suspend fun completeTask(taskId: String) {
        TODO("not implemented")
    }

    override suspend fun activateTask(task: Task) {
        TODO("not implemented")
    }

    override suspend fun activateTask(taskId: String) {
        TODO("not implemented")
    }

    override suspend fun clearCompletedTasks() {
        TODO("not implemented")
    }

    override suspend fun deleteAllTasks() {
        TODO("not implemented")
    }

    override suspend fun deleteTask(taskId: String) {
        TODO("not implemented")
    }
}
