package com.runningmessage.kotlinx.common

import android.view.View
import android.view.ViewGroup

/**
 *  the list of child view
 */
val ViewGroup.views: List<View>
    get() = List<View>(childCount) { getChildAt(it) }

/**
 *  the first child view
 */
val ViewGroup.firstView: View?
    get() = views.firstOrNull()

/***
 *  the last child view
 */
val ViewGroup.lastView: View?
    get() = views.lastOrNull()