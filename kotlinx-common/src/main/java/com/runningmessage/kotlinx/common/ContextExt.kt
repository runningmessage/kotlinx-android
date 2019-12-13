package com.runningmessage.kotlinx.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.support.annotation.StringRes
import android.widget.Toast

fun <T : Context> T?.toastShort(msg: CharSequence) = this?.let { context ->
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun <T : Context> T?.toastShort(@StringRes resId: Int) = this?.let { context ->
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun <T : Context> T?.toastLong(msg: CharSequence) = this?.let { context ->
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun <T : Context> T?.toastLong(@StringRes resId: Int) = this?.let { context ->
    Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
}

inline fun <reified A : Activity> Context?.startActivity() = this?.let { context ->
    startActivity(Intent(context, A::class.java))
}

/***
 *  cast the [dp] dip into px unit
 */
fun Context.dpToPx(dp: Float): Int = (dp * resources.displayMetrics.density + 0.5f).toInt()

/***
 *  cast the [px] px into dip unit
 */
fun Context.pxToDp(px: Float): Int = (px / resources.displayMetrics.density + 0.5f).toInt()

/***
 *  get the screen width pixels ,
 *  same as [Context.getResources].[getDisplayMetrics][android.content.res.Resources.getDisplayMetrics].[widthPixels][android.util.DisplayMetrics.widthPixels]
 */
fun Context.screenWidth(): Int = resources.displayMetrics.widthPixels

/***
 *  return the smaller between [screenWidth] and [screenHeight]
 */
fun Context.screenWidthShort(): Int = resources.displayMetrics.let {
    return if (it.widthPixels < it.heightPixels) it.widthPixels else it.heightPixels
}

/***
 *  return the [screenWidthShort] if current is orientation portrait, otherwise the [screenHeightLong]
 */
fun Context.screenWidthX(): Int = resources.displayMetrics.let {
    return if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        screenWidthShort()
    } else {
        screenHeightLong()
    }
}

/***
 *  get the screen height pixels ,
 *  same as [Context.getResources].[getDisplayMetrics][android.content.res.Resources.getDisplayMetrics].[heightPixels][android.util.DisplayMetrics.heightPixels]
 */
fun Context.screenHeight(): Int = resources.displayMetrics.heightPixels

/***
 *  return the larger between [screenWidth] and [screenHeight]
 */
fun Context.screenHeightLong(): Int = resources.displayMetrics.let {
    return if (it.widthPixels > it.heightPixels) it.widthPixels else it.heightPixels
}

/***
 *  return the [screenHeightLong] if current is orientation portrait, otherwise the [screenWidthShort]
 */
fun Context.screenHeightY(): Int = resources.displayMetrics.let {
    return if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        screenHeightLong()
    } else {
        screenWidthShort()
    }
}


