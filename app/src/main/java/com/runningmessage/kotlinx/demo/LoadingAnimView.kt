package com.runningmessage.kotlinx.demo

/**
 * Created by Lorss on 19-2-25.
 */

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import com.runningmessage.kotlinx.widget.SwipeRefreshProgress
import com.runningmessage.kotlinx.widget.dip2px

class LoadingAnimView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
        RelativeLayout(context), SwipeRefreshProgress {

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

    override fun setStyle(style: Long) {
        // nothing to do
    }

    override val viewWidth: Int
        get() = dip2px(context, 32f)
    override val viewHeight: Int
        get() = dip2px(context, 16f)

    override fun autoToAnimRefreshing(listener: Animation.AnimationListener?) {
        listener?.onAnimationEnd(null)
    }

    override fun releaseToAnimRefreshing(interpolatedTime: Float) {

    }

    override fun startAnimRefreshing() {
        startAnim(0)
    }

    override fun stopAnimRefreshing() {
        stopAnim()
    }

    override fun startDragging() {

    }

    override fun moveSpinner(
            overscrollTop: Float,
            totalDragDistance: Float,
            adjustedPercent: Float,
            tensionPercent: Float,
            targetY: Int
    ) {
        stopFrame(overscrollTop / totalDragDistance)
    }

    override fun finishSpinner(overscrollTop: Float, totalDragDistance: Float) {
        stopAnim()
    }

    private var mDrawable1: GradientDrawable? = null
    private var mDrawable2: GradientDrawable? = null

    private var mIsAnim = false

    private var mCircleRadius: Int = 0
    private var mCircleScaleDelta: Float = 0.toFloat()

    private var mDuration: Int = 0

    init {
        init()
    }

    private fun init() {
        setWillNotDraw(false)

        mDrawable1 = GradientDrawable()
        mDrawable1!!.shape = GradientDrawable.OVAL
        mDrawable1!!.setColor(Color.parseColor("#4394eb"))

        mDrawable2 = GradientDrawable()
        mDrawable2!!.shape = GradientDrawable.OVAL
        mDrawable2!!.setColor(Color.parseColor("#4394eb"))

    }

    fun setCircleRadius(circleRadius: Int) {
        mCircleRadius = circleRadius
    }

    fun setCircleScaleDelta(circleScaleDelta: Float) {
        mCircleScaleDelta = circleScaleDelta
    }

    fun setDuration(duration: Int) {
        mDuration = duration
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mIsAnim) {
            drawCircle(mCurrent, canvas)
        } else {
            drawCircle(mStop, canvas)
        }
    }

    private fun drawCircle(c: Int, canvas: Canvas) {
        val width = width
        val height = height
        if (width == 0 || height == 0) return

        if (mDuration <= 0) return
        if (c < 0) return
        if (mCircleRadius <= 0) return

        val ratio = (c % mDuration).toFloat() / mDuration.toFloat()
        val translationX = ((width - mCircleRadius) * ratio).toInt()


        var scaleDelta = 0f
        if (mCircleScaleDelta > 0) {
            scaleDelta = mCircleScaleDelta * (1 - Math.abs(.5f - ratio) / .5f)
        }

        val lRadius = (mCircleRadius * (1 + scaleDelta)).toInt()
        val lLeft = translationX - (lRadius - mCircleRadius) / 2
        val lRight = lLeft + lRadius
        val lTop = (height - lRadius) / 2
        val lBottom = lTop + lRadius
        mDrawable1!!.setBounds(lLeft, lTop, lRight, lBottom)

        val rRadius = (mCircleRadius * (1 - scaleDelta)).toInt()
        val rLeft = width - mCircleRadius - translationX + (mCircleRadius - rRadius) / 2
        val rRight = rLeft + rRadius
        val rTop = (height - rRadius) / 2
        val rBottom = rTop + rRadius
        mDrawable2!!.setBounds(rLeft, rTop, rRight, rBottom)

        mDrawable2!!.draw(canvas)
        mDrawable1!!.draw(canvas)
    }

    fun startAnim(from: Int) {
        var from = from
        mIsAnim = true

        if (mDuration > 0) {

            from = from % mDuration
            mCurrent = from
            val valueAnimator = ValueAnimator.ofInt(0, mDuration)
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.repeatCount = ValueAnimator.INFINITE
            valueAnimator.duration = mDuration.toLong()

            val finalFrom = from
            valueAnimator.addUpdateListener { animation ->
                if (mDuration > 0 && mIsAnim) {
                    mCurrent = (animation.animatedValue as Int + finalFrom) % mDuration
                    postInvalidate()
                } else {
                    valueAnimator.cancel()
                }
            }
            valueAnimator.start()
        }
    }

    fun stopAnim() {
        mIsAnim = false
    }

    fun stopFrame(ratio: Float) {
        if (ratio >= 0 && ratio <= 1) {
            mStop = (mDuration * ratio).toInt()
            mIsAnim = false
            postInvalidate()
        }
    }

    companion object {

        private var mCurrent: Int = 0
        private var mStop: Int = 0
    }
}
