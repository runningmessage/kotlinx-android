package com.runningmessage.kotlin.ext.demo

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.runningmessage.kotlin.ext.widget.LoadMoreFooter
import com.runningmessage.kotlin.ext.widget.LoadMoreRecyclerAdapter
import com.runningmessage.kotlin.ext.widget.dip2px

/**
 * Created by Lorss on 19-2-27.
 */
class MyLoadMoreRecyclerAdapter(val context: Context) :
    LoadMoreRecyclerAdapter<RecyclerView.ViewHolder, String, MyLoadMoreFooter>() {

    override fun onCreateDataViewHolder(
        parent: ViewGroup?,
        viewType: Int
    ): RecyclerView.ViewHolder =
        object : RecyclerView.ViewHolder(TextView(context)) {}.apply {

            val textView = this.itemView as? TextView
            if (textView != null) {
                textView.setBackgroundColor(Color.parseColor("#FFE2E2E2"))
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                textView.setTextColor(Color.parseColor("#FF333333"))
                textView.height = 200
                textView.width = ViewGroup.LayoutParams.MATCH_PARENT
                textView.gravity = Gravity.CENTER
            }
        }

    override fun getItemDataViewType(): Int {
        return 0
    }

    override fun onCreateFooter(): MyLoadMoreFooter =
        MyLoadMoreFooter(TextView(context)).apply {

            val textView = this.itemView as? TextView
            if (textView != null) {
                textView.setBackgroundColor(Color.parseColor("#FFE2E2E2"))
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                textView.setTextColor(Color.parseColor("#FF000000"))
                textView.height = 200
                textView.width = ViewGroup.LayoutParams.MATCH_PARENT
                textView.gravity = Gravity.CENTER
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val textView = (holder?.itemView as? TextView)
        val isFooter = isFooter(position)
        if (!isFooter) {
            textView?.text = getItem(position)
            textView?.height = dip2px(context, 100f + (position * 5 % 30))
            textView?.width = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

}

class MyLoadMoreFooter(itemView: View) : RecyclerView.ViewHolder(itemView), LoadMoreFooter {

    override val viewWidth: Int
        get() = itemView?.width ?: 0
    override val viewHeight: Int
        get() = itemView?.height ?: 0


    override var message: String = ""
        set(text) {
            field = text
            (itemView as? TextView)?.text = text
        }

    override fun showIdle() {
        (itemView as? TextView)?.text = "上拉加载更多"
    }

    override fun showFullToLoad() {
        (itemView as? TextView)?.text = "点击加载更多"
    }

    override fun showPullToLoad() {
        (itemView as? TextView)?.text = "上拉加载更多"
    }

    override fun showReleaseToLoad() {
        (itemView as? TextView)?.text = "松手加载更多"
    }

    override fun showLoading() {
        (itemView as? TextView)?.text = "加载中 ..."
    }

    override fun finishLoading() {
        (itemView as? TextView)?.text = message
    }

}