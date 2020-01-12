package com.runningmessage.kotlinx.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.runningmessage.kotlinx.demo.reflect.ReflectDemo

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ReflectDemo.onCreate(this)
    }

}
