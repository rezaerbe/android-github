package com.erbe.pagingwithnetwork.reddit

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import com.erbe.pagingwithnetwork.lib.reddit.repository.RedditPostRepository
import com.erbe.pagingwithnetwork.reddit.api.RedditApi
import com.erbe.pagingwithnetwork.reddit.db.RedditDb
import com.erbe.pagingwithnetwork.reddit.repository.inDb.DbRedditPostRepository
import com.erbe.pagingwithnetwork.reddit.repository.inMemory.byItem.InMemoryByItemRepository
import com.erbe.pagingwithnetwork.reddit.repository.inMemory.byPage.InMemoryByPageKeyRepository

/**
 * Super simplified service locator implementation to allow us to replace default implementations
 * for testing.
 */
interface ServiceLocator {
    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null
        fun instance(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator(
                        app = context.applicationContext as Application,
                        useInMemoryDb = false
                    )
                }
                return instance!!
            }
        }

        /**
         * Allows tests to replace the default implementations.
         */
        @VisibleForTesting
        fun swap(locator: ServiceLocator) {
            instance = locator
        }
    }

    fun getRepository(type: RedditPostRepository.Type): RedditPostRepository

    fun getRedditApi(): RedditApi
}

/**
 * default implementation of ServiceLocator that uses production endpoints.
 */
open class DefaultServiceLocator(val app: Application, val useInMemoryDb: Boolean) :
    ServiceLocator {
    private val db by lazy {
        RedditDb.create(app, useInMemoryDb)
    }

    private val api by lazy {
        RedditApi.create()
    }

    override fun getRepository(type: RedditPostRepository.Type): RedditPostRepository {
        return when (type) {
            RedditPostRepository.Type.IN_MEMORY_BY_ITEM -> InMemoryByItemRepository(
                redditApi = getRedditApi()
            )
            RedditPostRepository.Type.IN_MEMORY_BY_PAGE -> InMemoryByPageKeyRepository(
                redditApi = getRedditApi()
            )
            RedditPostRepository.Type.DB -> DbRedditPostRepository(
                db = db,
                redditApi = getRedditApi()
            )
        }
    }

    override fun getRedditApi(): RedditApi = api
}