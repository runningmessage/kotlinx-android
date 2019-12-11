package com.runningmessage.kotlinx.common

import android.os.Handler

/***
 *  rearrange the params for [Handler.postDelayed], so that it is able to use lambda
 */
fun <H : Handler, R> H.postDelayed(delay: Long?, task: Function0<R>?) = task?.let { task ->
    postDelayed({ task.invoke() }, delay ?: 0)
}