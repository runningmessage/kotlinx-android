package com.runningmessage.kotlin.ext.widget

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.ViewGroup

/**
 * Created by Lorss on 19-2-26.
 */
abstract class LoadMoreRecyclerAdapter<VH : RecyclerView.ViewHolder, Data> :
    RecyclerView.Adapter<VH>() {

    protected val mDataList: ArrayList<Data> = ArrayList()

    val dataCount
        get() = mDataList.size

    var loadMoreEnable: Boolean = true
    var autoLoadMore: Boolean = true

    var loadMoreListener: OnLoadMoreListener? = null

    private var mFooter: VH? = null

    companion object {
        private const val TYPE_FOOTER = Integer.MAX_VALUE;
    }


    abstract fun onCreateDataViewHolder(parent: ViewGroup?, viewType: Int): VH

    final override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH =
        when (viewType) {
            TYPE_FOOTER -> (mFooter ?: createFooter()).apply {
                this.itemView?.setOnClickListener {
                    loadMoreListener?.onLoadMore()
                }
            }
            else -> onCreateDataViewHolder(parent, viewType)
        }

    final override fun getItemCount(): Int =
        if (mDataList.size > 0) mDataList.size + 1 else 0

    fun getItem(position: Int): Data? = mDataList?.get(position)

    fun add(list: List<Data>) {
        mDataList.addAll(list)
    }

    final override fun getItemViewType(position: Int): Int {
        return if (isFooter(position)) TYPE_FOOTER else
            super.getItemViewType(position)
    }

    abstract fun getItemDataViewType(): Int

    protected fun isFooter(position: Int) = loadMoreEnable && position >= itemCount - 1

    abstract fun createFooter(): VH

    override fun onViewAttachedToWindow(holder: VH) {
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

            startMonitor(recyclerView, it)
        }

    }


    private fun startMonitor(
        recyclerView: RecyclerView?,
        layoutManager: RecyclerView.LayoutManager?
    ) {

        if (!loadMoreEnable || loadMoreListener == null) return

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!autoLoadMore && findLastVisibleItemPosition(layoutManager) == itemCount - 1) {
                        loadMoreListener?.onLoadMore()
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (autoLoadMore && findLastVisibleItemPosition(layoutManager) == itemCount - 1) {
                    loadMoreListener?.onLoadMore()
                } else if (autoLoadMore) {
                    autoLoadMore = false
                }
            }
        })
    }

    private fun findLastVisibleItemPosition(layoutManager: RecyclerView.LayoutManager?): Int =
        when (layoutManager) {
            is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
            is StaggeredGridLayoutManager -> layoutManager.findLastVisibleItemPositions(
                null
            ).max() ?: -2
            else -> -2
        }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

}