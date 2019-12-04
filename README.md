# kotlinx-android

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
    
TextViewExt:

    TextView.addEllipsis:
    1. add "…" after the end of text
    
ViewExt:

    View.layoutLeftMargin
    View.layoutRightMargin
    View.layoutTopMargin
    View.layoutBottomMargin
    View.layoutWidth
    View.layoutHeight
    
NoLeakListenerFunction1:

    wrap the Listener implementing interface , which only has one callback method, **Avoid Memory Leak!!!**   