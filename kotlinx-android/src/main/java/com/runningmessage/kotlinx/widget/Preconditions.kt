/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.runningmessage.kotlinx.widget

import android.support.annotation.IntRange
import android.support.annotation.RestrictTo
import android.text.TextUtils
import java.util.*

/**
 * Simple static methods to be called at the start of your own methods to verify
 * correct arguments and state.
 *
 * @hide
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
object Preconditions {
    fun checkArgument(expression: Boolean) {
        if (!expression) {
            throw IllegalArgumentException()
        }
    }

    /**
     * Ensures that an expression checking an argument is true.
     *
     * @param expression the expression to check
     * @param errorMessage the exception message to use if the check fails; will
     * be converted to a string using [String.valueOf]
     * @throws IllegalArgumentException if `expression` is false
     */
    fun checkArgument(expression: Boolean, errorMessage: Any) {
        if (!expression) {
            throw IllegalArgumentException(errorMessage.toString())
        }
    }

    /**
     * Ensures that an string reference passed as a parameter to the calling
     * method is not empty.
     *
     * @param string an string reference
     * @return the string reference that was validated
     * @throws IllegalArgumentException if `string` is empty
     */
    fun <T : CharSequence> checkStringNotEmpty(string: T): T {
        if (TextUtils.isEmpty(string)) {
            throw IllegalArgumentException()
        }
        return string
    }

    /**
     * Ensures that an string reference passed as a parameter to the calling
     * method is not empty.
     *
     * @param string an string reference
     * @param errorMessage the exception message to use if the check fails; will
     * be converted to a string using [String.valueOf]
     * @return the string reference that was validated
     * @throws IllegalArgumentException if `string` is empty
     */
    fun <T : CharSequence> checkStringNotEmpty(
        string: T,
        errorMessage: Any
    ): T {
        if (TextUtils.isEmpty(string)) {
            throw IllegalArgumentException(errorMessage.toString())
        }
        return string
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling
     * method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if `reference` is null
     */
    fun <T> checkNotNull(reference: T?): T {
        if (reference == null) {
            throw NullPointerException()
        }
        return reference
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling
     * method is not null.
     *
     * @param reference an object reference
     * @param errorMessage the exception message to use if the check fails; will
     * be converted to a string using [String.valueOf]
     * @return the non-null reference that was validated
     * @throws NullPointerException if `reference` is null
     */
    fun <T> checkNotNull(reference: T?, errorMessage: Any): T {
        if (reference == null) {
            throw NullPointerException(errorMessage.toString())
        }
        return reference
    }

    /**
     * Ensures the truth of an expression involving the state of the calling
     * instance, but not involving any parameters to the calling method.
     *
     * @param expression a boolean expression
     * @param message exception message
     * @throws IllegalStateException if `expression` is false
     */
    @JvmOverloads
    fun checkState(expression: Boolean, message: String? = null) {
        if (!expression) {
            throw IllegalStateException(message)
        }
    }

    /**
     * Check the requested flags, throwing if any requested flags are outside
     * the allowed set.
     *
     * @return the validated requested flags.
     */
    fun checkFlagsArgument(requestedFlags: Int, allowedFlags: Int): Int {
        if (requestedFlags and allowedFlags != requestedFlags) {
            throw IllegalArgumentException(
                "Requested flags 0x"
                        + Integer.toHexString(requestedFlags) + ", but only 0x"
                        + Integer.toHexString(allowedFlags) + " are allowed"
            )
        }

        return requestedFlags
    }

    /**
     * Ensures that that the argument numeric value is non-negative.
     *
     * @param value a numeric int value
     * @param errorMessage the exception message to use if the check fails
     * @return the validated numeric value
     * @throws IllegalArgumentException if `value` was negative
     */
    @IntRange(from = 0)
    fun checkArgumentNonnegative(
        value: Int,
        errorMessage: String
    ): Int {
        if (value < 0) {
            throw IllegalArgumentException(errorMessage)
        }

        return value
    }

    /**
     * Ensures that that the argument numeric value is non-negative.
     *
     * @param value a numeric int value
     *
     * @return the validated numeric value
     * @throws IllegalArgumentException if `value` was negative
     */
    @IntRange(from = 0)
    fun checkArgumentNonnegative(value: Int): Int {
        if (value < 0) {
            throw IllegalArgumentException()
        }

        return value
    }

    /**
     * Ensures that that the argument numeric value is non-negative.
     *
     * @param value a numeric long value
     * @return the validated numeric value
     * @throws IllegalArgumentException if `value` was negative
     */
    fun checkArgumentNonnegative(value: Long): Long {
        if (value < 0) {
            throw IllegalArgumentException()
        }

        return value
    }

    /**
     * Ensures that that the argument numeric value is non-negative.
     *
     * @param value a numeric long value
     * @param errorMessage the exception message to use if the check fails
     * @return the validated numeric value
     * @throws IllegalArgumentException if `value` was negative
     */
    fun checkArgumentNonnegative(value: Long, errorMessage: String): Long {
        if (value < 0) {
            throw IllegalArgumentException(errorMessage)
        }

        return value
    }

    /**
     * Ensures that that the argument numeric value is positive.
     *
     * @param value a numeric int value
     * @param errorMessage the exception message to use if the check fails
     * @return the validated numeric value
     * @throws IllegalArgumentException if `value` was not positive
     */
    fun checkArgumentPositive(value: Int, errorMessage: String): Int {
        if (value <= 0) {
            throw IllegalArgumentException(errorMessage)
        }

        return value
    }

    /**
     * Ensures that the argument floating point value is a finite number.
     *
     *
     * A finite number is defined to be both representable (that is, not NaN) and
     * not infinite (that is neither positive or negative infinity).
     *
     * @param value a floating point value
     * @param valueName the name of the argument to use if the check fails
     *
     * @return the validated floating point value
     *
     * @throws IllegalArgumentException if `value` was not finite
     */
    fun checkArgumentFinite(value: Float, valueName: String): Float {
        if (java.lang.Float.isNaN(value)) {
            throw IllegalArgumentException("$valueName must not be NaN")
        } else if (java.lang.Float.isInfinite(value)) {
            throw IllegalArgumentException("$valueName must not be infinite")
        }

        return value
    }

    /**
     * Ensures that the argument floating point value is within the inclusive range.
     *
     *
     * While this can be used to range check against +/- infinity, note that all NaN numbers
     * will always be out of range.
     *
     * @param value a floating point value
     * @param lower the lower endpoint of the inclusive range
     * @param upper the upper endpoint of the inclusive range
     * @param valueName the name of the argument to use if the check fails
     *
     * @return the validated floating point value
     *
     * @throws IllegalArgumentException if `value` was not within the range
     */
    fun checkArgumentInRange(
        value: Float, lower: Float, upper: Float,
        valueName: String
    ): Float {
        if (java.lang.Float.isNaN(value)) {
            throw IllegalArgumentException("$valueName must not be NaN")
        } else if (value < lower) {
            throw IllegalArgumentException(
                String.format(
                    Locale.US,
                    "%s is out of range of [%f, %f] (too low)", valueName, lower, upper
                )
            )
        } else if (value > upper) {
            throw IllegalArgumentException(
                String.format(
                    Locale.US,
                    "%s is out of range of [%f, %f] (too high)", valueName, lower, upper
                )
            )
        }

        return value
    }

    /**
     * Ensures that the argument int value is within the inclusive range.
     *
     * @param value a int value
     * @param lower the lower endpoint of the inclusive range
     * @param upper the upper endpoint of the inclusive range
     * @param valueName the name of the argument to use if the check fails
     *
     * @return the validated int value
     *
     * @throws IllegalArgumentException if `value` was not within the range
     */
    fun checkArgumentInRange(
        value: Int, lower: Int, upper: Int,
        valueName: String
    ): Int {
        if (value < lower) {
            throw IllegalArgumentException(
                String.format(
                    Locale.US,
                    "%s is out of range of [%d, %d] (too low)", valueName, lower, upper
                )
            )
        } else if (value > upper) {
            throw IllegalArgumentException(
                String.format(
                    Locale.US,
                    "%s is out of range of [%d, %d] (too high)", valueName, lower, upper
                )
            )
        }

        return value
    }

    /**
     * Ensures that the argument long value is within the inclusive range.
     *
     * @param value a long value
     * @param lower the lower endpoint of the inclusive range
     * @param upper the upper endpoint of the inclusive range
     * @param valueName the name of the argument to use if the check fails
     *
     * @return the validated long value
     *
     * @throws IllegalArgumentException if `value` was not within the range
     */
    fun checkArgumentInRange(
        value: Long, lower: Long, upper: Long,
        valueName: String
    ): Long {
        if (value < lower) {
            throw IllegalArgumentException(
                String.format(
                    Locale.US,
                    "%s is out of range of [%d, %d] (too low)", valueName, lower, upper
                )
            )
        } else if (value > upper) {
            throw IllegalArgumentException(
                String.format(
                    Locale.US,
                    "%s is out of range of [%d, %d] (too high)", valueName, lower, upper
                )
            )
        }

        return value
    }

    /**
     * Ensures that the array is not `null`, and none of its elements are `null`.
     *
     * @param value an array of boxed objects
     * @param valueName the name of the argument to use if the check fails
     *
     * @return the validated array
     *
     * @throws NullPointerException if the `value` or any of its elements were `null`
     */
    fun <T> checkArrayElementsNotNull(value: Array<T>?, valueName: String): Array<T> {
        if (value == null) {
            throw NullPointerException("$valueName must not be null")
        }

        for (i in value!!.indices) {
            if (value!![i] == null) {
                throw NullPointerException(
                    String.format(Locale.US, "%s[%d] must not be null", valueName, i)
                )
            }
        }

        return value
    }

    /**
     * Ensures that the [Collection] is not `null`, and none of its elements are
     * `null`.
     *
     * @param value a [Collection] of boxed objects
     * @param valueName the name of the argument to use if the check fails
     *
     * @return the validated [Collection]
     *
     * @throws NullPointerException if the `value` or any of its elements were `null`
     */
    fun <C : Collection<T>, T> checkCollectionElementsNotNull(
        value: C?, valueName: String
    ): C {
        if (value == null) {
            throw NullPointerException("$valueName must not be null")
        }

        var ctr: Long = 0
        for (elem in value!!) {
            if (elem == null) {
                throw NullPointerException(
                    String.format(Locale.US, "%s[%d] must not be null", valueName, ctr)
                )
            }
            ++ctr
        }

        return value
    }

    /**
     * Ensures that the [Collection] is not `null`, and contains at least one element.
     *
     * @param value a [Collection] of boxed elements.
     * @param valueName the name of the argument to use if the check fails.
     *
     * @return the validated [Collection]
     *
     * @throws NullPointerException if the `value` was `null`
     * @throws IllegalArgumentException if the `value` was empty
     */
    fun <T> checkCollectionNotEmpty(
        value: Collection<T>?,
        valueName: String
    ): Collection<T> {
        if (value == null) {
            throw NullPointerException("$valueName must not be null")
        }
        if (value!!.isEmpty()) {
            throw IllegalArgumentException("$valueName is empty")
        }
        return value
    }

    /**
     * Ensures that all elements in the argument floating point array are within the inclusive range
     *
     *
     * While this can be used to range check against +/- infinity, note that all NaN numbers
     * will always be out of range.
     *
     * @param value a floating point array of values
     * @param lower the lower endpoint of the inclusive range
     * @param upper the upper endpoint of the inclusive range
     * @param valueName the name of the argument to use if the check fails
     *
     * @return the validated floating point value
     *
     * @throws IllegalArgumentException if any of the elements in `value` were out of range
     * @throws NullPointerException if the `value` was `null`
     */
    fun checkArrayElementsInRange(
        value: FloatArray, lower: Float, upper: Float,
        valueName: String
    ): FloatArray {
        checkNotNull(value, "$valueName must not be null")

        for (i in value.indices) {
            val v = value[i]

            if (java.lang.Float.isNaN(v)) {
                throw IllegalArgumentException("$valueName[$i] must not be NaN")
            } else if (v < lower) {
                throw IllegalArgumentException(
                    String.format(
                        Locale.US, "%s[%d] is out of range of [%f, %f] (too low)",
                        valueName, i, lower, upper
                    )
                )
            } else if (v > upper) {
                throw IllegalArgumentException(
                    String.format(
                        Locale.US, "%s[%d] is out of range of [%f, %f] (too high)",
                        valueName, i, lower, upper
                    )
                )
            }
        }

        return value
    }
}
/**
 * Ensures the truth of an expression involving the state of the calling
 * instance, but not involving any parameters to the calling method.
 *
 * @param expression a boolean expression
 * @throws IllegalStateException if `expression` is false
 */
