package com.runningmessage.kotlin.ext.demo

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.runningmessage.kotlin.ext.widget.AbsSwipeRefreshLayout
import com.runningmessage.kotlin.ext.widget.LoadMoreRecyclerAdapter
import com.runningmessage.kotlin.ext.widget.dip2px
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random.Default.nextInt

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        npRecyclerView.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        npRecyclerView.itemAnimator = null

        val adapter = object : LoadMoreRecyclerAdapter<RecyclerView.ViewHolder, String>() {
            override fun getItemDataViewType(): Int {
                return 0
            }

            override fun createFooter(): RecyclerView.ViewHolder =
                object : RecyclerView.ViewHolder(TextView(applicationContext)) {}.apply {

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

            override fun onCreateDataViewHolder(
                parent: ViewGroup?,
                viewType: Int
            ): RecyclerView.ViewHolder =
                object : RecyclerView.ViewHolder(TextView(applicationContext)) {}.apply {

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

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
                val textView = (holder?.itemView as? TextView)
                val isFooter = isFooter(position)
                textView?.text = if (isFooter) "加载更多..." else mDataList[position]
                textView?.height =
                        if (isFooter) dip2px(applicationContext, 48f) else 200 + nextInt(100)
                textView?.width = ViewGroup.LayoutParams.MATCH_PARENT
            }

        }
        adapter.loadMoreListener = object : LoadMoreRecyclerAdapter.OnLoadMoreListener {
            override fun onLoadMore() {
                npRecyclerView.postDelayed({
                    val list = ArrayList<String>()
                    val count = adapter.dataCount
                    for (i in count..count + 10) {
                        list.add(i.toString())
                    }
                    adapter.add(list)
                    adapter.notifyDataSetChanged()
                }, 3000)
            }

        }
        npRecyclerView.adapter = adapter
        val list = ArrayList<String>()
        for (i in 1..10) {
            list.add(i.toString())
        }
        adapter.add(list)
        npRefreshLayout.setProgressViewEndTarget(true, dip2px(applicationContext, 32f))
        npRefreshLayout.setDistanceToTriggerSync(dip2px(applicationContext, 80f))
        npRefreshLayout.setTargetPull(true, dip2px(applicationContext, 4f))

        npRefreshLayout.postDelayed({ npRefreshLayout.isRefreshing = true }, 1000)
        npRefreshLayout.postDelayed({
            npRefreshLayout.setShowRemind(message = "Auto Refresh Message")
            npRefreshLayout.isRefreshing = false
        }, 5000)

        npRefreshLayout.setOnRefreshListener(object : AbsSwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                Toast.makeText(applicationContext, "pull refresh", Toast.LENGTH_SHORT).show()

                npRefreshLayout.postDelayed({
                    npRefreshLayout.setShowRemind(message = "Manual Refresh Message")
                    npRefreshLayout.isRefreshing = false
                }, 3000)

            }

        })

    }


}
