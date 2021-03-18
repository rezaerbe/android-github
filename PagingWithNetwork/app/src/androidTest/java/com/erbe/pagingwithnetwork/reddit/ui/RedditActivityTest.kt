package com.erbe.pagingwithnetwork.reddit.ui

import android.app.Application
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.erbe.pagingwithnetwork.R
import com.erbe.pagingwithnetwork.lib.reddit.repository.RedditPostRepository
import com.erbe.pagingwithnetwork.reddit.DefaultServiceLocator
import com.erbe.pagingwithnetwork.reddit.ServiceLocator
import com.erbe.pagingwithnetwork.reddit.api.RedditApi
import com.erbe.pagingwithnetwork.reddit.ui.SubRedditViewModel.Companion.DEFAULT_SUBREDDIT
import com.erbe.pagingwithnetwork.repository.FakeRedditApi
import com.erbe.pagingwithnetwork.repository.PostFactory
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Simple sanity test to ensure data is displayed
 */
@RunWith(Parameterized::class)
class RedditActivityTest(private val type: RedditPostRepository.Type) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun params() = arrayOf(
            RedditPostRepository.Type.IN_MEMORY_BY_ITEM,
            RedditPostRepository.Type.IN_MEMORY_BY_PAGE
        )
    }

    private val postFactory = PostFactory()

    @Before
    fun init() {
        val fakeApi = FakeRedditApi()
        fakeApi.addPost(postFactory.createRedditPost(DEFAULT_SUBREDDIT))
        fakeApi.addPost(postFactory.createRedditPost(DEFAULT_SUBREDDIT))
        fakeApi.addPost(postFactory.createRedditPost(DEFAULT_SUBREDDIT))
        val app = ApplicationProvider.getApplicationContext<Application>()
        // use a controlled service locator w/ fake API
        ServiceLocator.swap(
            object : DefaultServiceLocator(app = app, useInMemoryDb = true) {
                override fun getRedditApi(): RedditApi = fakeApi
            }
        )
    }

    @Test
    fun showSomeResults() {
        ActivityScenario.launch<RedditActivity>(
            RedditActivity.intentFor(
                context = ApplicationProvider.getApplicationContext(),
                type = type
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )

        onView(withId(R.id.list)).check { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            val recyclerView = view as RecyclerView
            assertEquals(3, recyclerView.adapter?.itemCount)
        }
    }
}