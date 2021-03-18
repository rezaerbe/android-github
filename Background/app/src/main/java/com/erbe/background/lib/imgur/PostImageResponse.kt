package com.erbe.background.lib.imgur

import com.google.gson.annotations.SerializedName

/**
 * The Imgur API post image response.
 */
class PostImageResponse {

    @SerializedName("data")
    val data: UploadedImage? = null

    @SerializedName("success")
    val isSuccess: Boolean = false

    @SerializedName("status")
    val status: Int = 0

    class UploadedImage {
        @SerializedName("id")
        val id: String? = null

        @SerializedName("link")
        val link: String? = null
    }
}
