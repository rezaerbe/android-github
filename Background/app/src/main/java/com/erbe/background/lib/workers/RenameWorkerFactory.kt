package com.erbe.background.lib.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters

class RenameWorkerFactory : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        return when (workerClassName) {
            "RxCleanupWorker" -> CleanupWorker(appContext, workerParameters)
            else -> null
        }
    }
}
