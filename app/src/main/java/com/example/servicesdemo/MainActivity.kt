package com.example.servicesdemo

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import androidx.work.*
import com.example.servicesdemo.databinding.ActivityMainBinding
import java.time.Duration
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityMainBinding
    var mService: AppService? = null
    private var mServiceConnection: ServiceConnection? = null
    private var mServiceBound: Boolean = false
    private lateinit var workManager: WorkManager
    private lateinit var numberGeneratorWorkRequest: WorkRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        workManager = WorkManager.getInstance(applicationContext)
        val constraints = Constraints(NetworkType.CONNECTED)
        numberGeneratorWorkRequest =
            OneTimeWorkRequestBuilder<NumberGeneratorWorker>()
                .setConstraints(constraints)
                .setInitialDelay(2,TimeUnit.SECONDS)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 20, TimeUnit.SECONDS)
                .build()
        val serviceIntent = Intent(applicationContext, AppService::class.java)
        mBinding.startBtn.setOnClickListener {
            startService(serviceIntent)
        }
        mBinding.stopBtn.setOnClickListener {
            stopService(serviceIntent)
        }
        mBinding.bindService.setOnClickListener {
            mServiceConnection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    mService = (service as AppService.MyBinder).getService()
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    mService = null
                }
            }
            mServiceBound = true
            bindService(serviceIntent, mServiceConnection!!, Service.BIND_AUTO_CREATE)
        }
        mBinding.unbindService.setOnClickListener {
            if (mServiceConnection != null && mServiceBound) {
                unbindService(mServiceConnection!!)
                mServiceBound = false
            }
        }
        mBinding.getNumberBtn.setOnClickListener {
            updateNumberOnUI((mService?.getNumber() ?: 0))
        }
        mBinding.startWork.setOnClickListener {

            runWorker()
        }
        mBinding.stopWork.setOnClickListener {
            stopWork()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun updateNumberOnUI(number: Int) {
        mBinding.number.text = number.toString()
    }

    private fun runWorker() {
        workManager.enqueue(numberGeneratorWorkRequest)
    }

    private fun stopWork() {
        workManager.cancelWorkById(numberGeneratorWorkRequest.id)
    }
}