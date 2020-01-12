package com.runningmessage.kotlinx.demo

import android.app.Application

/**
 * Created by Lorss on 20-1-8.
 */
class DemoApplication : Application() {

    companion object {
        lateinit var INSTANCE: DemoApplication
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}