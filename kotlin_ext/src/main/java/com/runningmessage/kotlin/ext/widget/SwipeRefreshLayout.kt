package com.runningmessage.kotlin.ext.widget

import android.content.Context
import android.util.AttributeSet

/**
 * Created by Lorss on 19-2-25.
 */
class SwipeRefreshLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    AbsSwipeRefreshLayout<CircleImageView>(context, attrs) {

    override fun createProgressView(): CircleImageView = CircleImageView.create(context)

}