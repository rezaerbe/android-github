package com.erbe.github.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.erbe.github.vo.Contributor
import com.erbe.github.vo.Repo
import com.erbe.github.vo.RepoSearchResult
import com.erbe.github.vo.User

/**
 * Main database description.
 */
@Database(
    entities = [
        User::class,
        Repo::class,
        Contributor::class,
        RepoSearchResult::class],
    version = 3,
    exportSchema = false
)
abstract class GithubDb : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun repoDao(): RepoDao
}
