package com.dawnimpulse.wallup.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.adapter.HomescreenAdapter
import com.dawnimpulse.wallup.ui.models.WallupViewModel
import com.dawnimpulse.wallup.ui.objects.BannerObject
import com.dawnimpulse.wallup.ui.objects.HomescreenObject
import com.dawnimpulse.wallup.utils.Config
import com.dawnimpulse.wallup.utils.functions.logd
import com.dawnimpulse.wallup.utils.functions.loge
import com.dawnimpulse.wallup.utils.functions.toastd
import kotlinx.android.synthetic.main.activity_general.*

class HomescreenActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var model: WallupViewModel
    private lateinit var adapter: HomescreenAdapter
    private lateinit var items: MutableList<Any?>

    // -------------
    //    create
    // -------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general)

        model = WallupViewModel(this)
        fetch(1, callback)
    }

    // -----------------------
    //    disposables clear
    // -----------------------
    override fun onDestroy() {
        super.onDestroy()
        Config.disposableHomescreenActivity.clear()
        if (::adapter.isInitialized)
            adapter.onDetachedFromRecyclerView(generalRecycler)
    }

    // ----------------
    //     refresh
    // ----------------
    override fun onRefresh() {

    }

    // -----------------
    //     fetch items
    // -----------------
    private fun fetch(page: Int, callback: (Any?, HomescreenObject?) -> Unit) {
        model.homescreen(callback)
    }

    // -----------------
    //    callback
    // -----------------
    private val callback = object : (Any?, HomescreenObject?) -> Unit {

        override fun invoke(e: Any?, r: HomescreenObject?) {
            e?.let {
                loge(it)
                toastd(it.toString())
            }
            r?.let {

                val image = it.banner
                val images = it.images
                val cols = it.random
                val homeCols = it.homescreen

                logd("${images.size} :: ${cols.size} :: ${homeCols.size}")

                items = mutableListOf()
                items.add(BannerObject(image, homeCols))

                logd(items.size)

                var index = 0
                cols.forEach {
                    items.add(it)
                    val itms = images.asSequence().withIndex().filter { i -> i.index >= index && i.index < index + 4 }.map { it.value }
                    index += itms.count()
                    items.addAll(itms)
                    logd(items.size)
                }

                logd("yeah")

                adapter = HomescreenAdapter(items, generalRecycler)
                generalRecycler.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                generalRecycler.adapter = adapter
            }
        }

    }
}
