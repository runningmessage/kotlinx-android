package com.runningmessage.kotlinx.common

fun <T> T.ifNull(block: () -> T) = this ?: block()
