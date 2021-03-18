package com.erbe.background.lib.imgur

import android.net.Uri
import com.erbe.background.lib.Constants
import okhttp3.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException

/**
 * The Imgur API client, which uses the [ImgurService] Retrofit APIs.
 */
class ImgurApi private constructor() {

    private val imgurService: ImgurService

    init {
        val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor())
                .build()
        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        imgurService = retrofit.create(ImgurService::class.java)
    }

    fun uploadImage(imageUri: Uri): Call<PostImageResponse> {
        val imageFile = File(imageUri.path!!)
        val requestFile = RequestBody.create(MEDIA_TYPE_PNG, imageFile)
        val body = MultipartBody.Part.createFormData("image", "image.png", requestFile)
        return imgurService.postImage(body)
    }

    private class AuthInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val headers = request.headers().newBuilder()
                    .add("Authorization", "Client-ID ${Constants.IMGUR_CLIENT_ID}")
                    .build()
            val authenticatedRequest = request.newBuilder().headers(headers).build()
            return chain.proceed(authenticatedRequest)
        }
    }

    companion object {

        private val MEDIA_TYPE_PNG = MediaType.parse("image/png")
        val instance: Lazy<ImgurApi> = lazy { ImgurApi() }
    }
}
