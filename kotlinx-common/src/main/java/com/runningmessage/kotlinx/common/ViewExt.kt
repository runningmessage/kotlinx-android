package com.runningmessage.kotlinx.common

import android.view.View
import android.view.ViewGroup

/**
 * Created by Lorss on 19-3-4.
 */

/***
 *     1. return the leftMargin of the layoutParams for the current View, if exists , otherwise return null
 *     2. set the leftMargin of the layoutParams for the current View, and reset the layoutParams
 */
var <V : View> V.layoutLeftMargin: Int?
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
            leftMargin = value ?: 0
            layoutParams = this
        }
    }

/***
 *     1. return the rightMargin of the layoutParams for the current View, if exists , otherwise return null
 *     2. set the rightMargin of the layoutParams for the current View, and reset the layoutParams
 */
var <V : View> V.layoutRightMargin: Int?
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.rightMargin
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
            rightMargin = value ?: 0
            layoutParams = this
        }
    }

/***
 *     1. return the topMargin of the layoutParams for the current View, if exists , otherwise return null
 *     2. set the topMargin of the layoutParams for the current View, and reset the layoutParams
 */
var <V : View> V.layoutTopMargin: Int?
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
            topMargin = value ?: 0
            layoutParams = this
        }
    }

/***
 *     1. return the bottomMargin of the layoutParams for the current View, if exists , otherwise return null
 *     2. set the bottomMargin of the layoutParams for the current View, and reset the layoutParams
 */
var <V : View> V.layoutBottomMargin: Int?
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin
    set(value) {
        (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
            bottomMargin = value ?: 0
            layoutParams = this
        }
    }

/***
 *     1. return the width of the layoutParams for the current View, if exists , otherwise return null
 *     2. set the width of the layoutParams for the current View, and reset the layoutParams
 */
var <V : View> V.layoutWidth: Int?
    get() = layoutParams?.width
    set(value) {
        layoutParams?.apply {
            width = value ?: 0
            layoutParams = this
        }
    }

/***
 *     1. return the height of the layoutParams for the current View, if exists , otherwise return null
 *     2. set the height of the layoutParams for the current View, and reset the layoutParams
 */
var <V : View> V.layoutHeight: Int?
    get() = layoutParams?.height
    set(value) {
        layoutParams?.apply {
            height = value ?: 0
            layoutParams = this
        }
    }

/***
 *  rearrange the params for [View.postDelayed], so that it is able to use lambda
 */
fun <V : View, R> V.postDelayed(delay: Long?, task: Function0<R>?) = task?.let { task ->
    postDelayed({ task.invoke() }, delay ?: 0)
}

