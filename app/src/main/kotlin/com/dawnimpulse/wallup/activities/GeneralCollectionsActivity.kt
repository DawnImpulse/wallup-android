/*
ISC License

Copyright 2018, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/
package com.dawnimpulse.wallup.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.adapters.CollectionsAdapter
import com.dawnimpulse.wallup.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.CollectionPojo
import com.dawnimpulse.wallup.utils.*
import kotlinx.android.synthetic.main.activity_general_collections.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-12-18 by Saksham
 *
 * @note Updates :
 */
class GeneralCollectionsActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    private val NAME = "GeneralCollectionsActivity"
    private lateinit var model: UnsplashModel
    private lateinit var cols: MutableList<CollectionPojo?>
    private lateinit var adapter: CollectionsAdapter
    private lateinit var username: String
    private var nextPage = 2

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_collections)

        model = UnsplashModel(lifecycle)
        username = intent.getStringExtra(C.USERNAME)

        model.userCollections(username, 1, 30, callback)
        generalCatReload.setOnClickListener {
            generalCatReload.gone()
            generalCatProgress.show()
            model.userCollections(username, 1, 30, callback)
        }
    }

    // on start
    override fun onStart() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
        super.onStart()
    }

    // on destroy
    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    // on refresh
    override fun onRefresh() {
        model.userCollections(username, 1, 30, callback)
    }

    // on load more
    override fun onLoadMore() {
        cols.add(null)
        adapter.notifyItemInserted(cols.size)
        model.userCollections(username, nextPage, 30, callbackPaginated)
    }

    // on message event
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: Event) {
        if (event.obj.has(C.TYPE)) {
            if (event.obj.getString(C.TYPE) == C.NETWORK) {
                runOnUiThread {
                    if (event.obj.getBoolean(C.NETWORK)) {
                        generalColConnLayout.setBackgroundColor(Colors(this).GREEN)
                        generalColConnText.text = "Back Online"
                        launch {
                            delay(1500)
                            runOnUiThread {
                                generalColConnLayout.visibility = View.GONE
                            }
                        }
                    } else {
                        generalColConnLayout.visibility = View.VISIBLE
                        generalColConnLayout.setBackgroundColor(Colors(this).LIKE)
                        generalColConnText.text = "No Internet"
                    }
                }
            }
        }
    }

    // callback for setting images in adapter
    private var callback = object : (Any?, Any?) -> Unit {
        override fun invoke(error: Any?, response: Any?) {
            error?.let {
                L.d(NAME, error)
                generalCatSwipe.isRefreshing = false
                generalCatProgress.gone()
                generalCatReload.show()
            }
            response?.let {
                cols = (response as List<CollectionPojo>).toMutableList()
                adapter = CollectionsAdapter(lifecycle, cols, C.FEATURED, generalCatRecycler)
                generalCatRecycler.layoutManager = LinearLayoutManager(this@GeneralCollectionsActivity)
                generalCatRecycler.adapter = adapter
                generalCatSwipe.isRefreshing = false
                generalCatSwipe.show()
                generalCatRecycler.show()
                generalCatProgress.gone()
                generalCatReload.gone()

                adapter.setOnLoadMoreListener(this@GeneralCollectionsActivity)
            }
        }
    }

    // callback for setting images in adapter
    private var callbackPaginated = object : (Any?, Any?) -> Unit {
        override fun invoke(error: Any?, response: Any?) {
            error?.let {
                L.d(NAME, error)
                toast("unable to fetch collections")
            }
            response?.let {
                nextPage++
                cols.removeAt(cols.size - 1)
                adapter.notifyItemRemoved(cols.size - 1)
                cols.addAll(response as List<CollectionPojo>)
                adapter.notifyDataSetChanged()
                adapter.setLoaded()
            }
        }
    }
}
