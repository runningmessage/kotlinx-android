package com.runningmessage.kotlinx.widget

import android.view.animation.Animation

/**
 * Created by Lorss on 19-2-26.
 */
interface SwipeRefreshRemind {

    fun setAnimationListener(listener: Animation.AnimationListener?)

    var message: String

    val viewWidth: Int

    val viewHeight: Int

    fun customAnimShow(taskAfterAnim: Runnable): Boolean = false

}