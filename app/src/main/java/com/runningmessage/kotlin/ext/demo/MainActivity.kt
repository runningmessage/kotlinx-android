package com.runningmessage.kotlin.ext.demo

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.runningmessage.kotlin.ext.widget.AbsSwipeRefreshLayout
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

        val adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup?,
                viewType: Int
            ): RecyclerView.ViewHolder =
                object : RecyclerView.ViewHolder(TextView(applicationContext)) {}.apply {

                    val textView = this.itemView as? TextView
                    if (textView != null) {
                        textView.setBackgroundColor(Color.parseColor("#FFE2E2E2"))
                        textView.textSize = 30f
                        textView.setTextColor(Color.parseColor("#FF000000"))
                        textView.height = 200
                        textView.width = 300
                        textView.gravity = Gravity.CENTER
                    }
                }

            override fun getItemCount(): Int = 100

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
                val textView = (holder?.itemView as? TextView)
                textView?.text = position.toString()
                textView?.height = 200 + nextInt(100)
                textView?.width = 300
            }

        }
        npRecyclerView.adapter = adapter
        npRefreshLayout.setProgressViewEndTarget(true, dip2px(applicationContext, 32f))
        npRefreshLayout.setDistanceToTriggerSync(dip2px(applicationContext, 80f))
        npRefreshLayout.setTargetPull(true, dip2px(applicationContext, 4f))

        npRefreshLayout.postDelayed({ npRefreshLayout.isRefreshing = true }, 1000)
        npRefreshLayout.postDelayed({ npRefreshLayout.isRefreshing = false }, 5000)

        npRefreshLayout.setOnRefreshListener(object : AbsSwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                Toast.makeText(applicationContext, "pull refresh", Toast.LENGTH_SHORT).show()

                npRefreshLayout.postDelayed({ npRefreshLayout.isRefreshing = false }, 3000)

            }

        })

    }


}
