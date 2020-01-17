# kotlinx-android

## How to use

---  
maven
```xml
    <dependency>
        <groupId>com.runningmessage.kotlinx-android</groupId>
        <artifactId>kotlinx-reflect</artifactId>
        <version>0.0.7</version>
        <type>pom</type>
    </dependency>

    <dependency>
        <groupId>com.runningmessage.kotlinx-android</groupId>
        <artifactId>kotlinx-common</artifactId>
        <version>0.0.7</version>
        <type>pom</type>
    </dependency>

    <dependency>
        <groupId>com.runningmessage.kotlinx-android</groupId>
        <artifactId>kotlinx-widget</artifactId>
        <version>0.0.7</version>
        <type>pom</type>
    </dependency>
```
---  
gradle
```gradle

    //project build.gradle
    buildscript {
        repositories {
            ...
            
            //add the maven url
            maven { url "https://dl.bintray.com/runningmessage/maven" }
        }
    }
    
    //app build.gradle
    compile 'com.runningmessage.kotlinx-android:kotlinx-reflect:0.0.7'
    compile 'com.runningmessage.kotlinx-android:kotlinx-common:0.0.7'
    compile 'com.runningmessage.kotlinx-android:kotlinx-widget:0.0.7'
```
---  
ivy
```xml
    <dependency org="com.runningmessage.kotlinx-android" name="kotlinx-reflect" rev="0.0.7">
	    <artifact name="kotlinx-common" ext="pom"></artifact>
    </dependency>

    <dependency org="com.runningmessage.kotlinx-android" name="kotlinx-common" rev="0.0.7">
	    <artifact name="kotlinx-common" ext="pom"></artifact>
    </dependency>
    
    <dependency org="com.runningmessage.kotlinx-android" name="kotlinx-widget" rev="0.0.7">
	    <artifact name="kotlinx-widget" ext="pom"></artifact>
    </dependency>
```
---
## kotlinx-reflect
Some functions to make reflective calls.


### simple example

Demo.01 simple example

````java

        import com.runningmessage.kotlinx.reflect.*

        lateinit var context: Context

        val Builder = "android.support.v7.app.AlertDialog${'$'}Builder"
        val OnClickListener = "android.content.DialogInterface${'$'}OnClickListener"

        Builder(context)                            // Like AlertDialog.Builder(context)
               .calls("setTitle")("Hello World")    // Call matched specific named function
               .calls("setPositiveButton")("OK",
                OnClickListener.createInners{       // Like object: DialogInterface.OnClickListener
                    /***
                     *  Like:
                     *  override fun onClick(dialog: Any, which: Int){
                     *
                     *  }
                     */
                    override<Any, Int>("onClick"){ dialog, which ->

                    }
                })
               .calls("create").calls("show")()     // Like builder.create().show()

````

Demo.02 Call function by reflect

```java

        private lateinit var activity: Any

        activity.runInActivity()

        private fun Any.runInActivity() {// In the code below , [this] is Activity Object

            /***
             * setContentView(R.layout.activity_main)
             * ***/
            calls("setContentView")(R.layout.activity_main)

            /***
             * val npRecyclerView = findViewById<ViewGroup>(R.id.npRecyclerView)
             * ***/
            val npRecyclerView = call<ViewGroup>("findViewById")(R.id.npRecyclerView)

            val makeText = "android.widget.Toast.makeText"

            callsStatic(makeText)(applicationContext, "Hello World", 0)
            .calls("show")()

            ... ...

         }

```
Demo.03 Create instance by reflect

```java

        /***************************** Import to reflect *****************************/
        /**  The string below can be used just same as declared class in the code   **/

        private const val MyLoadMoreRecyclerAdapter = "com.runningmessage.kotlinx.demo.MyLoadMoreRecyclerAdapter"
        private const val StaggeredGridLayoutManager = "android.support.v7.widget.StaggeredGridLayoutManager"

        private fun Any.runInActivity() {// In the code below , [this] is Activity Object

            ... ..

            // set the load more recycler adapter
            val applicationContext = call<Context>("getApplicationContext")()

            /***
             * the declared string [MyLoadMoreRecyclerAdapter] can be used same as class which is imported
             *
             * val adapter = com.runningmessage.kotlinx.demo.MyLoadMoreRecyclerAdapter(applicationContext)
             * ***/
            val adapter: Any? = MyLoadMoreRecyclerAdapter(applicationContext)

            /***
             * the declared string [StaggeredGridLayoutManager] can be used same as class which is imported
             *
             * npRecyclerView.setLayoutManager(StaggeredGridLayoutManager(2, 1))
             * ***/
            npRecyclerView.calls("setLayoutManager")(StaggeredGridLayoutManager(2, 1))

            ... ...
        }

```

Demo.04 Call property by reflect

```java

        private fun Any.runInActivity() {// In the code below , [this] is Activity Object

            ... ..

            /***
             * adapter.isAutoLoadMore = false
             ***/
            adapter.propertys("isAutoLoadMore").value = false

            /***
             * val count = adapter.dataCount
             ***/
            val count = adapter.property<Int>("dataCount").value ?: 0

            val makeText = "android.widget.Toast.makeText"
            val LENGTH_LONG = "android.widget.Toast.LENGTH_LONG"

            callsStatic(makeText)(applicationContext, "Hello World", property<Int>(LENGTH_LONG)?: 0)
            .calls("show")()

            ... ...
        }

```
Demo.05 Create anonymous inner class instance by reflect

````java

        val TextWatcher = "android.text.TextWatcher"

        val watcher = TextWatcher.createInners{ // Like val watcher = object: TextWatcher{ ... ... }

            /**
             * override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
             *
             * }
             **/
            override<CharSequence, Int, Int, Int>("beforeTextChanged"){
                s, start, before, count ->

            }

            /**
             * override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
             *
             * }
             **/
            override("onTextChanged"){
                s: CharSequence?, start: Int?, before: Int?, count: Int? ->

            }

            /**
             * override fun afterTextChanged(s: Editable?) {
             *
             * }
             **/
            override("onTextChanged", CallHandlerFunction { args: Array<out Any?>? ->
                val s = args?.get(0) as? Editable

            })
        }
````

Demo.06 Call instanceOf/is by reflect

```java

        val AppCompatActivity = "android.support.v7.app.AppCompatActivity"

        lateinit var activity: Any

        if(activity iss AppCompatActivity){
            // do something
        }

        activity.ifIs(AppCompatActivity) { appCompatActivity: Any ->
            appCompatActivity.runInActivity()
        }

        // another sample
        val context: Context? = activity.ifIs(AppCompatActivity) { appCompatActivity: Any ->
            return@ifIs appCompatActivity.call<Context>("getApplicationContext")()
        }

```
---
## kotlinx-widget
AbsSwipeRefreshLayout<ProgressView, RemindView>:

    rewite SwipeRefreshLayout :
    1. support pull down to refresh and enable to custom pull refresh anim: 支持下拉刷新自定义刷新动画
    2. support show remind pop view after pull refresh: 支持刷新完成后弹窗提醒

LoadMoreRecyclerAdapter<VHData : RecyclerView.ViewHolder, Data, VHFooter>:

    custom RecyclerView.Adapter :
    1. support pull up load more: 支持上拉加载更多
    
## kotlinx-common

ActivityExt:

    Activity.contentView
    1. return the content view set by [Activity.setContentView]

```java
  
Activity.startActivityForResult<MyActivity>(requestCode, bundle)

// same with below

Activity.startActivityForResult(Intent(context, MyActivity::class.java), requestCode, bundle)

``` 
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
AndroidExt:

```java
    
    /** Do something depending on android sdk, for <b>compatibility</b> */
    /** 判断安卓SDK版本后 执行代码,  <b>兼容性</b> */
    
    ifSdk(16, 19, 29) {
        //do something if Build.VERSION.SDK_INT is one of 16, 19, 29
    } other {
        //otherwise, do other thing
    }
        
    ifNotSdk(16, 19, 29) {
        //do something if Build.VERSION.SDK_INT is not 16, 19 or 29
    } other {
        //otherwise, do other thing
    }
    
    fromSdk(16) {
        //do something if Build.VERSION.SDK_INT >= 16
    } other {
        //otherwise, Build.VERSION.SDK_INT < 16, do other thing
    }
    
    toSdk(16) {
        //do something if Build.VERSION.SDK_INT <= 16
    } other {
        //otherwise, Build.VERSION.SDK_INT > 16, do other thing
    }

    afterSdk(16) {
        //do something if Build.VERSION.SDK_INT > 16
    } other {
        //otherwise, Build.VERSION.SDK_INT <= 16, do other thing
    }

    untilSdk(16) {
        //do something if Build.VERSION.SDK_INT < 16
    } other {
        //otherwise, Build.VERSION.SDK_INT >= 16, do other thing
    }

```
AnyExt.kt

```java
    
    Any.ifNull{
        // do something and return the default vale when null
    }
    
```    
BitmapExt:

    Bitmap?.createRoundedCornerBitmap
    1. create a new rounded corner bitmap, basing on the current bitmap: 基于当前 Bitmap 创建一个带圆角的 Bitmap 
    
ContextExt:

    Context?.toastShort(msg)    
    Context?.toastShort(resId)   
    Context?.toastLong(msg)    
    Context?.toastLong(resId) 
    
    Context.dpToPx(dp)
    1. cast the [dp] dip into px unit
    
    Context.pxToDp(px)
    1. cast the [px] px into dip unit
    
    Context.screenWidth()
    1. get the screen width pixels ,
    same as [Context.getResources].[getDisplayMetrics].[widthPixels]
 
    Context.screenWidthShort()
    1. return the smaller between [screenWidth] and [screenHeight]
    
    Context.screenWidthX()
    1. return the [screenWidthShort] if current is orientation portrait, otherwise the [screenHeightLong]
    
    Context.screenHeight()
    1. get the screen height pixels ,
    same as [Context.getResources].[getDisplayMetrics].[heightPixels]
    
    Context.screenHeightLong()
    1. return the larger between [screenWidth] and [screenHeight]
    
    Context.screenHeightY()
    1. return the [screenHeightLong] if current is orientation portrait, otherwise the [screenWidthShort]
    
```java
  
Context.startActivity<MyActivity>()

// same with below

Context.startActivity(Intent(context, MyActivity::class.java))

``` 
```java
  
Context.startActivity<MyActivity>{
    action = "com.action.xxx"
    putExtra("key", "value")
}

// same with below

val intent = Intent(context, MyActivity::class.java).apply{
    action = "com.action.xxx"
    putExtra("key", "value")
}
Context.startActivity(intent)

```
```java
context.startActivity("com.action.xxx"){
    putExtra("key", "value")
}

// same with below

val intent = Intent("com.action.xxx").apply{
    putExtra("key", "value")
}
context.startActivity(intent)

```

HandlerExt:
```java
  
Handler.postDelayed(delay){
    // do something
}

```    
    
NoLeakListenerFunction1:

    wrap the Listener implementing interface , which only has one callback method, **Avoid Memory Leak !!!** 
    包装 只实现 有一个单独方法的接口 的监听器， 并返回代理对象， ***避免内存泄漏 !!!***
    
StringExt.kt

    CharSequence.ifNullOrEmpty{}    
    CharSequence.ifNullOrBlank{}    
    CharSequence.ifNotNullOrEmpty{}    
    CharSequence.ifNotNullOrBlank{}
        
SupportExt.kt
      
    FragmentClassSupport
    FragmentGetActivitySupport
    FragmentIsRemovingSupport
          
TextViewExt:

    TextView.addEllipsis:
    1. add "…" after the end of text: 在 TextView 显示的文本末尾添加省略号( … )
    
ThreadExt:

    object UiThreadHandler
    1. The UI thread handler
    
    isMainThread()
    1. return whether the current thread is main thread
    

```java
/***
 *  create a new [Thread] and start to run the [block]
 */  
runOnNewThread { 
    // do something in a new thread
}

// same with below

Thread{
    // do something in a new thread
}.start()

``` 

```java
/***
 *  create a new [HandlerThread] named "HandlerThread-[nextThreadNum]",
 *  and send message to the [Handler] associated with this [HandlerThread].[getLooper()][HandlerThread.getLooper] to run the [block] after [delay] milliseconds,
 *  and then to call [HandlerThread.quitSafely] or [HandlerThread.quit] after [block] invoke
 */  
postOnNewThread(1000) { 
    // do something in a new thread after 1000 milliseconds
}

// same with below

val handler = Handler(HandlerThread("HandlerThread-${nextThreadNum()}").apply { start() }.looper)
handler.postDelayed(1000) {
    try {
        // do something in a new thread after 1000 milliseconds
    } finally {
        (Thread.currentThread() as? HandlerThread)?.let { thread ->

            fromSdk(18) {
                thread.quitSafely()
            } other {
                thread.quit()
            }

        }
    }
}

``` 
```java
/***
 *  if no [Looper] has associated with [currentThread][Thread.currentThread], just to [prepare][Looper.prepare] and call [Looper.loop],
 *  in this case, it will call [HandlerThread.quitSafely] or [HandlerThread.quit] after [block] invoke;
 *  and then send message to the [Handler] associated with [currentThread][Thread.currentThread].[looper][Looper.myLooper] to run the [block] after [delay] milliseconds
 */ 
postOnCurrentThread(1000) { 
    // do something in current thread after 1000 milliseconds
}

// same with below

if (isMainThread()) {
    postOnUiThread(1000){
        // do something in current thread after 1000 milliseconds
    }
} else {
    var needQuitLooper = false
    val looper: Looper? = Looper.myLooper().ifNull {
        needQuitLooper = true
        Looper.prepare()
        Looper.myLooper()
    }

    postOnUiThread {
        Handler(looper).postDelayed(1000) {
            try {
                // do something in current thread after 1000 milliseconds
            } finally {
                if (needQuitLooper) {
                    fromSdk(18) {
                        looper?.quitSafely()
                    } other {
                        looper?.quit()
                    }
                }
            }
        }
    }

    Looper.loop()

}

``` 
```java
  
runOnUiThread(target) { 
    // do something in ui thread, 
    // and try to associate with the lifecycle for target , when target is not null
}

postOnUiThread(target, 1000){
    // do something in ui thread after 1000 milliseconds, 
    // and try to associate with the lifecycle for target , when target is not null
}

``` 
    
    Runnable.runOnUiThread(target)
    1. immediately run the task, if current is on UI thread
    2. otherwise, to post the task into run queue, or maybe a ui thread handler
    
    Runnable.postOnUiThread(target, delay)
    1. post the task into run queue, or maybe a ui thread handler;
    and the task will be to run after [delay] milliseconds ,
    even if the [delay] is less than 0, it is still waiting the run queue to call running
    
ViewExt:

    
```java
  
View.setVisible() // visibility = View.VISIBLE
View.isVisible() // return visibility == View.VISIBLE
View.setInvisible() // visibility = View.INVISIBLE
View.isInvisible() // return visibility == View.INVISIBLE
View.setGone() // visibility = View.GONE
View.isGone() // return visibility = View.GONE

```   

    View.layoutLeftMargin: 
    1. return the leftMargin of the layoutParams for the current View, if exists , otherwise return null
    2. set the leftMargin of the layoutParams for the current View, and reset the layoutParams
    
    View.layoutRightMargin: 
    1. return the rightMargin of the layoutParams for the current View, if exists , otherwise return null
    2. set the rightMargin of the layoutParams for the current View, and reset the layoutParams
    
    View.layoutTopMargin: 
    1. return the topMargin of the layoutParams for the current View, if exists , otherwise return null
    2. set the topMargin of the layoutParams for the current View, and reset the layoutParams
    
    View.layoutBottomMargin: 
    1. return the bottomMargin of the layoutParams for the current View, if exists , otherwise return null
    2. set the bottomMargin of the layoutParams for the current View, and reset the layoutParams
    
    View.layoutWidth: 
    1. return the width of the layoutParams for the current View, if exists , otherwise return null
    2. set the width of the layoutParams for the current View, and reset the layoutParams
    
    View.layoutHeight: 
    1. return the height of the layoutParams for the current View, if exists , otherwise return null
    2. set the height of the layoutParams for the current View, and reset the layoutParams
    
```java
  
View.postDelayed(delay){
    // do something
}

```    

ViewGroupExt:

    ViewGroup.views
    1. the list of child view
    
    ViewGroup.firstView
    1. the first child view
    
    ViewGroup.lastView
    1. the last child view
