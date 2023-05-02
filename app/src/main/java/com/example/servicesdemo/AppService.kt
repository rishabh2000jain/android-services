package com.example.servicesdemo

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.milliseconds

class AppService : Service() {
    private val mServiceJob = SupervisorJob()
    private val mServiceScope = CoroutineScope(mServiceJob + Dispatchers.IO)
    private var mGeneratedNumber = 0
    inner class MyBinder : Binder() {
        fun getService(): AppService {
            return this@AppService
        }
    }

    fun getNumber():Int{
        return mGeneratedNumber
    }

    private val binder = MyBinder()

    companion object {
        const val TAG = "AppService"
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand Called")
        mServiceScope.launch {
            startNumberGenerator()
            stopSelf()
        }
        return START_REDELIVER_INTENT
    }

    private suspend fun startNumberGenerator() {
        for (i in 1..200) {
            mGeneratedNumber = i
            delay(1000.milliseconds)
            Log.d(TAG, "Number Generated $i")
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mServiceJob.cancel()
        Log.d(TAG, "onDestroy Called")
    }


}