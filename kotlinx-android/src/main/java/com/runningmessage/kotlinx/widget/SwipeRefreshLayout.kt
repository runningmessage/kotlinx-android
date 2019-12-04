package com.runningmessage.kotlinx.widget

import android.content.Context
import android.util.AttributeSet

/**
 * Created by Lorss on 19-2-25.
 */
class SwipeRefreshLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    AbsSwipeRefreshLayout<CircleImageView, Nothing>(context, attrs) {

    override fun createProgressView(): CircleImageView = CircleImageView.create(context)

}