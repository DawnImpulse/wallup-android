package com.dawnimpulse.wallup.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.adapter.HomeAdapter
import com.dawnimpulse.wallup.ui.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.utils.functions.putAny
import com.dawnimpulse.wallup.utils.functions.toJson
import com.dawnimpulse.wallup.utils.functions.toast
import com.dawnimpulse.wallup.utils.reusables.Config
import com.dawnimpulse.wallup.utils.reusables.EDITORIAL_IMAGES
import com.dawnimpulse.wallup.utils.reusables.HOME_IMAGES
import com.dawnimpulse.wallup.utils.reusables.Prefs
import com.dawnimpulse.wallup.workers.CacheHomeImages
import kotlinx.android.synthetic.main.activity_homescreen2.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Homescreen2Activity : AppCompatActivity(), OnLoadMoreListener {
    private lateinit var adapter: HomeAdapter
    private lateinit var list: MutableList<Any>

    // -------------
    //    create
    // -------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen2)

        if (savedInstanceState == null) {
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(recyclerDemo)
        }

        list = mutableListOf(1)
        adapter = HomeAdapter(list, recyclerDemo)
        recyclerDemo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerDemo.adapter = adapter

        /*fetch {
            list = it.toMutableList()
            recyclerDemo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            adapter = HomeAdapter(list, recyclerDemo)
            recyclerDemo.adapter = adapter
            adapter.setOnLoadMoreListener(this)
        }*/

    }

    // -------------
    //    destroy
    // -------------
    override fun onDestroy() {
        val uploadWorkRequest = OneTimeWorkRequestBuilder<CacheHomeImages>().build()
        WorkManager.getInstance().enqueue(uploadWorkRequest)

        Prefs.putAny(HOME_IMAGES, toJson(Config.homeImages))
        Prefs.putAny(EDITORIAL_IMAGES, toJson(Config.editorialImages))
        super.onDestroy()
    }

    // ---------------
    //    load more
    // ---------------
    override fun onLoadMore() {
        toast("loading")
        fetch {
            val pos = list.size
            list.addAll(it)
            adapter.notifyItemRangeInserted(pos, it.count())
            adapter.onLoaded()
            toast("loaded")
        }
    }


    private fun fetch(callback: (List<String>) -> Unit) {
        GlobalScope.launch {
            Thread.sleep(2000)
            runOnUiThread {
                callback((1..10).map { it.toString() })
            }
        }
    }
}
