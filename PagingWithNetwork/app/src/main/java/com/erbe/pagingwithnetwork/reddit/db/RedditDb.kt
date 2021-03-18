package com.erbe.pagingwithnetwork.reddit.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.erbe.pagingwithnetwork.lib.reddit.vo.RedditPost
import com.erbe.pagingwithnetwork.lib.reddit.vo.SubredditRemoteKey

/**
 * Database schema used by the DbRedditPostRepository
 */
@Database(
    entities = [RedditPost::class, SubredditRemoteKey::class],
    version = 1,
    exportSchema = false
)
abstract class RedditDb : RoomDatabase() {
    companion object {
        fun create(context: Context, useInMemory: Boolean): RedditDb {
            val databaseBuilder = if (useInMemory) {
                Room.inMemoryDatabaseBuilder(context, RedditDb::class.java)
            } else {
                Room.databaseBuilder(context, RedditDb::class.java, "reddit.db")
            }
            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun posts(): RedditPostDao
    abstract fun remoteKeys(): SubredditRemoteKeyDao
}