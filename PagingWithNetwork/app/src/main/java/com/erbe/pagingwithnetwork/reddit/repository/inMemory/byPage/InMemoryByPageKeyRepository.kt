package com.erbe.pagingwithnetwork.reddit.repository.inMemory.byPage

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.erbe.pagingwithnetwork.lib.reddit.repository.RedditPostRepository
import com.erbe.pagingwithnetwork.reddit.api.RedditApi

/**
 * Repository implementation that loads data directly from network by using the previous / next page
 * keys returned in the query.
 */
class InMemoryByPageKeyRepository(private val redditApi: RedditApi) : RedditPostRepository {
    override fun postsOfSubreddit(subReddit: String, pageSize: Int) = Pager(
        PagingConfig(pageSize)
    ) {
        PageKeyedSubredditPagingSource(
            redditApi = redditApi,
            subredditName = subReddit
        )
    }.flow
}
