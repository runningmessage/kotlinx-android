package com.runningmessage.kotlin.ext.demo

import android.content.Context
import android.util.AttributeSet
import com.runningmessage.kotlin.ext.widget.AbsSwipeRefreshLayout

/**
 * Created by Lorss on 19-2-25.
 */
class MySwipeRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AbsSwipeRefreshLayout<LoadingAnimView, SwipeRemindView>(context, attrs) {

    init {
        setShowRemind(true)
    }

    override fun createProgressView(): LoadingAnimView = LoadingAnimView(context).apply {
        this.setCircleRadius(30)
        this.setDuration(500)
        this.setCircleScaleDelta(.3f)
    }

    override fun createRemindView(): SwipeRemindView? = SwipeRemindView(context)


}