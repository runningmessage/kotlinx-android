package com.runningmessage.kotlinx.common

import android.app.Activity
import android.app.Fragment
import android.os.Handler
import android.os.Looper
import android.view.View
import java.lang.ref.WeakReference

object UiThreadHandler : Handler(Looper.getMainLooper())

fun isMainThread() = Looper.getMainLooper().thread.id == Thread.currentThread().id

inline fun <R> runOnNewThread(crossinline block: () -> R) {
    Thread { block() }.start()
}

inline fun <R> postOnNewThread(delay: Long = 0, crossinline block: () -> R) {
    Handler().postDelayed(delay) { block() }
}

inline fun <R> runOnUiThread(target: Any? = null, crossinline block: () -> R) {
    Runnable { block() }.runOnUiThread(target)
}

inline fun <R> postOnUiThread(target: Any? = null, delay: Long = 0, crossinline block: () -> R) {
    Runnable { block() }.postOnUiThread(target, delay)
}

/***
 * make the task [Runnable] , to run on UI thread
 *
 *      1. immediately run the task, if current is on UI thread
 *      2. otherwise, to post the task into run queue, or maybe a ui thread handler
 */
fun <T : Runnable> T?.runOnUiThread(target: Any? = null) = this?.let { task ->
    if (isMainThread()) {
        task.run()
    } else {
        task.postOnUiThread(target)
    }
}

/***
 * post the task into run queue, or maybe a ui thread handler;
 * and the task will be to run after [delay] milliseconds ,
 * even if the [delay] is less than 0, it is still waiting the run queue to call running
 */
fun <T : Runnable> T?.postOnUiThread(target: Any? = null, delay: Long = 0) = this?.let { task ->

    val targetRef = WeakReference(target)

    when (target) {

        is Activity -> {
            UiThreadHandler.postDelayed(delay) {
                (targetRef.get() as? Activity)?.let { activity ->
                    if (!activity.isFinishing) {
                        task.run()
                    }
                }
            }
        }

        is View -> {
            target.postDelayed(task, delay)
        }

        is Fragment -> {
            UiThreadHandler.postDelayed(delay) {
                (targetRef.get() as? Fragment)?.let { fragment ->
                    if (!fragment.isRemoving) {
                        task.run()
                    }
                }
            }
        }

        FragmentClassSupport?.isInstance(target) == true -> {
            UiThreadHandler.postDelayed(delay) {
                try {
                    val targetGet = targetRef.get()
                    FragmentClassSupport?.cast(targetGet)?.let { fragment ->
                        if (FragmentIsRemovingSupport?.invoke(fragment) != false) {
                            task.run()
                        }
                    }
                } catch (ignored: Throwable) {
                }
            }
        }

        else -> UiThreadHandler.postDelayed(task, delay)
    }

}
