package com.runningmessage.kotlinx.common

/**
 * Created by Lorss on 19-3-7.
 */

import android.os.Build

/**
 * Created by Lorss on 19-3-7.
 */
fun fromSdk(sdkInt: Int, block: () -> Unit): Long {
    if (Build.VERSION.SDK_INT >= sdkInt) block()
    return UNTIL or sdkInt.toLong()
}

fun toSdk(sdkInt: Int, block: () -> Unit): Long {
    if (Build.VERSION.SDK_INT <= sdkInt) block()
    return AFTER or sdkInt.toLong()
}

fun afterSdk(sdkInt: Int, block: () -> Unit): Long {
    if (Build.VERSION.SDK_INT > sdkInt) block()
    return TO or sdkInt.toLong()
}

fun untilSdk(sdkInt: Int, block: () -> Unit): Long {
    if (Build.VERSION.SDK_INT < sdkInt) block()
    return FROM or sdkInt.toLong()
}

infix fun Long.other(block: () -> Unit) {
    val sdkInt = (this and Int.MAX_VALUE.toLong()).toInt()
    when {
        this and FROM != 0L -> fromSdk(sdkInt, block)
        this and TO != 0L -> toSdk(sdkInt, block)
        this and AFTER != 0L -> afterSdk(sdkInt, block)
        this and UNTIL != 0L -> untilSdk(sdkInt, block)
    }
}

//@formatter:off
private const val FROM    = 1L shl Int.SIZE_BITS
private const val TO      = 1L shl (Int.SIZE_BITS + 1)
private const val AFTER   = 1L shl (Int.SIZE_BITS + 2)
private const val UNTIL   = 1L shl (Int.SIZE_BITS + 3)
//@formatter:on