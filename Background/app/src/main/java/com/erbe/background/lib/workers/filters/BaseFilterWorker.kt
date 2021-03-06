package com.erbe.background.lib.workers.filters

import android.R.drawable
import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.Log
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.core.app.NotificationCompat.Builder
import androidx.work.*
import com.erbe.background.R
import com.erbe.background.lib.Constants
import java.io.*
import java.util.*

abstract class BaseFilterWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        val resourceUri = inputData.getString(Constants.KEY_IMAGE_URI)
            ?: throw IllegalArgumentException("Invalid input uri")
        return try {
            setForeground(createForegroundInfo())
            val inputStream = inputStreamFor(applicationContext, resourceUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val output = applyFilter(bitmap)
            // write bitmap to a file and set the output
            val outputUri = writeBitmapToFile(output)
            Result.success(workDataOf(Constants.KEY_IMAGE_URI to outputUri.toString()))
        } catch (fileNotFoundException: FileNotFoundException) {
            Log.e(TAG, "Failed to decode input stream", fileNotFoundException)
            throw RuntimeException("Failed to decode input stream", fileNotFoundException)
        } catch (throwable: Throwable) {
            Log.e(TAG, "Error applying filter", throwable)
            Result.failure()
        }
    }

    abstract fun applyFilter(input: Bitmap): Bitmap

    /**
     * Writes a given [Bitmap] to the [Context.getFilesDir] directory.
     *
     * @param bitmap the [Bitmap] which needs to be written to the files directory.
     * @return a [Uri] to the output [Bitmap].
     */
    private fun writeBitmapToFile(
        bitmap: Bitmap
    ): Uri {

        // Bitmaps are being written to a temporary directory. This is so they can serve as inputs
        // for workers downstream, via Worker chaining.
        val name = "filter-output-${UUID.randomUUID()}.png"
        val outputDir = File(applicationContext.filesDir, Constants.OUTPUT_PATH)
        if (!outputDir.exists()) {
            outputDir.mkdirs() // should succeed
        }
        val outputFile = File(outputDir, name)
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(outputFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* ignored for PNG */, out)
        } finally {
            if (out != null) {
                try {
                    out.close()
                } catch (ignore: IOException) {
                }
            }
        }
        return Uri.fromFile(outputFile)
    }

    /**
     * Create ForegroundInfo required to run a Worker in a foreground service.
     */
    private fun createForegroundInfo(): ForegroundInfo {
        // For a real world app you might want to use a different id for each Notification.
        val notificationId = 1
        return ForegroundInfo(notificationId, createNotification())
    }

    /**
     * Create the notification and required channel (O+) for running work in a foreground service.
     */
    private fun createNotification(): Notification {
        val channelId = getString(R.string.notification_channel_id)
        val title = getString(R.string.notification_title)
        val cancel = getString(R.string.cancel_processing)
        val name = getString(R.string.channel_name)
        // This PendingIntent can be used to cancel the Worker.
        val intent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(id)

        val builder = Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setTicker(title)
            .setSmallIcon(R.drawable.baseline_gradient)
            .setOngoing(true)
            .addAction(drawable.ic_delete, cancel, intent)
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            createNotificationChannel(channelId, name).also {
                builder.setChannelId(it.id)
            }
        }
        return builder.build()
    }


    private fun getString(@StringRes id: Int) = applicationContext.getString(id)

    /**
     * Create the required notification channel for O+ devices.
     */
    @TargetApi(VERSION_CODES.O)
    private fun createNotificationChannel(
        channelId: String,
        name: String
    ): NotificationChannel {
        return NotificationChannel(
            channelId, name, NotificationManager.IMPORTANCE_LOW
        ).also { channel ->
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val TAG = "BaseFilterWorker"
        const val ASSET_PREFIX = "file:///android_asset/"

        /**
         * Creates an input stream which can be used to read the given `resourceUri`.
         *
         * @param context the application [Context].
         * @param resourceUri the [String] resourceUri.
         * @return the [InputStream] for the resourceUri.
         */
        @VisibleForTesting
        fun inputStreamFor(
            context: Context,
            resourceUri: String
        ): InputStream? {

            // If the resourceUri is an Android asset URI, then use AssetManager to get a handle to
            // the input stream. (Stock Images are Asset URIs).
            return if (resourceUri.startsWith(ASSET_PREFIX)) {
                val assetManager = context.resources.assets
                assetManager.open(resourceUri.substring(ASSET_PREFIX.length))
            } else {
                // Not an Android asset Uri. Use a ContentResolver to get a handle to the input stream.
                val resolver = context.contentResolver
                resolver.openInputStream(Uri.parse(resourceUri))
            }
        }
    }
}
