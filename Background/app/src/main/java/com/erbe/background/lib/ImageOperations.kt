package com.erbe.background.lib

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.work.*
import com.erbe.background.lib.workers.CleanupWorker
import com.erbe.background.lib.workers.SaveImageToGalleryWorker
import com.erbe.background.lib.workers.UploadWorker
import com.erbe.background.lib.workers.filters.BlurEffectFilterWorker
import com.erbe.background.lib.workers.filters.GrayScaleFilterWorker
import com.erbe.background.lib.workers.filters.WaterColorFilterWorker

/**
 * Builds and holds WorkContinuation based on supplied filters.
 */
@SuppressLint("EnqueueWork")
class ImageOperations(
    context: Context,
    imageUri: Uri,
    waterColor: Boolean = false,
    grayScale: Boolean = false,
    blur: Boolean = false,
    save: Boolean = false
) {

    private val imageInputData = workDataOf(Constants.KEY_IMAGE_URI to imageUri.toString())
    val continuation: WorkContinuation

    init {
        continuation = WorkManager.getInstance(context)
            .beginUniqueWork(
                Constants.IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanupWorker::class.java)
            ).thenMaybe<WaterColorFilterWorker>(waterColor)
            .thenMaybe<GrayScaleFilterWorker>(grayScale)
            .thenMaybe<BlurEffectFilterWorker>(blur)
            .then(
                if (save) {
                    workRequest<SaveImageToGalleryWorker>(tag = Constants.TAG_OUTPUT)
                } else /* upload */ {
                    workRequest<UploadWorker>(tag = Constants.TAG_OUTPUT)
                }
            )
    }

    /**
     * Applies a [ListenableWorker] to a [WorkContinuation] in case [apply] is `true`.
     */
    private inline fun <reified T : ListenableWorker> WorkContinuation.thenMaybe(
        apply: Boolean
    ): WorkContinuation {
        return if (apply) {
            then(workRequest<T>())
        } else {
            this
        }
    }

    /**
     * Creates a [OneTimeWorkRequest] with the given inputData and a [tag] if set.
     */
    private inline fun <reified T : ListenableWorker> workRequest(
        inputData: Data = imageInputData,
        tag: String? = null
    ) =
        OneTimeWorkRequestBuilder<T>().apply {
            setInputData(inputData)
            if (!tag.isNullOrEmpty()) {
                addTag(tag)
            }
        }.build()
}
