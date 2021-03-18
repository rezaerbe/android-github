package com.erbe.github.di

import com.erbe.github.ui.repo.RepoFragment
import com.erbe.github.ui.search.SearchFragment
import com.erbe.github.ui.user.UserFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeRepoFragment(): RepoFragment

    @ContributesAndroidInjector
    abstract fun contributeUserFragment(): UserFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment
}
