package com.example.servicesdemo

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.example.servicesdemo.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.milliseconds

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var mService: AppService? = null
    private var serviceConnection:ServiceConnection?=null
    private var serviceBound:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val serviceIntent = Intent(applicationContext, AppService::class.java)
        binding.startBtn.setOnClickListener {
            startService(serviceIntent)
        }
        binding.stopBtn.setOnClickListener {
            stopService(serviceIntent)
        }
        binding.bindService.setOnClickListener {
                 serviceConnection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    mService = (service as AppService.MyBinder).getService()
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    mService = null
                }
            }
            serviceBound = true
            bindService(serviceIntent,serviceConnection!!,Service.BIND_AUTO_CREATE)
        }
        binding.unbindService.setOnClickListener {
            if(serviceConnection!=null&&serviceBound) {
                unbindService(serviceConnection!!,)
                serviceBound = false
            }
        }
        binding.getNumberBtn.setOnClickListener {
            updateNumber()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun updateNumber() {
            binding.number.text = (mService?.getNumber()?:0).toString()
    }
}