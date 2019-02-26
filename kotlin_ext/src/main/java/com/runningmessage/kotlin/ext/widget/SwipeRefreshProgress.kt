package com.runningmessage.kotlin.ext.widget

import android.view.animation.Animation

/**
 * Created by Lorss on 19-2-25.
 */
interface SwipeRefreshProgress {

    fun setAnimationListener(listener: Animation.AnimationListener?)

    fun setStyle(style: Int)

    val viewWidth: Int

    val viewHeight: Int

    fun autoToAnimRefreshing(listener: Animation.AnimationListener?)

    fun releaseToAnimRefreshing(interpolatedTime: Float)

    fun startAnimRefreshing()

    fun stopAnimRefreshing()

    fun startDragging()

    fun moveSpinner(
        overscrollTop: Float,
        totalDragDistance: Float,
        adjustedPercent: Float,
        tensionPercent: Float,
        targetY: Int
    )

    fun finishSpinner(overscrollTop: Float, totalDragDistance: Float)

}

private const val STYLE_MASK_BASE = 0b1111
const val STYLE_MASK_SIZE = STYLE_MASK_BASE


const val STYLE_DEFAULT = 1
const val STYLE_LARGE = 2

fun parseStyle(style: Int) = IntArray(8).apply {
    for (i in 0..7) {
        this[i] = style and (STYLE_MASK_BASE shl (i * 4))
    }
}