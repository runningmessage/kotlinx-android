package com.runningmessage.kotlinx.demo

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.animation.Animation
import com.runningmessage.kotlinx.common.dpToPx
import com.runningmessage.kotlinx.common.screenWidthX
import com.runningmessage.kotlinx.widget.SwipeRefreshRemind

/**
 * Created by Lorss on 19-2-26.
 */
class SwipeRemindView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
        AppCompatTextView(context, attrs), SwipeRefreshRemind {

    init {
        setBackgroundResource(R.drawable.remind_bg)
        setPadding(0, 8, 0, 8)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        setTextColor(Color.parseColor("#FFFFFFFF"))
        gravity = Gravity.CENTER

    }

    private var mListener: Animation.AnimationListener? = null

    override fun setAnimationListener(listener: Animation.AnimationListener?) {
        mListener = listener
    }

    public override fun onAnimationStart() {
        super.onAnimationStart()
        mListener?.onAnimationStart(animation)
    }

    public override fun onAnimationEnd() {
        super.onAnimationEnd()
        mListener?.onAnimationEnd(animation)
    }

    override var message: String
        get() = text.toString()
        set(value) {
            text = value
        }
    override val viewWidth: Int
        get() = context.screenWidthX() - 100
    override val viewHeight: Int
        get() = context.dpToPx(36f)

}