package com.erbe.background.lib.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.erbe.background.lib.Constants
import java.text.SimpleDateFormat
import java.util.*

/**
 * Saves an output image to the [MediaStore].
 */
class SaveImageToGalleryWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val resolver = applicationContext.contentResolver
        return try {
            val resourceUri = Uri.parse(inputData.getString(Constants.KEY_IMAGE_URI))
            val bitmap = BitmapFactory.decodeStream(resolver.openInputStream(resourceUri))
            val imageUrl = MediaStore.Images.Media.insertImage(
                resolver, bitmap, DATE_FORMATTER.format(Date()), TITLE
            )
            if (imageUrl.isEmpty()) {
                Log.e(TAG, "Writing to MediaStore failed")
                Result.failure()
            }
            // Set the result of the worker by calling setOutputData().
            val output = Data.Builder()
                .putString(Constants.KEY_IMAGE_URI, imageUrl)
                .build()
            Result.success(output)
        } catch (exception: Exception) {
            Log.e(TAG, "Unable to save image to Gallery", exception)
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "SvImageToGalleryWrkr"
        private const val TITLE = "Filtered Image"
        private val DATE_FORMATTER =
            SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z", Locale.getDefault())
    }
}
