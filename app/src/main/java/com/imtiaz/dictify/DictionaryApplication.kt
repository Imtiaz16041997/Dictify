package com.imtiaz.dictify

import androidx.hilt.work.HiltWorkerFactory
import androidx.multidex.MultiDexApplication
import androidx.work.BackoffPolicy
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest // <--- Make sure this is imported if not already
import com.imtiaz.dictify.worker.RandomWordWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class DictionaryApplication : MultiDexApplication() , Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG) // Optional: for debugging workmanager
            .build()

    override fun onCreate() {
        super.onCreate()
        scheduleDailyWordUpdates()
    }

    private fun scheduleDailyWordUpdates() {
        val constraints = androidx.work.Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Requires internet for API calls
            .build()

        // Schedule to run every 8 hours (3 times a day)
        val dailyWordRequest = PeriodicWorkRequest.Builder(
            RandomWordWorker::class.java,
            15, TimeUnit.MINUTES // Remember: Minimum interval for PeriodicWorkRequest is 15 minutes
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                WorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS, // <--- CORRECTED LINE HERE
                TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "RandomWordInsightWork", // A unique name for your work
            ExistingPeriodicWorkPolicy.UPDATE, // Replace any existing work with this name
            dailyWordRequest
        )
        // Log.d("DictionaryApplication", "Scheduled RandomWordInsightWork") // For debugging
    }
}