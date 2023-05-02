package com.example.servicesdemo

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.example.servicesdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityMainBinding
    var mService: AppService? = null
    private var mServiceConnection:ServiceConnection?=null
    private var mServiceBound:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
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
            bindService(serviceIntent,mServiceConnection!!,Service.BIND_AUTO_CREATE)
        }
        mBinding.unbindService.setOnClickListener {
            if(mServiceConnection!=null&&mServiceBound) {
                unbindService(mServiceConnection!!,)
                mServiceBound = false
            }
        }
        mBinding.getNumberBtn.setOnClickListener {
            updateNumber()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun updateNumber() {
            mBinding.number.text = (mService?.getNumber()?:0).toString()
    }
}