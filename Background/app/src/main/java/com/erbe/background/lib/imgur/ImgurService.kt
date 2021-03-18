package com.erbe.background.lib.imgur

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * A [retrofit2.Retrofit] interface to the Imgur postImage API.
 */
interface ImgurService {

    @Multipart
    @POST("image")
    fun postImage(@Part image: MultipartBody.Part): Call<PostImageResponse>
}
