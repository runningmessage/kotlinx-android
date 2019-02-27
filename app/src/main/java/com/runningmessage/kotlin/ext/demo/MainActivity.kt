package com.runningmessage.kotlin.ext.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
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


        // set the load more recycler adapter
        val adapter = MyLoadMoreRecyclerAdapter(applicationContext)
        adapter.isLoadMoreEnable = false
        adapter.isAutoLoadMore = false

        adapter.onLoadMoreListener = object : LoadMoreRecyclerAdapter.OnLoadMoreListener {
            override fun onLoadMore() {
                npRecyclerView.postDelayed({
                    val random = nextInt(10)
                    if (random < 5) {
                        val list = ArrayList<String>()
                        val count = adapter.dataCount
                        for (i in count + 1..count + 10) {
                            list.add(i.toString())
                        }
                        adapter.addData(list)
                        adapter.footerMessage = "加载成功"
                        adapter.notifyDataSetChanged()
                    } else {
                        adapter.footerMessage = "没有更多数据"
                    }
                    adapter.isLoading = false
                }, 3000)
            }

        }
        npRecyclerView.adapter = adapter

        // set the init data for adapter
        val list = ArrayList<String>()
        for (i in 1..9) {
            list.add(i.toString())
        }
        adapter.setData(list)

        // set the recyclerView
        npRecyclerView.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        npRecyclerView.itemAnimator = null

        // set the refresh layout
        npRefreshLayout.setProgressViewEndTarget(true, dip2px(applicationContext, 32f))
        npRefreshLayout.setDistanceToTriggerSync(dip2px(applicationContext, 80f))
        npRefreshLayout.setTargetPull(true, dip2px(applicationContext, 4f))

        npRefreshLayout.setOnRefreshListener(object : AbsSwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                Toast.makeText(applicationContext, "pull refresh", Toast.LENGTH_SHORT).show()

                npRefreshLayout.postDelayed({
                    npRefreshLayout.setShowRemind(message = "Manual Refresh Message")
                    npRefreshLayout.isRefreshing = false
                }, 3000)

            }

        })

        // simulate auto refresh when first enter
        npRefreshLayout.postDelayed({ npRefreshLayout.isRefreshing = true }, 1000)
        npRefreshLayout.postDelayed({
            npRefreshLayout.setShowRemind(message = "Auto Refresh Message")
            npRefreshLayout.isRefreshing = false
            adapter.isLoadMoreEnable = true
        }, 5000)



    }


}
