package com.runningmessage.kotlin.ext.demo

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.animation.Animation
import android.widget.TextView
import com.runningmessage.kotlin.ext.widget.SwipeRefreshRemind
import com.runningmessage.kotlin.ext.widget.dip2px
import com.runningmessage.kotlin.ext.widget.getScreenWidth

/**
 * Created by Lorss on 19-2-26.
 */
class SwipeRemindView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    TextView(context, attrs), SwipeRefreshRemind {

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
        get() = getScreenWidth(context) - 100
    override val viewHeight: Int
        get() = dip2px(context, 36f)

}