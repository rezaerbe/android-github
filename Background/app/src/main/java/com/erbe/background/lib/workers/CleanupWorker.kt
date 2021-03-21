package com.erbe.background.lib.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.erbe.background.lib.Constants
import java.io.File

/** Clears temporary files. */
class CleanupWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        return try {
            cleanupDirectory()
            Result.success()
        } catch (exception: Exception) {
            Log.e(TAG, "Error cleaning up", exception)
            Result.failure()
        }
    }

    /** Removes pngs from the app's files directory */
    private fun cleanupDirectory() {
        File(applicationContext.filesDir, Constants.OUTPUT_PATH).apply {
            if (exists()) {
                listFiles()?.forEach { file ->
                    if (file.name.endsWith(".png")) {
                        val deleted = file.delete()
                        Log.i(TAG, "Deleted ${file.name} - $deleted")
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "CleanupWorker"
    }
}
