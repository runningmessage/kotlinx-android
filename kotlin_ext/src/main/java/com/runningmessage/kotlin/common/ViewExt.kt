package com.runningmessage.kotlin.common

import android.view.View
import android.view.ViewGroup

/**
 * Created by Lorss on 19-3-4.
 */

var <V : View> V.leftMargin: Int?
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin = value
    }
var <V : View> V.rightMargin: Int?
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.rightMargin
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.rightMargin = value
    }
var <V : View> V.topMargin: Int?
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin = value
    }
var <V : View> V.bottomMargin: Int?
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin = value
    }