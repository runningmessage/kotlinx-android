package com.runningmessage.kotlin.ext.widget

/**
 * Created by Lorss on 19-2-27.
 */
interface LoadMoreFooter {


    val viewWidth: Int

    val viewHeight: Int

    /***
     * keep some message , which help to update ui, for example , show different ui for [finishLoading]
     */
    var message: String

    /***
     *  to update ui when nothing happens for load more
     */
    fun showIdle()

    /***
     *  to update ui when the footer keeps completely visible
     */
    fun showFullToLoad()

    /***
     *  to update ui when the footer comes visible in the window , but cannot trigger load more
     */
    fun showPullToLoad()

    /***
     *  to update ui when the footer comes completely visible in the window , and at current time it is enable to trigger load more when release
     */
    fun showReleaseToLoad()

    /***
     *  to update ui when it is loading more
     */
    fun showLoading()

    /***
     *  to update ui after it has loaded more
     */
    fun finishLoading()


}