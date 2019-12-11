package com.runningmessage.kotlinx.common

import java.lang.reflect.Method

val FragmentClassSupport: Class<*>?
    get() = try {
        Class.forName("android.support.v4.app.Fragment")
    } catch (e: Throwable) {
        null
    }

val FragmentGetActivitySupport: Method?
    get() = try {
        FragmentClassSupport?.getDeclaredMethod("getActivity")
    } catch (e: Throwable) {
        null
    }

val FragmentIsRemovingSupport: Method?
    get() = try {
        FragmentClassSupport?.getDeclaredMethod("isRemoving")
    } catch (e: Throwable) {
        null
    }