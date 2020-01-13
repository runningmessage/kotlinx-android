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

package com.runningmessage.kotlinx.reflect

import com.runningmessage.kotlinx.common.ifNotNullOrBlank
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.*
import kotlin.reflect.full.isSubclassOf

/**
 * Created by Lorss on 20-1-7.
 */

@Throws(ReflectException::class)
operator fun <T> String.invoke(vararg args: Any?): T? = parseKClassByClassName(this)(*args)

@Throws(ReflectException::class)
operator fun <T> Class<*>.invoke(vararg args: Any?): T? = null

@Throws(ReflectException::class)
operator fun <T> KClass<*>.invoke(vararg args: Any?): T? {

    val types = parseKotlinTypes(*args)

    this.constructors.firstMatchFunction(types = types)?.let { constructor ->
        return constructor<T>(null, *args)
    }
            ?: throw ReflectException("Can not find the matched constructor in [this](value = $this)")
}

@Throws(ReflectException::class)
fun String.createInners(handler: (CallInnerHandler.() -> Unit)? = null) = createInner<Any>(handler)

@Throws(ReflectException::class)
fun <T> String.createInner(handler: (CallInnerHandler.() -> Unit)? = null): T? = parseKClassByClassName(this).createInner(handler)

@Throws(ReflectException::class)
fun Class<*>.createInners(handler: (CallInnerHandler.() -> Unit)? = null) = createInner<Any>(handler)

@Throws(ReflectException::class)
fun <T> Class<*>.createInner(handler: (CallInnerHandler.() -> Unit)? = null): T? = null

@Throws(ReflectException::class)
fun KClass<*>.createInners(handler: (CallInnerHandler.() -> Unit)? = null) = createInner<Any>(handler)

@Throws(ReflectException::class)
fun <T> KClass<*>.createInner(handler: (CallInnerHandler.() -> Unit)? = null): T? {

    val callInnerHandler = CallInnerHandler().apply {
        if (handler != null) handler()
    }
    try {
        return Proxy.newProxyInstance(this.java.classLoader, arrayOf(this.java)) { proxy, method, args ->
            return@newProxyInstance callInnerHandler.invoke(proxy, method, args)
        } as? T
    } catch (e: Throwable) {
        throw ReflectException(e)
    }
}


@Suppress("UNCHECKED_CAST")
@Throws(ReflectException::class)
operator fun <R> KCallable<*>.invoke(vararg args: Any?): R? {
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
            && it.parameters.sameAs(types.asList())
} as? KFunction<*>

private fun List<KParameter>.sameAs(that: List<KClass<*>>): Boolean {
    this.filter { it.kind == KParameter.Kind.VALUE }
            .also { if (it.size != that.size) return false }
            .forEachIndexed { index, kParameter ->
                if (that[index] == NULL::class) return@forEachIndexed
                if (that[index] != kParameter.type.classifier
                        && (kParameter.type.classifier as? KClass<*>)?.let { !that[index].isSubclassOf(it) } != false) return false
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

typealias InnerHandler = (args: Array<out Any?>?) -> Any?

class CallInnerHandler {

    private val handlerMap = mutableMapOf<String, InnerHandler>()

    fun override(methodName: String, handler: InnerHandler) {
        handlerMap[methodName] = handler
    }

    fun invoke(proxy: Any?, method: Method?, args: Array<out Any?>?): Any? {
        method?.let { methodNo ->

            handlerMap[methodNo.name]?.let { handler ->
                return handler(args)
            }
        }

        return Unit
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