package com.erbe.pagingwithnetwork.reddit.repository.inMemory.byPage

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadParams.Append
import androidx.paging.PagingSource.LoadParams.Prepend
import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.PagingState
import com.erbe.pagingwithnetwork.lib.reddit.vo.RedditPost
import com.erbe.pagingwithnetwork.reddit.api.RedditApi
import com.erbe.pagingwithnetwork.reddit.repository.inMemory.byItem.ItemKeyedSubredditPagingSource
import retrofit2.HttpException
import java.io.IOException

/**
 * A [PagingSource] that uses the before/after keys returned in page requests.
 *
 * @see ItemKeyedSubredditPagingSource
 */
class PageKeyedSubredditPagingSource(
    private val redditApi: RedditApi,
    private val subredditName: String
) : PagingSource<String, RedditPost>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, RedditPost> {
        return try {
            val data = redditApi.getTop(
                subreddit = subredditName,
                after = if (params is Append) params.key else null,
                before = if (params is Prepend) params.key else null,
                limit = params.loadSize
            ).data

            Page(
                data = data.children.map { it.data },
                prevKey = data.before,
                nextKey = data.after
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, RedditPost>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}