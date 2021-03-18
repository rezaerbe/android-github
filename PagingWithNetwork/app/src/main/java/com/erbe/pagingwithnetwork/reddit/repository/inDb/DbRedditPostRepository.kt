package com.erbe.pagingwithnetwork.reddit.repository.inDb

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.erbe.pagingwithnetwork.lib.reddit.repository.RedditPostRepository
import com.erbe.pagingwithnetwork.reddit.api.RedditApi
import com.erbe.pagingwithnetwork.reddit.db.RedditDb

/**
 * Repository implementation that uses a database backed [androidx.paging.PagingSource] and
 * [androidx.paging.RemoteMediator] to load pages from network when there are no more items cached
 * in the database to load.
 */
class DbRedditPostRepository(val db: RedditDb, val redditApi: RedditApi) : RedditPostRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun postsOfSubreddit(subReddit: String, pageSize: Int) = Pager(
        config = PagingConfig(pageSize),
        remoteMediator = PageKeyedRemoteMediator(db, redditApi, subReddit)
    ) {
        db.posts().postsBySubreddit(subReddit)
    }.flow
}
