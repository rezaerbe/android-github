package com.erbe.pagingwithnetwork.lib.reddit.repository

import androidx.paging.PagingData
import com.erbe.pagingwithnetwork.lib.reddit.vo.RedditPost
import kotlinx.coroutines.flow.Flow

/**
 * Common interface shared by the different repository implementations.
 * Note: this only exists for sample purposes - typically an app would implement a repo once, either
 * network+db, or network-only
 */
interface RedditPostRepository {
    fun postsOfSubreddit(subReddit: String, pageSize: Int): Flow<PagingData<RedditPost>>

    enum class Type {
        IN_MEMORY_BY_ITEM,
        IN_MEMORY_BY_PAGE,
        DB
    }
}