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

@file:Suppress("unused")

package com.runningmessage.kotlinx.reflect

import com.runningmessage.kotlinx.common.ifNotNullOrBlank
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.*
import kotlin.reflect.full.isSubclassOf

/**
 * Some functions to make reflective calls.
 *
 * An example comes at below:
 *
 * [Demo]. [Code].
 *
 * ```java
 *      import com.runningmessage.kotlinx.reflect.*
 *
 *      lateinit var context: Context
 *
 *      val Builder = "android.support.v7.app.AlertDialog${'$'}Builder"
 *      val OnClickListener = "android.content.DialogInterface${'$'}OnClickListener"
 *
 *      Builder(context)
 *             .calls("setTitle")("Hello World")
 *             .calls("setPositiveButton")("OK", OnClickListener.createInners{
 *
 *                  override<Any, Int>("onClick"){ dialog, which ->
 *
 *                  }
 *              })
 *             .calls("create")
 *             .calls("show")
 *
 * ```
 *
 * Created by Lorss on 20-1-7.
 */
val README = Unit
private val Demo = README
private val Code = README

/**
 *  Call a matched constructor of the [KClass], which class name is same as the receiver [String] name;
 *  then try casting the return value to type [T].
 *
 *  [Demo]. [Code].
 *
 *  ```java
 *
 *      val Builder: String = "android.support.v7.app.AlertDialog${'$'}Builder"
 *
 *      val builder: Any? = Builder(context)
 *
 *   ```
 *
 *  @param args the parameters when call constructor
 */
@Throws(ReflectException::class)
operator fun <T> String.invoke(vararg args: Any?): T? = parseKClassByClassName(this)(*args)

/**
 *  Call a matched constructor of the [Class] and try casting the return value to type [T].
 *
 *  [Demo]. [Code].
 *
 *  ```java
 *
 *      val Builder: Class<*> = Class.forName("android.support.v7.app.AlertDialog${'$'}Builder")
 *
 *      val builder: Any? = Builder(context)
 *
 *   ```
 *
 *  @param args the parameters when call constructor
 */
@Throws(ReflectException::class)
operator fun <T> Class<*>.invoke(vararg args: Any?): T? = this.kotlin.invoke(*args)

/**
 *  Call a matched constructor of the [KClass] and try casting the return value to type [T].
 *
 *  [Demo]. [Code].
 *
 *  ```java
 *
 *      val Builder: KClass<*> = Class.forName("android.support.v7.app.AlertDialog${'$'}Builder").kotlin
 *
 *      val builder: Any? = Builder(context)
 *
 *   ```
 *
 *  @param args the parameters when call constructor
 */
@Throws(ReflectException::class)
operator fun <T> KClass<*>.invoke(vararg args: Any?): T? {

    val types = parseKotlinTypes(*args)

    this.constructors.firstMatchFunction(types = types)?.let { constructor ->
        return constructor<T>(null, *args)
    }
            ?: throw ReflectException("Can not find the matched constructor in [this](value = $this)")
}

/**
 *  Create an anonymous class instance by calling [Proxy.newProxyInstance] using the interface [KClass], which class name is same as the receiver [String] name;
 *  the type of the return value is [Any]?.
 *
 *  [Demo]. [Code].
 *
 *  ```java
 *
 *      val OnClickListener = "android.content.DialogInterface${'$'}OnClickListener"
 *
 *      val listener: Any? = OnClickListener.createInners{
 *
 *           override<Any, Int>("onClick"){ dialog, which ->
 *
 *           }
 *      }
 *
 *   ```
 *  @param handler the lambda parameter when create anonymous inner class instance,
 *  in which can call [CallInnerHandler.override] to implement methods of interface
 */
@Throws(ReflectException::class)
fun String.createInners(handler: (CallInnerHandler<Any>.() -> Unit)? = null) = createInner(handler)

/**
 *  Create an anonymous class instance by calling [Proxy.newProxyInstance] using the interface [KClass], which class name is same as the receiver [String] name;
 *  then try casting the return value to type [T].
 *
 *  [Demo]. [Code].
 *
 *  ```java
 *
 *      val OnClickListener = "android.content.DialogInterface${'$'}OnClickListener"
 *
 *      val listener: Any? = OnClickListener.createInner{
 *
 *           override<Any, Int>("onClick"){ dialog, which ->
 *
 *           }
 *      }
 *
 *   ```
 *  @param handler the lambda parameter when create anonymous inner class instance,
 *  in which can call [CallInnerHandler.override] to implement methods of interface
 */
@Throws(ReflectException::class)
fun <T> String.createInner(handler: (CallInnerHandler<T>.() -> Unit)? = null): T? = parseKClassByClassName(this).createInner(handler)

/**
 *  Create an anonymous class instance by calling [Proxy.newProxyInstance] using the interface [Class];
 *  the type of the return value is [Any]?.
 *
 *  [Demo]. [Code].
 *
 *  ```java
 *
 *      val OnClickListener = Class.forName("android.content.DialogInterface${'$'}OnClickListener")
 *
 *      val listener: Any? = OnClickListener.createInners{
 *
 *           override<Any, Int>("onClick"){ dialog, which ->
 *
 *           }
 *      }
 *
 *   ```
 *  @param handler the lambda parameter when create anonymous inner class instance,
 *  in which can call [CallInnerHandler.override] to implement methods of interface
 */
@Throws(ReflectException::class)
fun Class<*>.createInners(handler: (CallInnerHandler<Any>.() -> Unit)? = null) = createInner(handler)

/**
 *  Create an anonymous class instance by calling [Proxy.newProxyInstance] using the interface [Class];
 *  then try casting the return value to type [T].
 *
 *  [Demo]. [Code].
 *
 *  ```java
 *
 *      val OnClickListener = Class.forName("android.content.DialogInterface${'$'}OnClickListener")
 *
 *      val listener: Any? = OnClickListener.createInners{
 *
 *           override<Any, Int>("onClick"){ dialog, which ->
 *
 *           }
 *      }
 *
 *   ```
 *  @param handler the lambda parameter when create anonymous inner class instance,
 *  in which can call [CallInnerHandler.override] to implement methods of interface
 */
@Throws(ReflectException::class)
fun <T> Class<*>.createInner(handler: (CallInnerHandler<T>.() -> Unit)? = null): T? = this.kotlin.createInner(handler)

/**
 *  Create an anonymous class instance by calling [Proxy.newProxyInstance] using the interface [KClass];
 *  the type of the return value is [Any]?.
 *
 *  [Demo]. [Code].
 *
 *  ```java
 *
 *      val OnClickListener = Class.forName("android.content.DialogInterface${'$'}OnClickListener").kotlin
 *
 *      val listener: Any? = OnClickListener.createInners{
 *
 *           override<Any, Int>("onClick"){ dialog, which ->
 *
 *           }
 *      }
 *
 *   ```
 *  @param handler the lambda parameter when create anonymous inner class instance,
 *  in which can call [CallInnerHandler.override] to implement methods of interface
 */
@Throws(ReflectException::class)
fun KClass<*>.createInners(handler: (CallInnerHandler<Any>.() -> Unit)? = null) = createInner(handler)

/**
 *  Create an anonymous class instance by calling [Proxy.newProxyInstance] using the interface [KClass];
 *  then try casting the return value to type [T].
 *
 *  [Demo]. [Code].
 *
 *  ```java
 *
 *      val OnClickListener = Class.forName("android.content.DialogInterface${'$'}OnClickListener").kotlin
 *
 *      val listener: Any? = OnClickListener.createInners{
 *
 *           override<Any, Int>("onClick"){ dialog, which ->
 *
 *           }
 *      }
 *
 *   ```
 *  @param handler the lambda parameter when create anonymous inner class instance,
 *  in which can call [CallInnerHandler.override] to implement methods of interface
 */
@Suppress("UNCHECKED_CAST")
@Throws(ReflectException::class)
fun <T> KClass<*>.createInner(handler: (CallInnerHandler<T>.() -> Unit)? = null): T? {

    val callInnerHandler = CallInnerHandler<T>().apply {
        if (handler != null) handler()
    }
    return try {
        Proxy.newProxyInstance(this.java.classLoader, arrayOf(this.java)) { proxy, method, args ->
            return@newProxyInstance callInnerHandler.invoke(proxy, method, args)
        } as? T
    } catch (e: Throwable) {
        throw ReflectException(e)
    }
}

@Suppress("UNCHECKED_CAST")
@Throws(ReflectException::class)
private operator fun <R> KCallable<*>.invoke(vararg args: Any?): R? {
    try {
        return when (this.parameters.firstOrNull()?.kind) {
            KParameter.Kind.INSTANCE -> {
                this.call(*args) as R?
            }
            KParameter.Kind.EXTENSION_RECEIVER -> {
                this.call(*args) as R?
            }
            else -> {
                this.call(*(args.dropFirst())) as R?
            }
        }
    } catch (e: Throwable) {
        throw ReflectException(e)
    }
}

@Throws(ReflectException::class)
fun calls(callableName: String?) = call<Any>(callableName)

@Throws(ReflectException::class)
fun <R> call(callableName: String?) = null.call<R>(callableName)

@Throws(ReflectException::class)
fun Any?.calls(callableName: String?) = this.call<Any>(callableName)

@Throws(ReflectException::class)
fun <R> Any?.call(callableName: String?): CallMedia<R> = callableName.ifNotNullOrBlank { fullCallableName ->

    return (if (fullCallableName.contains(".")) fullCallableName.substringAfterLast(".") else fullCallableName).ifNotNullOrBlank simple@{ simpleCallableName ->
        return@simple CallMedia<R>(
                parseKClassByInstance(this, callableName),
                this,
                simpleCallableName)
    }
            ?: throw ReflectException("The [simpleCallableName] for param [callableName](value = $callableName) can not be null or blank")

}
        ?: throw ReflectException("The param [callableName](value = $callableName) can not be null or blank")

@Throws(ReflectException::class)
fun propertys(propertyName: String?) = property<Any>(propertyName)

@Throws(ReflectException::class)
fun <R : Any> property(propertyName: String?) = null.property<R>(propertyName)

@Throws(ReflectException::class)
fun Any?.propertys(propertyName: String?) = this.property<Any>(propertyName)

@Throws(ReflectException::class)
fun <R : Any> Any?.property(propertyName: String?): CallProperty<R> = propertyName.ifNotNullOrBlank { fullPropertyName ->

    return (if (fullPropertyName.contains(".")) fullPropertyName.substringAfterLast(".") else fullPropertyName).ifNotNullOrBlank simple@{ simplePropertyName ->
        return@simple CallProperty<R>(
                parseKClassByInstance(this, propertyName),
                this,
                simplePropertyName)
    }
            ?: throw ReflectException("The [simplePropertyName] for param [propertyName](value = $propertyName) can not be null or blank")

}
        ?: throw ReflectException("The param [propertyName](value = $propertyName) can not be null or blank")

@Suppress("UNCHECKED_CAST")
@Throws(ReflectException::class)
private fun parseKClassByClassName(className: String): KClass<*> = className.ifNotNullOrBlank {
    try {
        return@ifNotNullOrBlank Class.forName(className).kotlin
    } catch (e: Throwable) {
        throw ReflectException(e)
    }
} ?: throw ReflectException("The param [className](value = $className) can not be null or blank")

@Throws(ReflectException::class)
private fun parseKClassByInstance(instance: Any?, callableName: String? = null): KClass<*> = instance?.let {
    try {
        return@let it::class
    } catch (e: Throwable) {
        throw ReflectException(e)
    }
} ?: callableName.ifNotNullOrBlank {
    return@ifNotNullOrBlank parseKClassByCallableName(it)
}
?: throw ReflectException("The param [callableName](value = $callableName) can not be null or blank when [instance] is null")

@Throws(ReflectException::class)
private fun parseKClassByCallableName(callableName: String): KClass<*> {
    if (callableName.contains(".")) {
        return parseKClassByClassName(callableName.substringBeforeLast("."))
    } else {
        throw ReflectException("Can not parse KClass from callableName(value = $callableName)")
    }
}

@Throws(ReflectException::class)
private fun parseKProperty(clazz: KClass<*>, simplePropertyName: String, type: KClass<*>? = null): KProperty<*>? {
    try {
        return clazz.members.firstMatchProperty(simplePropertyName, type)
    } catch (e: Throwable) {
        throw ReflectException(e)
    }
}

@Throws(ReflectException::class)
private fun parseKFunction(clazz: KClass<*>, simpleCallableName: String, types: Array<KClass<*>>): KFunction<*>? {
    try {
        return clazz.members.firstMatchFunction(simpleCallableName, types)
    } catch (e: Throwable) {
        throw ReflectException(e)
    }
}

private fun Collection<KCallable<*>>.firstMatchProperty(simpleCallableName: String, type: KClass<*>? = null) = this.firstOrNull {
    it is KProperty
            && it.name == simpleCallableName
            && (type == null
            || type == it.getter.returnType.classifier
            || (type == NULL::class)
            || (it.getter.returnType.classifier as? KClass<*>)?.let { typeClass -> type.isSubclassOf(typeClass) } == true)
} as? KProperty<*>

private fun Collection<KCallable<*>>.firstMatchFunction(simpleCallableName: String? = null, types: Array<KClass<*>>) = this.firstOrNull {
    it is KFunction
            && (simpleCallableName == null || it.name == simpleCallableName)
            && it.parameters.sameAsOrSuperOf(types.asList())
} as? KFunction<*>

private fun List<KParameter>.sameAsOrSuperOf(that: List<KClass<*>>): Boolean {
    this.filter { it.kind == KParameter.Kind.VALUE }
            .also { if (it.size != that.size) return false }
            .forEachIndexed { index, kParameter ->
                if (that[index] == NULL::class) return@forEachIndexed
                if (that[index] != kParameter.type.classifier
                        && (kParameter.type.classifier as? KClass<*>)?.let { !that[index].isSubclassOf(it) } != false) return false
            }
    return true
}

private fun List<Class<*>>.sameAsOrSubOf(that: List<KClass<*>>): Boolean {
    this.also { if (it.size != that.size) return false }
            .forEachIndexed { index, parameterClass ->
                if (that[index] != parameterClass
                        && (!parameterClass.isAssignableFrom(that[index].java))) return false
            }
    return true
}

private fun <T> Array<T>.dropFirst(): Array<T> =
        if (this.isEmpty()) this
        else this.copyOfRange(1, this.size)

private fun parseKotlinTypes(vararg values: Any?): Array<KClass<*>> {
    if (values.isEmpty()) {
        return emptyArray()
    }

    val result = Array<KClass<*>>(values.size) { NULL::class }
    values.forEachIndexed { index, value ->
        value?.also { result[index] = it::class }
    }
    return result
}

class CallInnerHandler<P> {

    private val handlerMap = HandlerMap<P>()

    class HandlerMap<P> {

        private val mCallHandlerMap = mutableMapOf<String, ArrayList<ICallHandlerFunction<P>>>()

        operator fun set(key: String, value: ICallHandlerFunction<P>) {
            mCallHandlerMap[key]
                    ?: ArrayList<ICallHandlerFunction<P>>().apply { mCallHandlerMap[key] = this }.let { list ->
                        if (!list.contains(value)) list.add(value)
                    }
        }

        operator fun get(method: Method): ICallHandlerFunction<P>? {
            mCallHandlerMap[method.name]?.let { list ->

                var targetHandler: ICallHandlerFunction<P>? = null

                list.forEach { handler ->
                    if (handler.parameterTypes == null) {
                        if (targetHandler == null) targetHandler = handler
                    } else {
                        val parameterTypes = handler.parameterTypes
                        if (parameterTypes != null && method.parameterTypes.toList().sameAsOrSubOf(parameterTypes)) targetHandler = handler
                    }
                }
                return targetHandler
            }
            return null
        }
    }

    fun invoke(proxy: Any?, method: Method?, args: Array<out Any?>?): Any? {
        method?.let { methodNo ->
            handlerMap[methodNo]?.let { handler ->
                return handler.call(proxy, args)
            }
        }
        return null
    }

    fun override(methodName: String, handler: CallHandlerFunction<P>) {
        handlerMap[methodName] = handler
    }

    fun override(methodName: String, handler: P?.() -> Any?) {
        handlerMap[methodName] = CallHandlerFunction0 {
            return@CallHandlerFunction0 this.handler()
        }
    }

    fun <P1> override(methodName: String, handler: P?.(param1: P1?) -> Any?) {
        handlerMap[methodName] = CallHandlerFunction1<P, P1> { param1 ->
            return@CallHandlerFunction1 this.handler(param1)
        }
    }

    fun <P1, P2> override(methodName: String, handler: P?.(param1: P1?, param2: P2?) -> Any?) {
        handlerMap[methodName] = CallHandlerFunction2<P, P1, P2> { param1, param2 ->
            return@CallHandlerFunction2 this.handler(param1, param2)
        }
    }

    fun <P1, P2, P3> override(methodName: String, handler: P?.(param1: P1?, param2: P2?, param3: P3?) -> Any?) {
        handlerMap[methodName] = CallHandlerFunction3<P, P1, P2, P3> { param1, param2, param3 ->
            return@CallHandlerFunction3 this.handler(param1, param2, param3)
        }
    }

    fun <P1, P2, P3, P4> override(methodName: String, handler: P?.(param1: P1?, param2: P2?, param3: P3?, param4: P4?) -> Any?) {
        handlerMap[methodName] = CallHandlerFunction4<P, P1, P2, P3, P4> { param1, param2, param3, param4 ->
            return@CallHandlerFunction4 this.handler(param1, param2, param3, param4)
        }
    }

    fun <P1, P2, P3, P4, P5> override(methodName: String, handler: P?.(param1: P1?, param2: P2?, param3: P3?, param4: P4?, param5: P5?) -> Any?) {
        handlerMap[methodName] = CallHandlerFunction5<P, P1, P2, P3, P4, P5> { param1, param2, param3, param4, param5 ->
            return@CallHandlerFunction5 this.handler(param1, param2, param3, param4, param5)
        }
    }

    fun <P1, P2, P3, P4, P5, P6> override(methodName: String, handler: P?.(param1: P1?, param2: P2?, param3: P3?, param4: P4?, param5: P5?, param6: P6?) -> Any?) {
        handlerMap[methodName] = CallHandlerFunction6<P, P1, P2, P3, P4, P5, P6> { param1, param2, param3, param4, param5, param6 ->
            return@CallHandlerFunction6 this.handler(param1, param2, param3, param4, param5, param6)
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7> override(methodName: String, handler: P?.(param1: P1?, param2: P2?, param3: P3?, param4: P4?, param5: P5?, param6: P6?, param7: P7?) -> Any?) {
        handlerMap[methodName] = CallHandlerFunction7<P, P1, P2, P3, P4, P5, P6, P7> { param1, param2, param3, param4, param5, param6, param7 ->
            return@CallHandlerFunction7 this.handler(param1, param2, param3, param4, param5, param6, param7)
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8> override(methodName: String, handler: P?.(param1: P1?, param2: P2?, param3: P3?, param4: P4?, param5: P5?, param6: P6?, param7: P7?, param8: P8?) -> Any?) {
        handlerMap[methodName] = CallHandlerFunction8<P, P1, P2, P3, P4, P5, P6, P7, P8> { param1, param2, param3, param4, param5, param6, param7, param8 ->
            return@CallHandlerFunction8 this.handler(param1, param2, param3, param4, param5, param6, param7, param8)
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8, P9> override(methodName: String, handler: P?.(param1: P1?, param2: P2?, param3: P3?, param4: P4?, param5: P5?, param6: P6?, param7: P7?, param8: P8?, param9: P9?) -> Any?) {
        handlerMap[methodName] = CallHandlerFunction9<P, P1, P2, P3, P4, P5, P6, P7, P8, P9> { param1, param2, param3, param4, param5, param6, param7, param8, param9 ->
            return@CallHandlerFunction9 this.handler(param1, param2, param3, param4, param5, param6, param7, param8, param9)
        }
    }

    fun <P1, P2, P3, P4, P5, P6, P7, P8, P9, P10> override(methodName: String, handler: P?.(param1: P1?, param2: P2?, param3: P3?, param4: P4?, param5: P5?, param6: P6?, param7: P7?, param8: P8?, param9: P9?, param10: P10?) -> Any?) {
        handlerMap[methodName] = CallHandlerFunction10<P, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10> { param1, param2, param3, param4, param5, param6, param7, param8, param9, param10 ->
            return@CallHandlerFunction10 this.handler(param1, param2, param3, param4, param5, param6, param7, param8, param9, param10)
        }
    }
}

abstract class CallInvoke<N> {

    abstract fun beforeNextCall(): N?

    @Throws(ReflectException::class)
    fun calls(callableName: String?) = this.call<Any>(callableName)

    @Throws(ReflectException::class)
    fun <R> call(callableName: String?): CallMedia<R> = beforeNextCall().call(callableName)

    @Throws(ReflectException::class)
    fun propertys(propertyName: String?) = this.property<Any>(propertyName)

    @Throws(ReflectException::class)
    fun <R : Any> property(propertyName: String?): CallProperty<R> = beforeNextCall().property(propertyName)
}

class CallMedia<R>(
        private val kotlinClass: KClass<*>,
        private val instance: Any?,
        private val simpleCallableName: String
) : CallInvoke<R>() {

    override fun beforeNextCall() = invoke()

    @Throws(ReflectException::class)
    operator fun invoke(vararg args: Any?): R? {
        val types = parseKotlinTypes(*args)
        val callable = parseKFunction(kotlinClass, simpleCallableName, types)
        callable?.let { callableNo ->
            return callableNo<R>(instance, *args)
        }
                ?: throw ReflectException("Can not find the matched callable for [simpleCallableName](value = $simpleCallableName) in [kotlinClass](value = $kotlinClass)")
    }
}

class CallProperty<R : Any>(
        private val kotlinClass: KClass<*>,
        private val instance: Any?,
        private val simplePropertyName: String
) : CallInvoke<R>() {

    override fun beforeNextCall(): R? = value

    var value: R?
        @Throws(ReflectException::class)
        set(value) {
            val property = parseKProperty(kotlinClass, simplePropertyName, if (value != null) value::class else null)
            property?.let { propertyNo ->
                if (propertyNo is KMutableProperty) {
                    propertyNo.setter<Unit>(instance, value)
                }
            }
                    ?: throw ReflectException("Can not find the matched property for [simplePropertyName](value = $simplePropertyName) in [kotlinClass](value = $kotlinClass)")
        }
        @Throws(ReflectException::class)
        get() {
            val property = parseKProperty(kotlinClass, simplePropertyName)
            property?.let { propertyNo ->
                return propertyNo.getter<R>(instance)
            }
                    ?: throw ReflectException("Can not find the matched property for [simplePropertyName](value = $simplePropertyName) in [kotlinClass](value = $kotlinClass)")
        }

}

private class NULL