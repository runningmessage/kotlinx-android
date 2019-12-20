package com.runningmessage.kotlinx.common

inline fun <T> T.ifNull(defaultValue: () -> T) = this ?: defaultValue()
