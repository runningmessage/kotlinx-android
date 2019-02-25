/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.runningmessage.kotlin.ext.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.support.annotation.VisibleForTesting
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView

/**
 * Private class created to work around issues with AnimationListeners being
 * called before the animation is actually complete and support shadows on older
 * platforms.
 */
class CircleImageView(context: Context, color: Int) : ImageView(context),
    SwipeRefreshProgress {

    private var mListener: Animation.AnimationListener? = null
    var mShadowRadius: Int = 0

    init {
        val density = getContext().resources.displayMetrics.density
        val shadowYOffset = (density * Y_OFFSET).toInt()
        val shadowXOffset = (density * X_OFFSET).toInt()

        mShadowRadius = (density * SHADOW_RADIUS).toInt()

        val circle: ShapeDrawable
        if (elevationSupported()) {
            circle = ShapeDrawable(OvalShape())
            ViewCompat.setElevation(this, SHADOW_ELEVATION * density)
        } else {
            val oval = OvalShadow(mShadowRadius)
            circle = ShapeDrawable(oval)
            setLayerType(View.LAYER_TYPE_SOFTWARE, circle.paint)
            circle.paint.setShadowLayer(
                mShadowRadius.toFloat(), shadowXOffset.toFloat(), shadowYOffset.toFloat(),
                KEY_SHADOW_COLOR
            )
            val padding = mShadowRadius
            // set padding so the inner image sits correctly within the shadow.
            setPadding(padding, padding, padding, padding)
        }
        circle.paint.color = color
        ViewCompat.setBackground(this, circle)
    }

    private fun elevationSupported(): Boolean {
        return android.os.Build.VERSION.SDK_INT >= 21
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!elevationSupported()) {
            setMeasuredDimension(
                measuredWidth + mShadowRadius * 2,
                measuredHeight + mShadowRadius * 2
            )
        }
    }

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

    /**
     * Update the background color of the circle image view.
     *
     * @param colorRes Id of a color resource.
     */
    fun setBackgroundColorRes(colorRes: Int) {
        setBackgroundColor(ContextCompat.getColor(context, colorRes))
    }

    override fun setBackgroundColor(color: Int) {
        if (background is ShapeDrawable) {
            (background as ShapeDrawable).paint.color = color
        }
    }

    private inner class OvalShadow internal constructor(shadowRadius: Int) : OvalShape() {
        private var mRadialGradient: RadialGradient? = null
        private val mShadowPaint: Paint

        init {
            mShadowPaint = Paint()
            mShadowRadius = shadowRadius
            updateRadialGradient(rect().width().toInt())
        }

        override fun onResize(width: Float, height: Float) {
            super.onResize(width, height)
            updateRadialGradient(width.toInt())
        }

        override fun draw(canvas: Canvas, paint: Paint) {
            val viewWidth = this@CircleImageView.width
            val viewHeight = this@CircleImageView.height
            canvas.drawCircle(
                (viewWidth / 2).toFloat(),
                (viewHeight / 2).toFloat(),
                (viewWidth / 2).toFloat(),
                mShadowPaint
            )
            canvas.drawCircle(
                (viewWidth / 2).toFloat(),
                (viewHeight / 2).toFloat(),
                (viewWidth / 2 - mShadowRadius).toFloat(),
                paint
            )
        }

        private fun updateRadialGradient(diameter: Int) {
            mRadialGradient = RadialGradient(
                (diameter / 2).toFloat(),
                (diameter / 2).toFloat(),
                mShadowRadius.toFloat(),
                intArrayOf(FILL_SHADOW_COLOR, Color.TRANSPARENT),
                null,
                Shader.TileMode.CLAMP
            )
            mShadowPaint.shader = mRadialGradient
        }
    }

    companion object {

        private const val KEY_SHADOW_COLOR = 0x1E000000
        private const val FILL_SHADOW_COLOR = 0x3D000000
        // PX
        private const val X_OFFSET = 0f
        private const val Y_OFFSET = 1.75f
        private const val SHADOW_RADIUS = 3.5f
        private const val SHADOW_ELEVATION = 4
        // Default background for the progress spinner
        private const val CIRCLE_BG_LIGHT = -0x50506

        @VisibleForTesting
        internal val CIRCLE_DIAMETER = 40
        @VisibleForTesting
        internal val CIRCLE_DIAMETER_LARGE = 56

        private const val MAX_ALPHA = 255
        private const val STARTING_PROGRESS_ALPHA = (.3f * MAX_ALPHA).toInt()


        // Max amount of circle that can be filled by progress during swipe gesture,
        // where 1.0 is a full circle
        private const val MAX_PROGRESS_ANGLE = .8f

        private const val ALPHA_ANIMATION_DURATION = 300

        fun create(context: Context): CircleImageView =
            CircleImageView(context, CIRCLE_BG_LIGHT).apply {
                visibility = View.GONE
            }
    }

    private var mProgress: CircularProgressDrawable
    private var mScaleAnimation: Animation? = null
    private var mAlphaStartAnimation: Animation? = null
    private var mAlphaMaxAnimation: Animation? = null

    override fun setSize(size: Int) {
        if (size != CircularProgressDrawable.LARGE && size != CircularProgressDrawable.DEFAULT) {
            return
        }
        val metrics = resources.displayMetrics
        progressCircleDiameter = if (size == CircularProgressDrawable.LARGE) {
            (CIRCLE_DIAMETER_LARGE * metrics.density).toInt()
        } else {
            (CIRCLE_DIAMETER * metrics.density).toInt()
        }
        // force the bounds of the progress circle inside the circle view to
        // update by setting it to null before updating its size and then
        // re-setting it
        setImageDrawable(null)
        mProgress?.setStyle(size)
        setImageDrawable(mProgress)
    }

    /**
     * Get the diameter of the progress circle that is displayed as part of the
     * swipe to refresh layout.
     *
     * @return Diameter in pixels of the progress circle view.
     */
    var progressCircleDiameter: Int = 0
        private set

    private val mMediumAnimationDuration: Int = resources.getInteger(
        android.R.integer.config_mediumAnimTime
    )

    init {
        val metrics = resources.displayMetrics
        progressCircleDiameter = (CIRCLE_DIAMETER * metrics.density).toInt()

        mProgress = CircularProgressDrawable(context)
        mProgress?.setStyle(CircularProgressDrawable.DEFAULT)
        setImageDrawable(mProgress)
    }

    override val viewWidth: Int
        get() = progressCircleDiameter

    override val viewHeight: Int
        get() = progressCircleDiameter

    override var progressAlpha: Int
        get() = mProgress.alpha
        set(alpha) {
            mProgress.alpha = alpha
        }

    override fun autoToAnimRefreshing(listener: Animation.AnimationListener?) {
        visibility = View.VISIBLE
        progressAlpha = MAX_ALPHA
        mScaleAnimation = object : Animation() {
            public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                scaleX = interpolatedTime
                scaleY = interpolatedTime
            }
        }
        mScaleAnimation!!.duration = mMediumAnimationDuration.toLong()
        if (listener != null) {
            setAnimationListener(listener)
        }
        clearAnimation()
        startAnimation(mScaleAnimation)
    }

    override fun releaseToAnimRefreshing(interpolatedTime: Float) {
        mProgress.arrowScale = 1 - interpolatedTime
    }

    override fun startAnimRefreshing() {
        // Make sure the progress view is fully visible
        mProgress.alpha = MAX_ALPHA
        mProgress.start()
    }

    override fun stopAnimRefreshing() {
        mProgress.stop()
        background.alpha = MAX_ALPHA
        mProgress.alpha = MAX_ALPHA
    }

    override fun startDragging() {
        mProgress.alpha = STARTING_PROGRESS_ALPHA
    }


    override fun moveSpinner(
        overscrollTop: Float,
        totalDragDistance: Float,
        adjustedPercent: Float,
        tensionPercent: Float,
        targetY: Int
    ) {
        mProgress.arrowEnabled = true


        if (overscrollTop < totalDragDistance) {
            if (progressAlpha > STARTING_PROGRESS_ALPHA && !isAnimationRunning(mAlphaStartAnimation)) {
                // Animate the alpha
                startProgressAlphaStartAnimation()
            }
        } else {
            if (progressAlpha < MAX_ALPHA && !isAnimationRunning(mAlphaMaxAnimation)) {
                // Animate the alpha
                startProgressAlphaMaxAnimation()
            }
        }

        val strokeStart = adjustedPercent * .8f
        mProgress.setStartEndTrim(0f, Math.min(MAX_PROGRESS_ANGLE, strokeStart))
        mProgress.arrowScale = Math.min(1f, adjustedPercent)

        val rotation = (-0.25f + .4f * adjustedPercent + tensionPercent * 2) * .5f
        mProgress.progressRotation = rotation
    }

    private fun isAnimationRunning(animation: Animation?): Boolean {
        return animation != null && animation.hasStarted() && !animation.hasEnded()
    }

    private fun startProgressAlphaStartAnimation() {
        mAlphaStartAnimation =
                startAlphaAnimation(
                    progressAlpha,
                    STARTING_PROGRESS_ALPHA
                )
    }

    private fun startProgressAlphaMaxAnimation() {
        mAlphaMaxAnimation = startAlphaAnimation(
            progressAlpha,
            MAX_ALPHA
        )
    }

    private fun startAlphaAnimation(startingAlpha: Int, endingAlpha: Int): Animation {
        val alpha = object : Animation() {
            public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                progressAlpha =
                        (startingAlpha + (endingAlpha - startingAlpha) * interpolatedTime).toInt()
            }
        }
        alpha.duration = ALPHA_ANIMATION_DURATION.toLong()
        // Clear out the previous animation listeners.
        setAnimationListener(null)
        clearAnimation()
        startAnimation(alpha)
        return alpha
    }

    override fun finishSpinner(overscrollTop: Float, mTotalDragDistance: Float) {
        if (overscrollTop > mTotalDragDistance) {

        } else {
            mProgress.setStartEndTrim(0f, 0f)
            mProgress.arrowEnabled = false
        }
    }
}
