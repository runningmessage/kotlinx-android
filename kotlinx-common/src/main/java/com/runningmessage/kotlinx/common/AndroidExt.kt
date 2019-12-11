package com.runningmessage.kotlinx.common

/**
 * Created by Lorss on 19-3-7.
 */

import android.os.Build

/**
 * Created by Lorss on 19-3-7.
 */

//@formatter:off
/**
 * e.g.
```java

fromSdk(16) {
    //do something if Build.VERSION.SDK_INT >= 16
} other {
    //otherwise, Build.VERSION.SDK_INT < 16, do other thing
}

```
 */
//@formatter:on
fun fromSdk(sdkInt: Int, block: () -> Unit): Long {
    if (Build.VERSION.SDK_INT >= sdkInt) block()
    return UNTIL or sdkInt.toLong()
}

//@formatter:off
/**
 * e.g.
```java

toSdk(16) {
    //do something if Build.VERSION.SDK_INT <= 16
} other {
    //otherwise, Build.VERSION.SDK_INT > 16, do other thing
}

```
 */
//@formatter:on
fun toSdk(sdkInt: Int, block: () -> Unit): Long {
    if (Build.VERSION.SDK_INT <= sdkInt) block()
    return AFTER or sdkInt.toLong()
}

//@formatter:off
/**
 * e.g.
```java

afterSdk(16) {
    //do something if Build.VERSION.SDK_INT > 16
} other {
    //otherwise, Build.VERSION.SDK_INT <= 16, do other thing
}

```
 */
//@formatter:on
fun afterSdk(sdkInt: Int, block: () -> Unit): Long {
    if (Build.VERSION.SDK_INT > sdkInt) block()
    return TO or sdkInt.toLong()
}

//@formatter:off
/**
 * e.g.
```java

untilSdk(16) {
    //do something if Build.VERSION.SDK_INT < 16
} other {
    //otherwise, Build.VERSION.SDK_INT >= 16, do other thing
}

```
 */
//@formatter:on
fun untilSdk(sdkInt: Int, block: () -> Unit): Long {
    if (Build.VERSION.SDK_INT < sdkInt) block()
    return FROM or sdkInt.toLong()
}

/***
 * see : [fromSdk], [toSdk], [untilSdk], [afterSdk]
 */
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