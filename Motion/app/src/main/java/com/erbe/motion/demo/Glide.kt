package com.erbe.motion.demo

import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

/**
 * Executes the specified [body] when the request is complete. It is invoked no matter whether the
 * request succeeds or fails.
 */
fun <T> RequestBuilder<T>.doOnEnd(body: () -> Unit): RequestBuilder<T> {
    return addListener(object : RequestListener<T> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<T>?,
            isFirstResource: Boolean
        ): Boolean {
            body()
            return false
        }

        override fun onResourceReady(
            resource: T,
            model: Any?,
            target: Target<T>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            body()
            return false
        }
    })
}
