package com.erbe.pagingwithnetwork.reddit.repository.inMemory.byItem

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.erbe.pagingwithnetwork.lib.reddit.repository.RedditPostRepository
import com.erbe.pagingwithnetwork.reddit.api.RedditApi

/**
 * Repository implementation that that loads data directly from the network and uses the Item's name
 * as the key to discover prev/next pages.
 */
class InMemoryByItemRepository(private val redditApi: RedditApi) : RedditPostRepository {
    override fun postsOfSubreddit(subReddit: String, pageSize: Int) = Pager(
        PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false
        )
    ) {
        ItemKeyedSubredditPagingSource(
            redditApi = redditApi,
            subredditName = subReddit
        )
    }.flow
}
