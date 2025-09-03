package com.bos.payment.appName.localdb

import android.util.Log
import com.bos.payment.appName.localdb.AppLog.initialize

import org.apache.poi.sl.usermodel.ObjectMetaData.Application

class Controller : android.app.Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: Controller? = null

        fun applicationContext() : Controller {
            return instance as Controller
        }
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize global resources here (e.g., logging, DI, shared prefs)
        Log.d("MyApplication", "App Started")

        this.initialize()


    }

}