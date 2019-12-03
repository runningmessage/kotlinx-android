package com.runningmessage.kotlin.common

import android.view.View
import android.view.ViewGroup

/**
 * Created by Lorss on 19-3-4.
 */

var <V : View> V.layoutLeftMargin: Int?
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin = value
    }
var <V : View> V.layoutRightMargin: Int?
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.rightMargin
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.rightMargin = value
    }
var <V : View> V.layoutTopMargin: Int?
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin = value
    }
var <V : View> V.layoutBottomMargin: Int?
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin = value
    }
var <V : View> V.layoutWidth: Int?
    get() = layoutParams?.width
    set(value) {
        layoutParams?.width = value
    }
var <V : View> V.layoutHeight: Int?
    get() = layoutParams?.height
    set(value) {
        layoutParams?.height = value
    }
