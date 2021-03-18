package com.erbe.pagingwithnetwork.reddit.api

import android.util.Log
import com.erbe.pagingwithnetwork.lib.reddit.vo.RedditPost
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * API communication setup
 */
interface RedditApi {

    @GET("/r/{subreddit}/hot.json")
    suspend fun getTop(
        @Path("subreddit") subreddit: String,
        @Query("limit") limit: Int,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null
    ): ListingResponse

    class ListingResponse(val data: ListingData)

    class ListingData(
        val children: List<RedditChildrenResponse>,
        val after: String?,
        val before: String?
    )

    data class RedditChildrenResponse(val data: RedditPost)

    companion object {
        private const val BASE_URL = "https://www.reddit.com/"
        fun create(): RedditApi {
            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Log.d("API", it) })
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(HttpUrl.parse(BASE_URL)!!)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RedditApi::class.java)
        }
    }
}