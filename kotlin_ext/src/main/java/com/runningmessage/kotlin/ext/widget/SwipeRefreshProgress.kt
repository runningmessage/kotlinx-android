package com.runningmessage.kotlin.ext.widget

import android.view.animation.Animation

/**
 * Created by Lorss on 19-2-25.
 */
interface SwipeRefreshProgress {

    fun setAnimationListener(listener: Animation.AnimationListener?)

    fun setSize(size: Int)

    val viewWidth: Int

    val viewHeight: Int

    var progressAlpha: Int

    fun autoToAnimRefreshing(listener: Animation.AnimationListener?)

    fun releaseToAnimRefreshing(interpolatedTime: Float)

    fun startAnimRefreshing()

    fun stopAnimRefreshing()

    fun startDragging()

    fun moveSpinner(
        overscrollTop: Float,
        mTotalDragDistance: Float,
        adjustedPercent: Float,
        tensionPercent: Float,
        targetY: Int
    )

    fun finishSpinner(overscrollTop: Float, mTotalDragDistance: Float)

}