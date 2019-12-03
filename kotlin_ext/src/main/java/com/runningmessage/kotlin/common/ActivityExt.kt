package com.runningmessage.kotlin.common

import android.app.Activity
import android.widget.Toast

fun Activity.toastShort(msg: CharSequence) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun Activity.toastLong(msg: CharSequence) = Toast.makeText(this, msg, Toast.LENGTH_LONG).show()