package com.erbe.background

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.erbe.background.lib.Constants
import com.erbe.background.lib.ImageOperations

/**
 * A [ViewModel] for [FilterActivity].
 *
 * Keeps track of pending image filter operations.
 */
class FilterViewModel(application: Application) : AndroidViewModel(application) {
    private val mWorkManager = WorkManager.getInstance(application)

    internal val outputStatus: LiveData<List<WorkInfo>>
        get() = mWorkManager.getWorkInfosByTagLiveData(Constants.TAG_OUTPUT)

    internal fun apply(imageOperations: ImageOperations) {
        imageOperations.continuation.enqueue()
    }

    internal fun cancel() {
        mWorkManager.cancelUniqueWork(Constants.IMAGE_MANIPULATION_WORK_NAME)
    }
}