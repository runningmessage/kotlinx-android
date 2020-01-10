package com.runningmessage.kotlinx.common

inline fun <C, R> C?.ifNullOrEmpty(defaultValue: () -> R) where C : CharSequence, R : C =
        if (isNullOrEmpty()) defaultValue() else this

inline fun <C, R> C?.ifNullOrBlank(defaultValue: () -> R) where C : CharSequence, R : C =
        if (isNullOrBlank()) defaultValue() else this

inline fun <C, R> C?.ifNotNullOrEmpty(translate: (C) -> R) where C : CharSequence =
        if (!isNullOrEmpty()) translate(this!!) else null

inline fun <C, R> C?.ifNotNullOrBlank(translate: (C) -> R) where C : CharSequence =
        if (!isNullOrBlank()) translate(this!!) else null