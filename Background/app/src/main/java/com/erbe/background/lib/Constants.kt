package com.erbe.background.lib

/**
 * Defines a list of constants used for [androidx.work.Worker] names, inputs & outputs.
 */
object Constants {

    // The name of the image manipulation work
    const val IMAGE_MANIPULATION_WORK_NAME = "image_manipulation_work"

    // Other keys
    const val OUTPUT_PATH = "demo_filter_outputs"
    const val BASE_URL = "https://api.imgur.com/3/"
    const val KEY_IMAGE_URI = "KEY_IMAGE_URI"

    const val TAG_OUTPUT = "OUTPUT"

    // Provide your own clientId to test Imgur uploads.
    const val IMGUR_CLIENT_ID = ""
}
