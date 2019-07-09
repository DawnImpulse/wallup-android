/**
 * ISC License
 *
 * Copyright 2018-2019, Saksham (DawnImpulse)
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
 * WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
 * OR PERFORMANCE OF THIS SOFTWARE.
 **/
package com.dawnimpulse.wallup.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.adapter.RandomImagesAdapter
import com.dawnimpulse.wallup.ui.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.ui.models.WallupViewModel
import com.dawnimpulse.wallup.ui.objects.ImageObject
import com.dawnimpulse.wallup.utils.functions.*
import com.dawnimpulse.wallup.utils.reusables.Config
import com.dawnimpulse.wallup.utils.reusables.FAIL_LOAD_MORE
import com.dawnimpulse.wallup.utils.reusables.LOAD_MORE
import com.dawnimpulse.wallup.utils.reusables.RANDOM
import kotlinx.android.synthetic.main.activity_random_images.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-29 by Saksham
 * @note Updates :
 *  Saksham - 2019 07 09 - master - handling both sorted & random images
 */
class RandomActivity : AppCompatActivity(), View.OnClickListener, OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var adapter: RandomImagesAdapter
    private lateinit var items: MutableList<ImageObject?>
    private lateinit var model: WallupViewModel
    private var page = 1
    private var type = ""

    /**
     * on create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_images)

        model = WallupViewModel(this)

        fetch("", listener)
        Config.disposableRandomActivity.add(RxBus.subscribe { events(it) })

        randomSwipe.setOnRefreshListener(this)
        randomShuffle.setOnClickListener(this)
        randomLoad.setOnClickListener(this)
    }

    /**
     * clear disposables
     */
    override fun onDestroy() {
        super.onDestroy()

        Config.disposableRandomActivity.clear()
    }

    /**
     * click handling
     */
    override fun onClick(v: View?) {
        v?.let {
            when (v.id) {
                randomLoad.id -> {
                    randomLoad.gone()
                    randomP.show()
                    fetch(type, listener)
                }

                randomShuffle.id -> {
                    items.clear()
                    adapter.notifyDataSetChanged()
                    randomP.show()

                    type = RANDOM
                    fetch(RANDOM, listener)
                }
            }
        }
    }

    /**
     * get more items
     */
    override fun onLoadMore() {
        fetch(type, listenerPaginated)
    }

    /**
     * refresh items
     */
    override fun onRefresh() {
        page = 1
        type = ""
        fetch(type, listener)
    }

    /**
     * fetch images
     */
    private fun fetch(type: String, callback: (Any?, List<ImageObject>?) -> Unit) {
        when (type) {
            RANDOM -> model.getRandomImages(callback)
            else -> model.getSortedImages(page, callback)
        }
    }


    /**
     * string events handling
     */
    private fun events(event: String) {
        when (event) {
            LOAD_MORE -> {
                fetch(type, listenerPaginated)
            }
        }
    }

    /**
     * set items listener
     */
    private var listener = object : (Any?, List<ImageObject>?) -> Unit {
        override fun invoke(e: Any?, r: List<ImageObject>?) {

            randomSwipe.isRefreshing = false
            randomP.gone()

            e?.let {
                loge(it)
                toast("failed to fetch images")
                randomLoad.show()
            }
            r?.let {
                items = it.toMutableList()
                items.add(null)

                randomRecycler.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                adapter = RandomImagesAdapter(items, randomRecycler)
                randomRecycler.adapter = adapter
                adapter.setLoadMore(this@RandomActivity)

                if (type.isEmpty())
                    page++
            }
        }
    }


    /**
     * set paginated listener
     */
    private var listenerPaginated = object : (Any?, List<ImageObject>?) -> Unit {
        override fun invoke(e: Any?, r: List<ImageObject>?) {

            // on adapter loaded
            adapter.onLoaded()

            e?.let {
                loge(it)
                toast("failed to fetch images")
                RxBus.accept(FAIL_LOAD_MORE)
            }
            r?.let {

                // remove loading
                items.asSequence().withIndex().filter { it.value == null }.map { it.index }
                        .forEach {
                            items.removeAt(it)
                            adapter.notifyItemRemoved(it)
                        }

                val count = items.size
                items.addAll(it)
                items.add(null)

                if (count < 30)
                    adapter.setLoadMore(null)
                else
                    adapter.notifyItemRangeInserted(count, it.size + 1)

                if (type.isEmpty())
                    page++
            }
        }

    }
}