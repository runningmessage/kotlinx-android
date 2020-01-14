/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("UNCHECKED_CAST")

package com.runningmessage.kotlinx.reflect

import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

/**
 * Created by Lorss on 20-1-13.
 */

interface ICallHandlerFunction<P> {

    fun call(proxy: Any?, args: Array<out Any?>?) {
        onCall((proxy as? P), args)
    }

    fun onCall(that: P?, args: Array<out Any?>?): Any?

    val parameterTypes: List<KClass<*>>?
        get() = emptyList()

    val parseParameterTypes: List<KClass<*>>?
        get() = emptyList()
}

abstract class AbsCallHandlerFunction<P> : ICallHandlerFunction<P> {

    private var mParameterTypes: List<KClass<*>>? = null
    private var mParsed = false

    override val parameterTypes: List<KClass<*>>?
        get() {
            if (!mParsed) {
                mParameterTypes = parseParameterTypes
                mParsed = true
            }
            return mParameterTypes
        }

    override val parseParameterTypes: List<KClass<*>>?
        get() = null
}

class CallHandlerFunction<P>(val handler: P?.(args: Array<out Any?>?) -> Any?) : AbsCallHandlerFunction<P>() {
    override fun onCall(that: P?, args: Array<out Any?>?): Any? {
        return that.handler(args)
    }

    override val parseParameterTypes: List<KClass<*>>?
        get() = null
}

class CallHandlerFunction0<P>(val handler: P?.() -> Any?) : AbsCallHandlerFunction<P>() {
    override fun onCall(that: P?, args: Array<out Any?>?): Any? {
        return that.handler()
    }

    override val parseParameterTypes: List<KClass<*>>?
        get() = emptyList()
}

private fun parseParameterizedTypes(subClassInstance: Any): List<KClass<*>>? {
    val javaTypeArr = (subClassInstance.javaClass.genericSuperclass as? ParameterizedType)?.actualTypeArguments
    return when {
        javaTypeArr == null -> null
        javaTypeArr.isEmpty() -> emptyList()
        javaTypeArr.isNotEmpty() -> javaTypeArr.map { (it as? Class<*>)?.kotlin }.let {
            return if (it.contains(null)) null else it.map { it!! }
        }
        else -> null
    }
}

private fun <T> List<T>.dropFirst(): List<T> =
        if (this.isEmpty()) this
        else List(size - 1) { this[it + 1] }


open class CallHandlerFunction1<P, P1>(val handler: P?.(param1: P1?) -> Any?) : AbsCallHandlerFunction<P>() {
    override fun onCall(that: P?, args: Array<out Any?>?): Any? {
        if (args == null || args.isEmpty()) return null
        return that.handler(args[0] as? P1)
    }

    override val parseParameterTypes: List<KClass<*>>?
        get() = parseParameterizedTypes(object : CallHandlerFunction1<P, P1>({}) {})?.dropFirst()
}

open class CallHandlerFunction2<P, P1, P2>(val handler: P?.(param1: P1?, param2: P2?) -> Any?) : AbsCallHandlerFunction<P>() {
    override fun onCall(that: P?, args: Array<out Any?>?): Any? {
        if (args == null || args.size < 2) return null
        return that.handler(args[0] as? P1, args[1] as? P2)
    }

    override val parseParameterTypes: List<KClass<*>>?
        get() = parseParameterizedTypes(object : CallHandlerFunction2<P, P1, P2>({ _, _ -> }) {})?.dropFirst()
}

open class CallHandlerFunction3<P, P1, P2, P3>(val handler: P?.(param1: P1?, param2: P2?, param3: P3?) -> Any?) : AbsCallHandlerFunction<P>() {
    override fun onCall(that: P?, args: Array<out Any?>?): Any? {
        if (args == null || args.size < 3) return null
        return that.handler(args[0] as? P1, args[1] as? P2, args[2] as? P3)
    }

    override val parseParameterTypes: List<KClass<*>>?
        get() = parseParameterizedTypes(object : CallHandlerFunction3<P, P1, P2, P3>({ _, _, _ -> }) {})?.dropFirst()
}

open class CallHandlerFunction4<P, P1, P2, P3, P4>(val handler: P?.(param1: P1?, param2: P2?, param3: P3?, param4: P4?) -> Any?) : AbsCallHandlerFunction<P>() {
    override fun onCall(that: P?, args: Array<out Any?>?): Any? {
        if (args == null || args.size < 4) return null
        return that.handler(args[0] as? P1, args[1] as? P2, args[2] as? P3, args[3] as? P4)
    }

    override val parseParameterTypes: List<KClass<*>>?
        get() = parseParameterizedTypes(object : CallHandlerFunction4<P, P1, P2, P3, P4>({ _, _, _, _ -> }) {})?.dropFirst()
}

open class CallHandlerFunction5<P, P1, P2, P3, P4, P5>(val handler: P?.(param1: P1?, param2: P2?, param3: P3?, param4: P4?, param5: P5?) -> Any?) : AbsCallHandlerFunction<P>() {
    override fun onCall(that: P?, args: Array<out Any?>?): Any? {
        if (args == null || args.size < 5) return null
        return that.handler(args[0] as? P1, args[1] as? P2, args[2] as? P3, args[3] as? P4, args[4] as? P5)
    }

    override val parseParameterTypes: List<KClass<*>>?
        get() = parseParameterizedTypes(object : CallHandlerFunction5<P, P1, P2, P3, P4, P5>({ _, _, _, _, _ -> }) {})?.dropFirst()
}

open class CallHandlerFunction6<P, P1, P2, P3, P4, P5, P6>(val handler: P?.(param1: P1?, param2: P2?, param3: P3?, param4: P4?, param5: P5?, param6: P6?) -> Any?) : AbsCallHandlerFunction<P>() {
    override fun onCall(that: P?, args: Array<out Any?>?): Any? {
        if (args == null || args.size < 6) return null
        return that.handler(args[0] as? P1, args[1] as? P2, args[2] as? P3, args[3] as? P4, args[4] as? P5, args[5] as? P6)
    }

    override val parseParameterTypes: List<KClass<*>>?
        get() = parseParameterizedTypes(object : CallHandlerFunction6<P, P1, P2, P3, P4, P5, P6>({ _, _, _, _, _, _ -> }) {})?.dropFirst()
}

open class CallHandlerFunction7<P, P1, P2, P3, P4, P5, P6, P7>(val handler: P?.(param1: P1?, param2: P2?, param3: P3?, param4: P4?, param5: P5?, param6: P6?, param7: P7?) -> Any?) : AbsCallHandlerFunction<P>() {
    override fun onCall(that: P?, args: Array<out Any?>?): Any? {
        if (args == null || args.size < 7) return null
        return that.handler(args[0] as? P1, args[1] as? P2, args[2] as? P3, args[3] as? P4, args[4] as? P5, args[5] as? P6, args[6] as? P7)
    }

    override val parseParameterTypes: List<KClass<*>>?
        get() = parseParameterizedTypes(object : CallHandlerFunction7<P, P1, P2, P3, P4, P5, P6, P7>({ _, _, _, _, _, _, _ -> }) {})?.dropFirst()
}

open class CallHandlerFunction8<P, P1, P2, P3, P4, P5, P6, P7, P8>(val handler: P?.(param1: P1?, param2: P2?, param3: P3?, param4: P4?, param5: P5?, param6: P6?, param7: P7?, param8: P8?) -> Any?) : AbsCallHandlerFunction<P>() {
    override fun onCall(that: P?, args: Array<out Any?>?): Any? {
        if (args == null || args.size < 8) return null
        return that.handler(args[0] as? P1, args[1] as? P2, args[2] as? P3, args[3] as? P4, args[4] as? P5, args[5] as? P6, args[6] as? P7, args[7] as? P8)
    }

    override val parseParameterTypes: List<KClass<*>>?
        get() = parseParameterizedTypes(object : CallHandlerFunction8<P, P1, P2, P3, P4, P5, P6, P7, P8>({ _, _, _, _, _, _, _, _ -> }) {})?.dropFirst()
}

open class CallHandlerFunction9<P, P1, P2, P3, P4, P5, P6, P7, P8, P9>(val handler: P?.(param1: P1?, param2: P2?, param3: P3?, param4: P4?, param5: P5?, param6: P6?, param7: P7?, param8: P8?, param9: P9?) -> Any?) : AbsCallHandlerFunction<P>() {
    override fun onCall(that: P?, args: Array<out Any?>?): Any? {
        if (args == null || args.size < 9) return null
        return that.handler(args[0] as? P1, args[1] as? P2, args[2] as? P3, args[3] as? P4, args[4] as? P5, args[5] as? P6, args[6] as? P7, args[7] as? P8, args[8] as? P9)
    }

    override val parseParameterTypes: List<KClass<*>>?
        get() = parseParameterizedTypes(object : CallHandlerFunction9<P, P1, P2, P3, P4, P5, P6, P7, P8, P9>({ _, _, _, _, _, _, _, _, _ -> }) {})?.dropFirst()
}

open class CallHandlerFunction10<P, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10>(val handler: P?.(param1: P1?, param2: P2?, param3: P3?, param4: P4?, param5: P5?, param6: P6?, param7: P7?, param8: P8?, param9: P9?, param10: P10?) -> Any?) : AbsCallHandlerFunction<P>() {
    override fun onCall(that: P?, args: Array<out Any?>?): Any? {
        if (args == null || args.size < 9) return null
        return that.handler(args[0] as? P1, args[1] as? P2, args[2] as? P3, args[3] as? P4, args[4] as? P5, args[5] as? P6, args[6] as? P7, args[7] as? P8, args[8] as? P9, args[9] as? P10)
    }

    override val parseParameterTypes: List<KClass<*>>?
        get() = parseParameterizedTypes(object : CallHandlerFunction10<P, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10>({ _, _, _, _, _, _, _, _, _, _ -> }) {})?.dropFirst()
}