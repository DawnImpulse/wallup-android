package com.dawnimpulse.wallup.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.adapter.HomescreenAdapter
import com.dawnimpulse.wallup.ui.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.ui.models.WallupViewModel
import com.dawnimpulse.wallup.ui.objects.BannerObject
import com.dawnimpulse.wallup.ui.objects.HomescreenObject
import com.dawnimpulse.wallup.utils.functions.loge
import com.dawnimpulse.wallup.utils.functions.toastd
import com.dawnimpulse.wallup.utils.reusables.Config
import kotlinx.android.synthetic.main.activity_general.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-12 by Saksham
 * @note Updates :
 */
class HomescreenActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
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
        fetch(0, callback)
        generalSwipe.setOnRefreshListener(this)
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
        fetch(0, callback)
    }

    // ----------------
    //     refresh
    // ----------------
    override fun onLoadMore() {
        fetch(1, callbackPaginated)
    }

    // -----------------
    //     fetch items
    // -----------------
    private fun fetch(type: Int, callback: (Any?, HomescreenObject?) -> Unit) {
        when (type) {
            0 -> model.homescreen(callback)
            1 -> model.homescreenRandom(callback)
        }
    }

    // -----------------
    //    callback
    // -----------------
    private val callback = object : (Any?, HomescreenObject?) -> Unit {
        override fun invoke(e: Any?, r: HomescreenObject?) {
            generalSwipe.isRefreshing = false

            e?.let {
                loge(it)
                toastd(it.toString())
            }
            r?.let {
                if (::items.isInitialized)
                    items.clear()
                else
                    items = mutableListOf()

                val image = it.banner
                val images = it.images
                val cols = it.cols
                val homeCols = it.homescreen

                items.add(BannerObject(image!!, homeCols!!))

                var index = 0
                cols.forEach { wco ->
                    items.add(wco)
                    val itms = images.asSequence().withIndex().filter { i -> i.index >= index && i.index < index + 4 }.map { it.value }
                    items.addAll(itms)
                    index += itms.count()
                }

                items.add(null)

                if (!::adapter.isInitialized) {
                    adapter = HomescreenAdapter(items, generalRecycler)
                    generalRecycler.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                    generalRecycler.adapter = adapter
                } else
                    adapter.notifyDataSetChanged()

                adapter.setOnLoadMoreListener(this@HomescreenActivity)
            }
        }

    }


    // -----------------------
    //    callback paginated
    // -----------------------
    private val callbackPaginated = object : (Any?, HomescreenObject?) -> Unit {

        override fun invoke(e: Any?, r: HomescreenObject?) {

            adapter.onLoaded()

            e?.let {
                loge(it)
                toastd(it.toString())
            }
            r?.let {

                // remove progress bar
                items.asSequence().withIndex().filter { it.value == null }.map { it.index }
                        .forEach {
                            items.removeAt(it)
                            adapter.notifyItemRemoved(it)
                        }

                val images = it.images
                val cols = it.cols

                var index = 0
                cols.forEach { wco ->
                    items.add(wco)
                    val itms = images.asSequence().withIndex().filter { i -> i.index >= index && i.index < index + 4 }.map { it.value }
                    items.addAll(itms)
                    index += itms.count()
                }

                items.add(null)
            }
        }

    }
}
