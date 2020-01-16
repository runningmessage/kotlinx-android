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
 * Just import kotlinx-reflect and android framework package,
 *
 * to invoke all code in other package (e.g. kotlinx-widget and android support etc)
 */

@Suppress("PrivatePropertyName")
class ReflectDemo {


    companion object {

        //@formatter:off
        /*************************************************************/
        /********************* Import to reflect *********************/
        /*************************************************************/

        private const val MyLoadMoreRecyclerAdapter     = "com.runningmessage.kotlinx.demo.MyLoadMoreRecyclerAdapter"

        private const val packageKotlinxWidget          = "com.runningmessage.kotlinx.widget"
        private const val LoadMoreRecyclerAdapter       = "$packageKotlinxWidget.LoadMoreRecyclerAdapter"
        private const val OnLoadMoreListener            = "$LoadMoreRecyclerAdapter${'$'}OnLoadMoreListener"
        private const val AbsSwipeRefreshLayout         = "$packageKotlinxWidget.AbsSwipeRefreshLayout"
        private const val OnRefreshListener             = "$AbsSwipeRefreshLayout${'$'}OnRefreshListener"

        private const val packageSupport                = "android.support.v7"
        private const val AppCompatActivity             = "$packageSupport.app.AppCompatActivity"
        private const val packageSupportWidget          = "$packageSupport.widget"
        private const val RecyclerView                  = "$packageSupportWidget.RecyclerView"
        private const val StaggeredGridLayoutManager    = "$packageSupportWidget.StaggeredGridLayoutManager"

        //@formatter:on

        private lateinit var activity: Any

        fun onCreate(activity: Any) {
            this.activity = activity
            this.activity.ifIs(AppCompatActivity) { appCompatActivity: Any ->
                appCompatActivity.runInActivity()
            }
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

            val applicationContext = call<Context>("getApplicationContext")() ?: return

            /***
             * val adapter = com.runningmessage.kotlinx.demo.MyLoadMoreRecyclerAdapter(applicationContext)
             * ***/
            val adapter: Any? = MyLoadMoreRecyclerAdapter(applicationContext)

            npRecyclerView.ifIs(RecyclerView) { recyclerView: ViewGroup ->

                adapter.ifIs(LoadMoreRecyclerAdapter) { adapter: Any ->

                    initView(recyclerView, adapter, npRefreshLayout, applicationContext)
                }
            }


        }

        private fun initView(
            recyclerView: ViewGroup,
            adapter: Any,
            refreshLayout: ViewGroup?,
            context: Context
        ) {

            // set the load more recycler adapter
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

                    recyclerView.postDelayed({
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


            recyclerView.calls("setAdapter")(adapter)


            // set the init data for adapter
            val list = ArrayList<String>()
            for (i in 1..9) {
                list.add(i.toString())
            }
            adapter.calls("setData")(list)

            // set the recyclerView
            recyclerView.calls("setLayoutManager")(StaggeredGridLayoutManager(2, 1))
            recyclerView.calls("setItemAnimator")(null)

            // set the refresh layout
            refreshLayout.ifIs(AbsSwipeRefreshLayout) { layout: ViewGroup ->
                initRefreshLayout(layout, adapter, context)
            }
        }

        private fun initRefreshLayout(
            layout: ViewGroup,
            adapter: Any,
            applicationContext: Context
        ) {
            layout.calls("setProgressViewEndTarget")(
                true, applicationContext.dpToPx(32f)
            )
            layout.calls("setDistanceToTriggerSync")(
                applicationContext.dpToPx(80f)
            )
            layout.calls("setTargetPull")(true)

            layout.calls("setOnRefreshListener")(OnRefreshListener.createInners {

                override<Boolean>("onRefresh") { autoNotify ->

                    val autoNotifyNo = autoNotify ?: false

                    adapter.propertys("isLoadMoreEnable").value = false
                    val message =
                        if (autoNotifyNo) "Auto Refresh Message" else "Swipe Refresh Message"
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                    layout.postDelayed({
                        layout.calls("setShowRemind")(true, message)
                        layout.propertys("isRefreshing").value = false
                        adapter.propertys("isLoadMoreEnable").value = true
                    }, 3000)
                }
            })


            // simulate auto refresh when first enter
            layout.postDelayed(1000) {
                //npRefreshLayout.notifyRefresh(true, true)
                layout.calls("notifyRefresh")(true, true)
            }
        }
    }


}

