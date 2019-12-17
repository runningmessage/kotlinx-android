# kotlinx-android

## How to use

---  
maven
```maven
    <dependency>
        <groupId>com.runningmessage.kotlinx-android</groupId>
        <artifactId>kotlinx-common</artifactId>
        <version>0.0.3</version>
        <type>pom</type>
    </dependency>
    <dependency>
        <groupId>com.runningmessage.kotlinx-android</groupId>
        <artifactId>kotlinx-widget</artifactId>
        <version>0.0.3</version>
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
    compile 'com.runningmessage.kotlinx-android:kotlinx-common:0.0.3'
    compile 'com.runningmessage.kotlinx-android:kotlinx-widget:0.0.3'
```
---  
ivy
```ivy
    <dependency org="com.runningmessage.kotlinx-android" name="kotlinx-common" rev="0.0.3">
	    <artifact name="kotlinx-common" ext="pom"></artifact>
    </dependency>
    
    <dependency org="com.runningmessage.kotlinx-android" name="kotlinx-widget" rev="0.0.3">
	    <artifact name="kotlinx-widget" ext="pom"></artifact>
    </dependency>
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
AndroidExt:

```java
    
    /** Do something depending on android sdk, for <b>compatibility</b> */
    /** 判断安卓SDK版本后 执行代码,  <b>兼容性</b> */
    
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
HandlerExt:
```java
  
Handler.postDelayed(delay){
    // do something
}

```    
    
NoLeakListenerFunction1:

    wrap the Listener implementing interface , which only has one callback method, **Avoid Memory Leak !!!** 
    包装 只实现 有一个单独方法的接口 的监听器， 并返回代理对象， ***避免内存泄漏 !!!***
    
TextViewExt:

    TextView.addEllipsis:
    1. add "…" after the end of text: 在 TextView 显示的文本末尾添加省略号( … )
    
ThreadExt:

    object UiThreadHandler
    isMainThread()
    
```java
  
runOnNewThread { 
    // do something in a new thread
}

// same with below

Thread{
    // do something in a new thread
}.start()

``` 
```java
  
postOnNewThread(1000) { 
    // do something in a new thread after 1000 milliseconds
}

// same with below

val handler = Handler(HandlerThread("HandlerThread-${nextThreadNum()}").looper)
handler.postDelayed(1000) {
    // do something in a new thread after 1000 milliseconds
}

``` 
```java
  
postOnCurrentThread(1000) { 
    // do something in current thread after 1000 milliseconds
}

// same with below

Handler().postDelayed(1000) {
    // do something in current thread after 1000 milliseconds
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
