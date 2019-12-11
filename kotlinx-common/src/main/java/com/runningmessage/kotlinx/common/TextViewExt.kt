package com.runningmessage.kotlinx.common

import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by Lorss on 19-3-4.
 */

/***
 * add "…" after the end of text
 */
fun TextView.addEllipsis(textWidth: Int, maxLine: Int = 2) {

    if (maxLine <= 0) return

    measureView(this, textWidth - 8)
    var currentLayout = layout
    var fix = false
    while (currentLayout?.lineCount ?: 0 > maxLine) {
        text = text.substring(0, currentLayout.getLineEnd(maxLine - 1) - 1) + "…"
        fix = true
        currentLayout = layout
    }
    if (fix && text.length > maxLine + 2) {
        text = text.substring(0, text.length - maxLine - 1) + "…"
    }
    measureView(this, textWidth)
}

private fun measureView(child: View, width: Int): Int {
    var p: ViewGroup.LayoutParams? = child.layoutParams
    if (p == null) {
        p = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    val spec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST)
    val childWidthSpec = ViewGroup.getChildMeasureSpec(spec, 0 + 0, p.width)
    val lpHeight = p.height
    val childHeightSpec: Int
    if (lpHeight > 0) {
        childHeightSpec = View.MeasureSpec.makeMeasureSpec(
                lpHeight,
                View.MeasureSpec.EXACTLY
        )
    } else {
        childHeightSpec = View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
        )
    }
    child.measure(childWidthSpec, childHeightSpec)
    return child.measuredHeight
}