package com.erbe.pagingwithnetwork.repository

import com.erbe.pagingwithnetwork.lib.reddit.vo.RedditPost
import java.util.concurrent.atomic.AtomicInteger

class PostFactory {
    private val counter = AtomicInteger(0)
    fun createRedditPost(subredditName: String): RedditPost {
        val id = counter.incrementAndGet()
        val post = RedditPost(
            name = "name_$id",
            title = "title $id",
            score = 10,
            author = "author $id",
            num_comments = 0,
            created = System.currentTimeMillis(),
            thumbnail = null,
            subreddit = subredditName,
            url = null
        )
        post.indexInResponse = -1
        return post
    }
}