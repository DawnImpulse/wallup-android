package com.dawnimpulse.wallup.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.adapter.WallupCollectionAdapter
import com.dawnimpulse.wallup.ui.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.ui.models.WallupCollectionsViewModel
import com.dawnimpulse.wallup.ui.objects.WallupCollectionObject
import com.dawnimpulse.wallup.utils.*
import com.dawnimpulse.wallup.utils.functions.loge
import com.dawnimpulse.wallup.utils.functions.toast
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
class CollectionsActivity : AppCompatActivity(), OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var wallupModel: WallupCollectionsViewModel
    private lateinit var adapter: WallupCollectionAdapter
    private var compositeDisposable = CompositeDisposable()

    // general event on bus
    private fun events(event: String) {
        when (event) {
            // refreshed
            REFRESHED_WALLUP_COLLECTIONS_SORTED -> generalSwipe.isRefreshing = false

            // loaded
            LOADED_WALLUP_COLLECTIONS_SORTED -> {
                adapter.setLoaded()
            }

            // finished
            FINISHED_WALLUP_COLLECTIONS_SORTED -> {
                if (::adapter.isInitialized)
                    adapter.setLoaded()
            }

        }
    }

    // error events on bus
    private fun errorEvents(error: RxErrorBusObject) {
        when (error.type) {
            ERROR_WALLUP_COLLECTIONS_SORTED -> {
                loge(error.error ?: "error fetching collections")
                toast("error fetching collections")
                generalSwipe.isRefreshing = false
            }
        }
    }

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general)
        wallupModel = ViewModelProviders.of(this).get(WallupCollectionsViewModel::class.java)
        wallupModel.sortedCollections.observe(this, collectionsObserver())
        compositeDisposable.add(RxErrorBus.subscribe { errorEvents(it) })
        compositeDisposable.add(RxBus.subscribe { events(it) })
        generalSwipe.setOnRefreshListener(this)
    }

    // clear disposables
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    // load more items
    override fun onLoadMore() {
        RxBus.accept(LOAD_MORE_WALLUP_COLLECTIONS_SORTED)
    }

    // refreshing data
    override fun onRefresh() {
        RxBus.accept(REFRESH_WALLUP_COLLECTIONS_SORTED)
    }

    // collection live data
    private fun collectionsObserver(): Observer<MutableList<WallupCollectionObject>> {
        return Observer {
            if (::adapter.isInitialized) {
                adapter.notifyDataSetChanged()
            } else {
                adapter = WallupCollectionAdapter(it, generalRecycler)
                generalRecycler.layoutManager = LinearLayoutManager(this)
                generalRecycler.adapter = adapter
                adapter.setOnLoadMoreListener(this)
            }
        }
    }
}
