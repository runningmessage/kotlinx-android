package com.runningmessage.kotlinx.demo.reflect

import android.content.Context
import android.view.ViewGroup
import android.widget.Toast
import com.runningmessage.kotlinx.common.dpToPx
import com.runningmessage.kotlinx.common.postDelayed
import com.runningmessage.kotlinx.demo.R
import com.runningmessage.kotlinx.reflect.*
import kotlin.random.Random

/**
 * Created by Lorss on 20-1-8.
 *
 * Just import kotlinx-reflect, to invoke code in kotlinx-widget and android support
 */

@Suppress("PrivatePropertyName")
class ReflectDemo {


    companion object {

        //formatter:off
        /*************************************************************/
        /********************* Import to reflect *********************/
        /*************************************************************/

        private const val MyLoadMoreRecyclerAdapter = "com.runningmessage.kotlinx.demo.MyLoadMoreRecyclerAdapter"
        private const val OnLoadMoreListener = "com.runningmessage.kotlinx.widget.LoadMoreRecyclerAdapter${'$'}OnLoadMoreListener"
        private const val OnRefreshListener = "com.runningmessage.kotlinx.widget.AbsSwipeRefreshLayout${'$'}OnRefreshListener"
        private const val StaggeredGridLayoutManager = "android.support.v7.widget.StaggeredGridLayoutManager"

        //formatter:on

        private lateinit var activity: Any

        fun onCreate(activity: Any) {
            this.activity = activity
            this.activity.runInActivity()
        }

        private fun Any.runInActivity() {// this is Activity Object

            /***
             * setContentView(R.layout.activity_main)
             * ***/
            calls("setContentView")(R.layout.activity_main)

            /***
             * val npRecyclerView = findViewById<ViewGroup>(R.id.npRecyclerView)
             * ***/
            val npRecyclerView = call<ViewGroup>("findViewById")(R.id.npRecyclerView)
            /***
             * val npRefreshLayout = findViewById<ViewGroup>(R.id.npRefreshLayout)
             * ***/
            val npRefreshLayout = call<ViewGroup>("findViewById")(R.id.npRefreshLayout)

            // set the load more recycler adapter
            val applicationContext = call<Context>("getApplicationContext")()

            /***
             * val adapter = com.runningmessage.kotlinx.demo.MyLoadMoreRecyclerAdapter(applicationContext)
             * ***/
            val adapter: Any? = MyLoadMoreRecyclerAdapter(applicationContext)

            adapter.propertys("isAutoLoadMore").value = false

            /***
             * adapter.onLoadMoreListener = object: com.runningmessage.kotlinx.widget.LoadMoreRecyclerAdapter.OnLoadMoreListener{
             *
             *      override fun onLoadMore(){
             *
             *      }
             *
             * }
             * ***/
            adapter.propertys("onLoadMoreListener").value = OnLoadMoreListener.createInners {

                override("onLoadMore") {

                    npRecyclerView?.postDelayed({
                        val random = Random.nextInt(10)
                        if (random < 5) {
                            val list = ArrayList<String>()
                            val count = adapter.property<Int>("dataCount").value ?: 0
                            for (i in count + 1..count + 10) {
                                list.add(i.toString())
                            }
                            adapter.call<Any>("addData")(list, null)
                            adapter.propertys("footerMessage").value = "加载成功"
                            adapter.calls("notifyDataSetChanged")()
                        } else {
                            adapter.propertys("footerMessage").value = "没有更多数据"
                        }
                        adapter.property<Boolean>("isLoading").value = false
                    }, 3000)
                }
            }


            npRecyclerView.calls("setAdapter")(adapter)


            // set the init data for adapter
            val list = ArrayList<String>()
            for (i in 1..9) {
                list.add(i.toString())
            }
            adapter.calls("setData")(list)

            // set the recyclerView
            npRecyclerView.calls("setLayoutManager")(StaggeredGridLayoutManager(2, 1))
            npRecyclerView.calls("setItemAnimator")(null)

            // set the refresh layout
            npRefreshLayout.calls("setProgressViewEndTarget")(true, applicationContext?.dpToPx(32f)
                    ?: 64)
            npRefreshLayout.calls("setDistanceToTriggerSync")(applicationContext?.dpToPx(80f)
                    ?: 160)
            npRefreshLayout.calls("setTargetPull")(true)

            npRefreshLayout.calls("setOnRefreshListener")(OnRefreshListener.createInners {

                override("onRefresh") {
                    val autoNotify: Boolean = it?.get(0) as? Boolean ?: false

                    adapter.propertys("isLoadMoreEnable").value = false
                    val message = if (autoNotify) "Auto Refresh Message" else "Swipe Refresh Message"
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                    npRefreshLayout?.postDelayed({
                        npRefreshLayout.calls("setShowRemind")(true, message)
                        npRefreshLayout.propertys("isRefreshing").value = false
                        adapter.propertys("isLoadMoreEnable").value = true
                    }, 3000)
                }
            })


            // simulate auto refresh when first enter
            npRefreshLayout?.postDelayed(1000) {
                //npRefreshLayout.notifyRefresh(true, true)
                npRefreshLayout.calls("notifyRefresh")(true, true)
            }
        }
    }


}

