package com.runningmessage.kotlinx.widget

import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Created by Lorss on 19-2-25.
 */
private var heightPixels = 0
private var widthPixels = 0

/**
 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 */
fun dip2px(context: Context, dpValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

/**
 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
 */
fun px2dip(context: Context, pxValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}

/**
 * 获取屏幕高度（像素）
 */
fun getScreenHeight(context: Context): Int {
    if (widthPixels == 0 || heightPixels == 0) {
        try {
            val dm = DisplayMetrics()
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(dm)
            widthPixels = dm.widthPixels
            heightPixels = dm.heightPixels
        } catch (e: Exception) {
            //
        }

    }
    return if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        if (widthPixels > heightPixels) widthPixels else heightPixels
    } else {
        if (widthPixels < heightPixels) widthPixels else heightPixels
    }
}

fun getScreenWidth(context: Context): Int {
    if (widthPixels == 0 || heightPixels == 0) {
        try {
            val dm = DisplayMetrics()
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(dm)
            widthPixels = dm.widthPixels
            heightPixels = dm.heightPixels
        } catch (e: Exception) {
            //
        }

    }
    return if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        if (widthPixels < heightPixels) widthPixels else heightPixels
    } else {
        if (widthPixels > heightPixels) widthPixels else heightPixels
    }
}