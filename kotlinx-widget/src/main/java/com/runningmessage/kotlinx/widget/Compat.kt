package com.runningmessage.kotlinx.widget

import android.os.Build
import android.widget.ListView

/**
 * Created by Lorss on 19-2-27.
 */

class ListViewCompat {

    companion object {

        /**
         * Check if the items in the list can be scrolled in a certain direction.
         *
         * @param direction Negative to check scrolling up, positive to check
         * scrolling down.
         * @return true if the list can be scrolled in the specified direction,
         * false otherwise.
         * @see .scrollListBy
         */
        @JvmStatic
        fun canScrollList(listView: ListView, direction: Int): Boolean {
            if (Build.VERSION.SDK_INT >= 19) {
                // Call the framework version directly
                return listView.canScrollList(direction)
            } else {
                // provide backport on earlier versions
                val childCount = listView.childCount
                if (childCount == 0) {
                    return false
                }

                val firstPosition = listView.firstVisiblePosition
                if (direction > 0) {
                    val lastBottom = listView.getChildAt(childCount - 1).bottom
                    val lastPosition = firstPosition + childCount
                    return lastPosition < listView.count || lastBottom > listView.height - listView.listPaddingBottom
                } else {
                    val firstTop = listView.getChildAt(0).top
                    return firstPosition > 0 || firstTop < listView.listPaddingTop
                }
            }
        }

    }
}
