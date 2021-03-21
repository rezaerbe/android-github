package com.erbe.architecture.di

import com.erbe.architecture.data.source.DefaultTasksRepository
import com.erbe.architecture.data.source.FakeRepository
import com.erbe.architecture.data.source.TasksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

/**
 * TasksRepository binding to use in tests.
 *
 * Hilt will inject a [FakeRepository] instead of a [DefaultTasksRepository].
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [TasksRepositoryModule::class]
)
abstract class TestTasksRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindRepository(repo: FakeRepository): TasksRepository
}
