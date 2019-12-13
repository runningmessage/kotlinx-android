package com.runningmessage.kotlinx.common

import android.app.Activity
import android.content.Intent
import android.os.Bundle

inline fun <reified A : Activity> A.startActivityForResult(requestCode: Int, options: Bundle? = null) {
    startActivityForResult(Intent(this, A::class.java), requestCode, options)
}

