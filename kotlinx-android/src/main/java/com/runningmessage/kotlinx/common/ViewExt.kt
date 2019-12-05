package com.runningmessage.kotlinx.common

import android.view.View
import android.view.ViewGroup

/**
 * Created by Lorss on 19-3-4.
 */

var <V : View> V.layoutLeftMargin: Int?
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
            leftMargin = value ?: 0
            layoutParams = this
        }
    }

var <V : View> V.layoutRightMargin: Int?
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.rightMargin
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
            rightMargin = value ?: 0
            layoutParams = this
        }
    }

var <V : View> V.layoutTopMargin: Int?
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
            topMargin = value ?: 0
            layoutParams = this
        }
    }

var <V : View> V.layoutBottomMargin: Int?
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
            bottomMargin = value ?: 0
            layoutParams = this
        }
    }

var <V : View> V.layoutWidth: Int?
    get() = layoutParams?.width
    set(value) {
        layoutParams?.apply {
            width = value ?: 0
            layoutParams = this
        }
    }

var <V : View> V.layoutHeight: Int?
    get() = layoutParams?.height
    set(value) {
        layoutParams?.apply {
            height = value ?: 0
            layoutParams = this
        }
    }
