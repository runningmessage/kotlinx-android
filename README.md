# kotlinx-android

## How to use

---  
maven
```maven
    <dependency>
        <groupId>com.runningmessage.kotlinx</groupId>
        <artifactId>kotlinx-android</artifactId>
        <version>0.0.2</version>
        <type>pom</type>
    </dependency>
```
---  
gradle
```gradle
    compile 'com.runningmessage.kotlinx:kotlinx-android:0.0.2'
```
---  
ivy
```ivy
    <dependency org="com.runningmessage.kotlinx" name="kotlinx-android" rev="0.0.2">
	    <artifact name="kotlinx-android" ext="pom"></artifact>
    </dependency>
```
## widget
AbsSwipeRefreshLayout<ProgressView, RemindView>:

    rewite SwipeRefreshLayout :
    1. support pull down to refresh and enable to custom pull refresh anim: 支持下拉刷新自定义刷新动画
    2. support show remind pop view after pull refresh: 支持刷新完成后弹窗提醒

LoadMoreRecyclerAdapter<VHData : RecyclerView.ViewHolder, Data, VHFooter>:

    custom RecyclerView.Adapter :
    1. support pull up load more: 支持上拉加载更多
    
## common
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
    
TextViewExt:

    TextView.addEllipsis:
    1. add "…" after the end of text: 在 TextView 显示的文本末尾添加省略号( … )
    
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
    
    
NoLeakListenerFunction1:

    wrap the Listener implementing interface , which only has one callback method, **Avoid Memory Leak !!!** 
    包装 只实现 有一个单独方法的接口 的监听器， 并返回代理对象， ***避免内存泄漏 !!!***