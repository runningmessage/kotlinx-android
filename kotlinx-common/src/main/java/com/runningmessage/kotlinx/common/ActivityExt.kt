package com.runningmessage.kotlinx.common

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window.ID_ANDROID_CONTENT

//@formatter:off
/***
 * e.g.

```java

Activity.startActivityForResult<MyActivity>(requestCode, bundle)

// same with below

Activity.startActivityForResult(Intent(context, MyActivity::class.java), requestCode, bundle)

```
 */
//@formatter:on
inline fun <reified A : Activity> Activity.startActivityForResult(requestCode: Int, options: Bundle? = null) {
    startActivityForResult(Intent(this, A::class.java), requestCode, options)
}

//@formatter:off
/***
 * e.g.
```java

Activity.startActivityForResult<MyActivity>(requestCode, bundle){
    action = "com.action.xxx"
    putExtra("key", "value")
}

// same with below

val intent = Intent(context, MyActivity::class.java).apply{
    action = "com.action.xxx"
    putExtra("key", "value")
}
Activity.startActivityForResult(intent, requestCode, bundle)

```
 */
//@formatter:on
inline fun <reified A : Activity> Activity.startActivityForResult(requestCode: Int, options: Bundle? = null, block: Intent.() -> Unit) {
    startActivityForResult(Intent(this, A::class.java).also { it.block() }, requestCode, options)
}


//@formatter:off
/***
 * e.g.
```java

Activity.startActivityForResult("com.action.xxx", requestCode, bundle){
    putExtra("key", "value")
}

// same with below

val intent = Intent("com.action.xxx").apply{
    putExtra("key", "value")
}
Activity.startActivityForResult(intent, requestCode, bundle)

```
 */
//@formatter:on
fun Activity.startActivityForResult(action: String = "", requestCode: Int, options: Bundle? = null, block: (Intent.() -> Unit)? = null) {
    startActivityForResult(Intent().also { intent ->
        action.ifNotNullOrBlank { action ->
            intent.action = action
        }
        block?.let { intent.it() }
    }, requestCode, options)
}

/***
 *  return the content view set by [Activity.setContentView]
 */
val <A : Activity> A.contentView: View?
    get() = findViewById<ViewGroup>(ID_ANDROID_CONTENT)?.firstView


