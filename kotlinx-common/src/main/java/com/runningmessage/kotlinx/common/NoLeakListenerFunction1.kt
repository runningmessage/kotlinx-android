package com.runningmessage.kotlinx.common


import java.lang.ref.WeakReference
import java.lang.reflect.Proxy

/***
 * wrap the Listener implementing interface , which only has one callback method, **Avoid Memory Leak !!!**
 */
class NoLeakListenerFunction1<Listener>(clazz: Class<Listener>, target: NoLeakTarget, action: Int) {

    private val targetRef = WeakReference<NoLeakTarget>(target)

    val wrapperListener: Listener? = try {
        Proxy.newProxyInstance(clazz.classLoader, arrayOf(clazz)) { _, method, args ->
            try {
                method.returnType.cast(targetRef.get()?.callbackNoLeak(action, args))
            } catch (e: Throwable) {
                if (method.returnType.isPrimitive) {
                    when (method.returnType) {
                        java.lang.Boolean.TYPE -> false
                        java.lang.Character.TYPE -> 0.toChar()
                        java.lang.Byte.TYPE -> 0.toByte()
                        java.lang.Short.TYPE -> 0.toShort()
                        java.lang.Integer.TYPE -> 0.toInt()
                        java.lang.Long.TYPE -> 0.toLong()
                        java.lang.Float.TYPE -> 0.toFloat()
                        java.lang.Double.TYPE -> 0.toDouble()
                        java.lang.Void.TYPE -> null
                        else -> null
                    }
                } else null
            }
        } as? Listener
    } catch (e: Throwable) {
        null
    }

}

interface NoLeakTarget {
    fun callbackNoLeak(action: Int, args: Array<Any?>?): Any?
}