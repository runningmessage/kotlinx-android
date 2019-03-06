package com.runningmessage.kotlin.common

import android.graphics.*
import android.support.annotation.IntDef
import android.support.annotation.RestrictTo
import android.widget.ImageView

/**
 * Created by Lorss on 19-3-6.
 */


//@formatter:off
const val CORNER_LEFT_TOP       = 0b0001
const val CORNER_RIGHT_TOP      = 0b0010
const val CORNER_RIGHT_BOTTOM   = 0b0100
const val CORNER_LEFT_BOTTOM    = 0b1000
const val CORNER_ALL            = 0b1111
//@formatter:on

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@Retention(AnnotationRetention.SOURCE)
@IntDef(
    flag = true,
    value = [CORNER_ALL.toLong(), CORNER_LEFT_TOP.toLong(), CORNER_RIGHT_TOP.toLong(), CORNER_RIGHT_BOTTOM.toLong(), CORNER_LEFT_BOTTOM.toLong()]
)
annotation class CornerPosition


val sS2FArray = arrayOf(
    Matrix.ScaleToFit.FILL,
    Matrix.ScaleToFit.START,
    Matrix.ScaleToFit.CENTER,
    Matrix.ScaleToFit.END
)

fun scaleTypeToScaleToFit(st: ImageView.ScaleType): Matrix.ScaleToFit {
    // ScaleToFit enum to their corresponding Matrix.ScaleToFit values
    return sS2FArray[st.ordinal - 1]
}

//生成圆角图片
@JvmOverloads

fun Bitmap?.createRoundedCornerBitmap(
    @CornerPosition corners: Int = CORNER_ALL,
    roundPx: Float? = null,
    outputWidth: Int = 0,
    outputHeight: Int = 0,
    scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP, matrix: Matrix? = null
): Bitmap? = this.let { bitmap ->


    try {

        if (bitmap == null) return null

        if ((corners and CORNER_ALL) == 0) return bitmap

        val rx = roundPx ?: 12f

        if (rx <= 0) return bitmap

        val srcWidth = bitmap.width
        val srcHeight = bitmap.height

        if (bitmap.width == 0 || bitmap.height == 0) return bitmap

        val outWidth = if (outputWidth > 0) outputWidth else srcWidth
        val outHeight = if (outputHeight > 0) outputHeight else srcHeight

        val output = Bitmap.createBitmap(
            outWidth,
            outHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val paint = Paint()


        val rectF: RectF = when (corners and CORNER_ALL) {
            CORNER_LEFT_TOP -> RectF(
                Rect(
                    0, 0, outWidth + rx.toInt() * 2,
                    outHeight + rx.toInt() * 2
                )
            )
            CORNER_RIGHT_TOP -> RectF(
                Rect(
                    -rx.toInt() * 2, 0, outWidth,
                    outHeight + rx.toInt() * 2
                )
            )
            CORNER_RIGHT_BOTTOM -> RectF(
                Rect(
                    -rx.toInt() * 2, -rx.toInt() * 2, outWidth,
                    outHeight
                )
            )
            CORNER_LEFT_BOTTOM -> RectF(
                Rect(
                    -rx.toInt() * 2, 0, outWidth + rx.toInt() * 2,
                    outHeight
                )
            )
            CORNER_LEFT_TOP or CORNER_LEFT_BOTTOM -> RectF(
                Rect(
                    0, 0, outWidth + rx.toInt() * 2,
                    outHeight
                )
            )
            CORNER_LEFT_TOP or CORNER_RIGHT_TOP -> RectF(
                Rect(
                    0, 0, outWidth,
                    outHeight + rx.toInt() * 2
                )
            )
            CORNER_RIGHT_TOP or CORNER_RIGHT_BOTTOM -> RectF(
                Rect(
                    -rx.toInt() * 2, 0, outWidth,
                    outHeight
                )
            )

            else -> RectF(
                Rect(
                    0, 0, outWidth,
                    outHeight
                )
            )
        }

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = Color.BLACK
        canvas.drawRoundRect(rectF, rx, rx, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)


        var drawableBitmap = bitmap

        if (outWidth != srcWidth || outHeight != srcHeight) {

            var mDrawMatrix: Matrix?

            Matrix().apply {

                /** copy from [android.widget.ImageView.configureBounds]*/
                when (scaleType) {
                    ImageView.ScaleType.MATRIX -> // Use the specified matrix as-is.
                        mDrawMatrix = if (matrix?.isIdentity == true) {
                            null
                        } else {
                            matrix
                        }
                    ImageView.ScaleType.CENTER -> {
                        // Center bitmap in view, no scaling.
                        mDrawMatrix = this
                        mDrawMatrix?.setTranslate(
                            Math.round((outWidth - srcWidth) * 0.5f).toFloat(),
                            Math.round((outHeight - srcHeight) * 0.5f).toFloat()
                        )
                    }
                    ImageView.ScaleType.CENTER_CROP -> {
                        mDrawMatrix = this

                        val scale: Float
                        var dx = 0f
                        var dy = 0f

                        if (srcWidth * outHeight > outWidth * srcHeight) {
                            scale = outHeight.toFloat() / srcHeight.toFloat()
                            dx = (outWidth - srcWidth * scale) * 0.5f
                        } else {
                            scale = outWidth.toFloat() / srcWidth.toFloat()
                            dy = (outHeight - srcHeight * scale) * 0.5f
                        }

                        mDrawMatrix?.setScale(scale, scale)
                        mDrawMatrix?.postTranslate(
                            Math.round(dx).toFloat(),
                            Math.round(dy).toFloat()
                        )
                    }
                    ImageView.ScaleType.CENTER_INSIDE -> {
                        mDrawMatrix = this
                        val scale: Float
                        val dx: Float
                        val dy: Float

                        if (srcWidth <= outWidth && srcHeight <= outHeight) {
                            scale = 1.0f
                        } else {
                            scale = Math.min(
                                outWidth.toFloat() / srcWidth.toFloat(),
                                outHeight.toFloat() / srcHeight.toFloat()
                            )
                        }

                        dx = Math.round((outWidth - srcWidth * scale) * 0.5f).toFloat()
                        dy = Math.round((outHeight - srcHeight * scale) * 0.5f).toFloat()

                        mDrawMatrix?.setScale(scale, scale)
                        mDrawMatrix?.postTranslate(dx, dy)
                    }
                    else -> {
                        // Generate the required transform.
                        val mTempSrc =
                            RectF().apply {

                                set(0f, 0f, srcWidth.toFloat(), srcHeight.toFloat())
                            }
                        val mTempDst =
                            RectF().apply {

                                set(0f, 0f, outWidth.toFloat(), outHeight.toFloat())
                            }

                        mDrawMatrix = this
                        mDrawMatrix?.setRectToRect(
                            mTempSrc,
                            mTempDst,
                            scaleTypeToScaleToFit(scaleType)
                        )
                    }
                }

            }

            drawableBitmap =
                    Bitmap.createBitmap(bitmap, 0, 0, srcWidth, srcHeight, mDrawMatrix, true)
        }

        val rectSrc = Rect(
            0, 0, outWidth,
            outHeight
        )
        val rectDst = Rect(
            0, 0, outWidth,
            outHeight
        )
        canvas.drawBitmap(drawableBitmap, rectSrc, rectDst, paint)
        return output
    } catch (e: Exception) {
        return bitmap
    }


}
