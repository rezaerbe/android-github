package com.erbe.pagingwithnetwork.reddit.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.erbe.pagingwithnetwork.lib.reddit.vo.RedditPost

@Dao
interface RedditPostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<RedditPost>)

    @Query("SELECT * FROM posts WHERE subreddit = :subreddit ORDER BY indexInResponse ASC")
    fun postsBySubreddit(subreddit: String): PagingSource<Int, RedditPost>

    @Query("DELETE FROM posts WHERE subreddit = :subreddit")
    suspend fun deleteBySubreddit(subreddit: String)

    @Query("SELECT MAX(indexInResponse) + 1 FROM posts WHERE subreddit = :subreddit")
    suspend fun getNextIndexInSubreddit(subreddit: String): Int
}