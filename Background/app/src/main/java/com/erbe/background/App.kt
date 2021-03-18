package com.erbe.background

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager
import com.erbe.background.lib.workers.RenameWorkerFactory
import java.util.concurrent.Executors

/**
 * The [Application]. Responsible for initializing [WorkManager] in [Log.VERBOSE] mode.
 */
class App : Application(), Configuration.Provider {

    override fun getWorkManagerConfiguration() =
            Configuration.Builder().setExecutor(Executors.newSingleThreadExecutor())
                    .setWorkerFactory(RenameWorkerFactory())
                    .setMinimumLoggingLevel(Log.VERBOSE).build()
}
