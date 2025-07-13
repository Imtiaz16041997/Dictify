package com.imtiaz.dictify

import android.annotation.SuppressLint
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.startup.Initializer
import androidx.work.Configuration
import androidx.work.WorkManager
import javax.inject.Inject

// This is required for Hilt to inject into Workers
@SuppressLint("EnsureInitializerNoArgConstr")
class HiltWorkManagerInitializer @Inject constructor(
    val workerFactory: HiltWorkerFactory
) : Initializer<WorkManager> {
    override fun create(context: Context): WorkManager {
        val configuration = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
        WorkManager.initialize(context, configuration)
        return WorkManager.getInstance(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}