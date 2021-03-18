package com.erbe.background.lib.workers

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.erbe.background.lib.Constants
import com.erbe.background.lib.imgur.ImgurApi

/**
 * Uploads an image to Imgur using the [ImgurApi].
 */
class UploadWorker(appContext: Context, workerParams: WorkerParameters) :
        Worker(appContext, workerParams) {

    override fun doWork(): Result {
        var imageUriInput: String? = null
        return try {
            val args = inputData
            imageUriInput = args.getString(Constants.KEY_IMAGE_URI)
            val imageUri = Uri.parse(imageUriInput)
            val imgurApi = ImgurApi.instance.value
            // Upload the image to Imgur.
            val response = imgurApi.uploadImage(imageUri).execute()
            // Check to see if the upload succeeded.
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()
                val error = errorBody?.string()
                toastAndLog("Request failed $imageUriInput ($error)")
                Result.failure()
            } else {
                val imageResponse = response.body()
                var outputData = workDataOf()
                if (imageResponse != null) {
                    val imgurLink = imageResponse.data!!.link
                    // Set the result of the worker by calling setOutputData().
                    outputData = Data.Builder()
                            .putString(Constants.KEY_IMAGE_URI, imgurLink)
                            .build()
                }
                Result.success(outputData)
            }
        } catch (e: Exception) {
            toastAndLog("Failed to upload image with URI $imageUriInput")
            Result.failure()
        }
    }

    private fun toastAndLog(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        Log.e(TAG, message)
    }

    companion object {
        private const val TAG = "UploadWorker"
    }
}
