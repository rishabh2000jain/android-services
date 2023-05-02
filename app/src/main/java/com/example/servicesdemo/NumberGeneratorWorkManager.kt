package com.example.servicesdemo

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.milliseconds


class NumberGeneratorWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private var notificationManager = AppNotificationManager(applicationContext)

    companion object {
        const val TAG = "NumberGeneratorJobIntentService"
    }




    override suspend fun doWork(): Result {
        try {
            notificationManager.registerNotificationChannelChannel("122","channel_work_manager_demo","Demo for work manager")
            setForegroundAsync(ForegroundInfo(12,notificationManager.getNotification()))
            for (i in 1..10) {
                delay(1000.milliseconds)
                Log.d(TAG, "Number Generated $i id ${workerParameters.id}")
            }
        }catch (e:CancellationException){
            Log.d(TAG, "Work Cancelled ${workerParameters.id}")
            return Result.failure()
        }
        return Result.success()
    }



}
