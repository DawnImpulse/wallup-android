package com.dawnimpulse.wallup.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.adapter.WallupCollectionAdapter
import com.dawnimpulse.wallup.ui.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.ui.models.WallupViewModel
import com.dawnimpulse.wallup.ui.objects.WallupCollectionObject
import com.dawnimpulse.wallup.utils.reusables.Config
import com.dawnimpulse.wallup.utils.functions.RxBusTime
import com.dawnimpulse.wallup.utils.functions.F
import com.dawnimpulse.wallup.utils.functions.loge
import com.dawnimpulse.wallup.utils.functions.toastd
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_general.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-11 by Saksham
 * @note Updates :
 */
class CollectionsActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    private lateinit var wallupModel: WallupViewModel
    private lateinit var adapter: WallupCollectionAdapter
    private lateinit var items: MutableList<WallupCollectionObject?>
    private var compositeDisposable = CompositeDisposable()
    private var page: Int = 2
    private var next = true


    // -------------------
    //      on create
    // -------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general)

        wallupModel = WallupViewModel(this)
        compositeDisposable.add(
                F.publishInterval()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            RxBusTime.accept(it)
                        }
        )

        fetchData(1, callback)
        generalSwipe.setOnRefreshListener(this)
    }

    // -------------------
    //      dispose
    // -------------------
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
        Config.disposableCollectionsActivity.clear()
        adapter.onDetachedFromRecyclerView(generalRecycler)
    }

    // ----------------------
    //      refresh list
    // ----------------------
    override fun onRefresh() {
        next = true
        fetchData(1, callback)
    }

    // -------------------------
    //     load more items
    // -------------------------
    override fun onLoadMore() {
        if (next)
            fetchData(page, callbackPaginated)
    }

    // -------------------
    //     fetch data
    // -------------------
    private fun fetchData(page: Int, callback: (Any?, List<WallupCollectionObject?>?) -> Unit) {
        wallupModel.getSortedCollections(page, callback)
    }

    // ------------------
    //      callback
    // ------------------
    private var callback = object : (Any?, List<WallupCollectionObject?>?) -> Unit {
        override fun invoke(e: Any?, r: List<WallupCollectionObject?>?) {

            // error
            e?.let {
                loge(e)
                toastd(e.toString())
                generalSwipe.isRefreshing = false
            }

            // response
            r?.let {
                if (::items.isInitialized)
                    items.clear()
                items = it.toMutableList()
                adapter = WallupCollectionAdapter(generalRecycler, items)
                generalRecycler.layoutManager = LinearLayoutManager(this@CollectionsActivity)
                generalRecycler.adapter = adapter
                generalSwipe.isRefreshing = false

                items.add(null)
                adapter.notifyDataSetChanged()
                adapter.setOnLoadMoreListener(this@CollectionsActivity)
            }
        }

    }

    // -----------------------------
    //      callback paginated
    // -----------------------------
    private var callbackPaginated = object : (Any?, List<WallupCollectionObject?>?) -> Unit {
        override fun invoke(e: Any?, r: List<WallupCollectionObject?>?) {

            // set is loading false
            adapter.onLoaded()

            // error
            e?.let {
                loge(e)
                toastd(e.toString())
            }

            // response
            r?.let {

                // remove progress bar
                items.asSequence().withIndex().filter { it.value == null }.map { it.index }
                        .forEach {
                            items.removeAt(it)
                            adapter.notifyItemRemoved(it)
                        }

                if (it.isEmpty()) {
                    next = false
                } else {
                    page++
                    items.addAll(it)
                    items.add(null)
                    adapter.notifyItemRangeInserted(items.size, it.size + 1)
                }
            }
        }

    }
}
