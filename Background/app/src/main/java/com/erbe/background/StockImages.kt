package com.erbe.background

import android.net.Uri
import java.util.*

/**
 * Helps produce a random stock image [Uri].
 */
internal object StockImages {

    private val random = Random()
    private val imageUris = arrayOf(
            Uri.parse("file:///android_asset/images/lit_pier.jpg"),
            Uri.parse("file:///android_asset/images/parting_ways.jpg"),
            Uri.parse("file:///android_asset/images/wrong_way.jpg"),
            Uri.parse("file:///android_asset/images/jack_beach.jpg"),
            Uri.parse("file:///android_asset/images/jack_blur.jpg"),
            Uri.parse("file:///android_asset/images/jack_road.jpg"))

    /**
     * This method produces a random image [Uri]. This is so you can see
     * the effects of applying filters on different kinds of stock images.
     *
     * @return a random stock image [Uri].
     */
    fun randomStockImage(): Uri {
        val index = random.nextInt(imageUris.size)
        return imageUris[index]
    }
}
