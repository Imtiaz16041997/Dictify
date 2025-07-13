package com.imtiaz.dictify.worker

import android.content.Context
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.imtiaz.dictify.R
import com.imtiaz.dictify.data.local.dao.DictionaryDao
import com.imtiaz.dictify.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.io.IOException
import retrofit2.HttpException
import android.app.NotificationChannel
import android.os.Build
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.imtiaz.dictify.data.dataSource.remote.ApiService
import com.imtiaz.dictify.data.remote.RandomWordApiService
import com.imtiaz.dictify.data.local.entity.DailyWordEntity

@HiltWorker
class RandomWordWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val randomWordApiService: RandomWordApiService, // Inject the new random word API service
    private val apiService: ApiService, // Inject the existing dictionary API service
    private val dictionaryDao: DictionaryDao
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "word_insight_channel"
        const val NOTIFICATION_CHANNEL_NAME = "Word Insights"
        const val NOTIFICATION_ID = 1001
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // 1. Fetch a random word
            val randomWords = randomWordApiService.getRandomWord()
            val randomWord = randomWords.firstOrNull() // Get the single word from the list

            if (randomWord.isNullOrBlank()) {
                // If random word API returns nothing, try to use last stored word
                return@withContext handleApiFailure("No random word found from API.")
            }

            // 2. Fetch the definition for the random word using your existing dictionary API
            val wordResponses = apiService.findingWordTranslation(randomWord)
            val wordResponse = wordResponses.firstOrNull() // Get the first response if multiple

            if (wordResponse != null && wordResponse.word != null) {
                val word = wordResponse.word
                val definition = wordResponse.meanings.firstOrNull()?.definitions?.firstOrNull()?.definition
                val partOfSpeech = wordResponse.meanings.firstOrNull()?.partOfSpeech
                val audioUrl = wordResponse.phonetics.firstOrNull { it.audio?.isNotBlank() == true }?.audio

                // 3. Store in Room database
                val dailyWordEntity = DailyWordEntity(
                    word = word,
                    definition = definition,
                    partOfSpeech = partOfSpeech,
                    audioUrl = audioUrl,
                    fetchedDate = System.currentTimeMillis()
                )
                dictionaryDao.insertDailyWord(dailyWordEntity)

                // 4. Display Notification
                showNotification(dailyWordEntity)
                Result.success()
            } else {
                handleApiFailure("No definition found for '$randomWord'.")
            }
        } catch (e: HttpException) {
            handleApiFailure("HTTP Error: ${e.code()}. Message: ${e.message()}", e)
        } catch (e: IOException) {
            handleApiFailure("Network Error: ${e.localizedMessage}", e, shouldRetry = true)
        } catch (e: Exception) {
            handleApiFailure("An unexpected error occurred: ${e.localizedMessage}", e)
        }
    }

    private suspend fun handleApiFailure(
        logMessage: String,
        exception: Exception? = null,
        shouldRetry: Boolean = false
    ): Result {
        Log.e("RandomWordWorker", logMessage, exception)
        val lastWord = dictionaryDao.getLatestDailyWord().firstOrNull() // Blocking call, but OK for worker

        if (lastWord != null) {
            showNotification(lastWord) // Show the last successful word if available
            return Result.success() // Still consider it a success as we showed something
        } else {
            showErrorNotification("Word Insight Failed", logMessage)
            return if (shouldRetry) Result.retry() else Result.failure()
        }
    }

    private fun showNotification(dailyWord: DailyWordEntity) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)

        /*val pendingIntent = Intent(applicationContext, MainActivity::class.java).apply {
            putExtra("word_item", dailyWord.word)
            action = Intent.ACTION_VIEW
        }.let { intent ->
            PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }*/

        val pendingIntent = Intent(applicationContext, MainActivity::class.java).apply {
            putExtra("word_item", dailyWord.word)
            putExtra("notification_definition", dailyWord.definition)       // <-- NEW
            putExtra("notification_part_of_speech", dailyWord.partOfSpeech) // <-- NEW
            putExtra("from_notification_click", true)                      // <-- NEW flag
            action = Intent.ACTION_VIEW
            // Consider adding flags for singleTop or clearTask if your app's navigation requires
            // addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }.let { intent ->
            PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Word Insight: ${dailyWord.word}")
            .setContentText(dailyWord.definition ?: "No definition available.")
            .setStyle(NotificationCompat.BigTextStyle().bigText(dailyWord.definition ?: "No definition available."))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun showErrorNotification(title: String, message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)

        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID + 1, notification) // Use a different ID for error notifications
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for daily word insights."
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}