package com.runningmessage.kotlin.ext.widget

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.ViewGroup
import java.lang.ref.WeakReference

/**
 * Created by Lorss on 19-2-26.
 */
abstract class LoadMoreRecyclerAdapter<VHData : RecyclerView.ViewHolder, Data, VHFooter> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>()
        where VHFooter : RecyclerView.ViewHolder,
              VHFooter : LoadMoreFooter {

    companion object {
        private const val TYPE_FOOTER = Integer.MAX_VALUE
        private const val FINISH_SHOW_DURATION = 1500L
    }

    private val mDataList: ArrayList<Data> = ArrayList()

    val dataCount
        get() = mDataList.size

    val dataList
        get() = mDataList

    private var mKeepRefRecyclerView: RecyclerView? = null
    private var mWeakRefRecyclerView: WeakReference<RecyclerView>? = null
    private var hasStartMonitor = false

    var isLoadMoreEnable: Boolean = true
        set(enable) {
            field = enable
            if (enable) mKeepRefRecyclerView?.let { startMonitor(it) }
            notifyDataSetChanged()
        }
    var isAutoLoadMore: Boolean = true

    var onLoadMoreListener: OnLoadMoreListener? = null

    private var mFooter: VHFooter? = null

    var footerMessage: String
        set(value) {
            mFooter?.message = value
        }
        get() = mFooter?.message ?: ""


    abstract fun onCreateDataViewHolder(parent: ViewGroup?, viewType: Int): VHData

    final override fun onCreateViewHolder(
        parent: ViewGroup?,
        viewType: Int
    ): RecyclerView.ViewHolder =
        when (viewType) {
            TYPE_FOOTER -> (mFooter ?: onCreateFooter()).apply {
                showIdle()
                itemView?.setOnClickListener {
                    callLoadMore()
                }
                mFooter = this
            }
            else -> onCreateDataViewHolder(parent, viewType)
        }

    final override fun getItemCount(): Int =
        if (isLoadMoreEnable && (dataCount > 0)) dataCount + 1 else dataCount

    fun getItem(position: Int): Data? = mDataList[position]

    fun addData(list: Collection<Data>, index: Int? = null) =
        when {
            index == null -> mDataList.addAll(list)
            (index >= 0) && (index <= mDataList.size) -> mDataList.addAll(index, list)
            else -> false
        }

    fun addData(data: Data, index: Int? = null) =
        when {
            index == null -> mDataList.add(data)
            (index >= 0) && (index <= mDataList.size) -> {
                mDataList.add(index, data)
                true
            }
            else -> false
        }


    fun setData(list: Collection<Data>) {
        mDataList.clear()
        mDataList.addAll(list)
    }

    final override fun getItemViewType(position: Int): Int {
        return if (isFooter(position)) TYPE_FOOTER else
            getItemDataViewType(position)
    }

    abstract fun getItemDataViewType(position: Int): Int

    protected fun isFooter(position: Int) = isLoadMoreEnable && position >= itemCount - 1

    abstract fun onCreateFooter(): VHFooter

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (isFooter(holder.layoutPosition)) {
            (holder.itemView.layoutParams as? StaggeredGridLayoutManager.LayoutParams)?.isFullSpan =
                    true
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        (recyclerView?.layoutManager as? GridLayoutManager)?.let {

            it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int =
                    if (isFooter(position)) it.spanCount else 1

            }

        }

        recyclerView?.let {
            mWeakRefRecyclerView = WeakReference(recyclerView)
            startMonitor(recyclerView)
        }
    }


    private fun startMonitor(
        recyclerView: RecyclerView
    ) {

        if (!isLoadMoreEnable) {
            mKeepRefRecyclerView = recyclerView
            return
        }

        mKeepRefRecyclerView = null

        if (hasStartMonitor) return

        hasStartMonitor = true

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    val (lastVisible, lastCompletelyVisible) = findVisiblePositions(recyclerView)

                    if (!isAutoLoadMore) {

                        if (lastVisible == itemCount - 1) {
                            if (lastCompletelyVisible == itemCount - 1) {
                                callLoadMore()
                            } else {
                                mFooter?.showIdle()
                            }
                        }
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val (lastVisible, lastCompletelyVisible) = findVisiblePositions(recyclerView)

                if (isAutoLoadMore) {
                    if (lastVisible == itemCount - 1) {
                        callLoadMore()
                    } else {
                        isAutoLoadMore = false
                    }
                } else {
                    if (lastVisible == itemCount - 1) {
                        if (recyclerView?.scrollState == RecyclerView.SCROLL_STATE_IDLE) {// called by init scroll
                            if (lastCompletelyVisible == itemCount - 1) {
                                if (recyclerView.canScrollVertically(-1)) {
                                    recyclerView.smoothScrollBy(0, -(mFooter?.viewHeight ?: 0))
                                } else {
                                    mFooter?.showFullToLoad()
                                }
                            } else {
                                mFooter?.showPullToLoad()
                            }
                        } else {// called by move scroll event
                            if (lastCompletelyVisible == itemCount - 1) {
                                mFooter?.showReleaseToLoad()
                            } else {
                                mFooter?.showPullToLoad()
                            }
                        }


                    }
                }
            }
        })
    }

    private fun callLoadMore() {
        mLoading = true
        mFooter?.showLoading()
        onLoadMoreListener?.onLoadMore()
    }

    private var mLoading = false
    var isLoading: Boolean
        set(loading) {
            if (loading && loading != mLoading) {
                setLoading(true, true)
            } else {
                setLoading(false, false)
            }
            mLoading = loading
        }
        get() = mLoading

    private fun setLoading(loading: Boolean, notify: Boolean) {

        if (loading) {
            if (notify) callLoadMore()
        } else {
            mFooter?.let { footer ->

                footer.finishLoading()
                mWeakRefRecyclerView?.get()?.let { recyclerView ->

                    val (lastVisible, lastCompletelyVisible) = findVisiblePositions(recyclerView)

                    if (lastCompletelyVisible == itemCount - 1) {
                        recyclerView.postDelayed({
                            if (recyclerView.canScrollVertically(-1)) {
                                recyclerView.smoothScrollBy(0, -footer.viewHeight)
                            } else {
                                footer.showFullToLoad()
                            }
                        }, FINISH_SHOW_DURATION)
                    }
                }
            }

        }
    }

    private fun findVisiblePositions(recyclerView: RecyclerView?): Pair<Int, Int> {
        val layoutManager = recyclerView?.layoutManager
        val lastVisible = findLastVisibleItemPosition(layoutManager)
        val lastCompletelyVisible =
            findLastCompletelyVisibleItemPosition(layoutManager)
        return Pair(lastVisible, lastCompletelyVisible)
    }

    private fun findLastVisibleItemPosition(layoutManager: RecyclerView.LayoutManager?): Int =
        when (layoutManager) {
            is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
            is StaggeredGridLayoutManager -> layoutManager.findLastVisibleItemPositions(
                null
            ).max() ?: -2
            else -> -2
        }

    private fun findLastCompletelyVisibleItemPosition(layoutManager: RecyclerView.LayoutManager?): Int =
        when (layoutManager) {
            is LinearLayoutManager -> layoutManager.findLastCompletelyVisibleItemPosition()
            is StaggeredGridLayoutManager -> layoutManager.findLastCompletelyVisibleItemPositions(
                null
            ).max() ?: -2
            else -> -2
        }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

}