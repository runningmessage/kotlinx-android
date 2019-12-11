package com.runningmessage.kotlinx.widget

import android.view.animation.Animation

/**
 * Created by Lorss on 19-2-25.
 */
interface SwipeRefreshProgress {

    fun setAnimationListener(listener: Animation.AnimationListener?)

    fun setStyle(style: Long)

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

//@formatter:off
private const val STYLE_MASK_BASE = 0b1111.toLong()


const val STYLE_MASK_SIZE   = STYLE_MASK_BASE

const val STYLE_DEFAULT     = 0b0001.toLong()
const val STYLE_LARGE       = 0b0010.toLong()
//@formatter:on

fun parseStyle(style: Long) = LongArray(8).apply {
    for (i in 0..7) {
        this[i] = style and (STYLE_MASK_BASE shl (i * 4))
    }
}