/*
ISC License

Copyright 2018-2019, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/
package org.sourcei.wallup.deprecated.activities

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_general_collections.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.sourcei.wallup.deprecated.R
import org.sourcei.wallup.deprecated.utils.L
import org.sourcei.wallup.deprecated.utils.gone
import org.sourcei.wallup.deprecated.utils.show
import org.sourcei.wallup.deprecated.utils.toast

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-12-18 by Saksham
 *
 * @note Updates :
 */
class GeneralCollectionsActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, org.sourcei.wallup.deprecated.interfaces.OnLoadMoreListener {
    private val NAME = "GeneralCollectionsActivity"
    private lateinit var model: org.sourcei.wallup.deprecated.models.UnsplashModel
    private lateinit var cols: MutableList<org.sourcei.wallup.deprecated.pojo.CollectionPojo?>
    private lateinit var adapter: org.sourcei.wallup.deprecated.adapters.CollectionsAdapter
    private lateinit var username: String
    private var nextPage = 2

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_collections)

        model = org.sourcei.wallup.deprecated.models.UnsplashModel(lifecycle)
        username = intent.getStringExtra(org.sourcei.wallup.deprecated.utils.C.USERNAME)

        model.userCollections(username, 1, 30, callback)
        generalCatReload.setOnClickListener {
            generalCatReload.gone()
            generalCatProgress.show()
            model.userCollections(username, 1, 30, callback)
        }

        generalCatProgressI.animation = AnimationUtils.loadAnimation(this, R.anim.rotation_progress)
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
    fun onEvent(event: org.sourcei.wallup.deprecated.utils.Event) {
        if (event.obj.has(org.sourcei.wallup.deprecated.utils.C.TYPE)) {
            if (event.obj.getString(org.sourcei.wallup.deprecated.utils.C.TYPE) == org.sourcei.wallup.deprecated.utils.C.NETWORK) {
                runOnUiThread {
                    if (event.obj.getBoolean(org.sourcei.wallup.deprecated.utils.C.NETWORK)) {
                        generalColConnLayout.setBackgroundColor(org.sourcei.wallup.deprecated.utils.Colors(this).GREEN)
                        generalColConnText.text = "Back Online"
                        GlobalScope.launch {
                            delay(1500)
                            runOnUiThread {
                                generalColConnLayout.visibility = View.GONE
                            }
                        }
                    } else {
                        generalColConnLayout.visibility = View.VISIBLE
                        generalColConnLayout.setBackgroundColor(org.sourcei.wallup.deprecated.utils.Colors(this).LIKE)
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
                cols = (response as List<org.sourcei.wallup.deprecated.pojo.CollectionPojo>).toMutableList()
                adapter = org.sourcei.wallup.deprecated.adapters.CollectionsAdapter(lifecycle, cols, org.sourcei.wallup.deprecated.utils.C.FEATURED, generalCatRecycler)
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
                cols.addAll(response as List<org.sourcei.wallup.deprecated.pojo.CollectionPojo>)
                adapter.notifyDataSetChanged()
                adapter.setLoaded()
            }
        }
    }
}
