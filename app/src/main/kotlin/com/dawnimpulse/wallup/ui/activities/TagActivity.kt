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
import com.dawnimpulse.wallup.ui.objects.TagObject
import com.dawnimpulse.wallup.utils.functions.*
import com.dawnimpulse.wallup.utils.handlers.ImageHandler
import com.dawnimpulse.wallup.utils.reusables.Config
import com.dawnimpulse.wallup.utils.reusables.FAIL_LOAD_MORE
import com.dawnimpulse.wallup.utils.reusables.LOAD_MORE
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_tags.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-30 by Saksham
 * @note Updates :
 */
class TagActivity : AppCompatActivity(), View.OnClickListener, OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var model: WallupViewModel
    private lateinit var tag: TagObject
    private lateinit var adapter: RandomImagesAdapter
    private lateinit var items: MutableList<ImageObject?>

    /**
     * create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tags)

        model = WallupViewModel(this)
        tag = Gson().fromJson(intent.getStringExtra(com.dawnimpulse.wallup.utils.reusables.TAG), TagObject::class.java)

        setDetails()
        tagsSwipe.setOnRefreshListener(this)
        tagsLoad.setOnClickListener(this)
        Config.disposableRandomActivity.add(RxBus.subscribe { events(it) })
    }

    /**
     * clear disposable
     */
    override fun onDestroy() {
        super.onDestroy()
        Config.disposableRandomActivity.clear()
    }

    /**
     * on click
     */
    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                tagsLoad.id -> {
                    tagsLoad.gone()
                    tagsProgress.show()
                    fetch(listener)
                }
            }
        }
    }

    /**
     * get more items
     */
    override fun onLoadMore() {
        fetch(listenerPaginated)
    }

    /**
     * refresh items
     */
    override fun onRefresh() {
        fetch(listener)
    }

    /**
     * strings events
     */
    private fun events(event: String) {
        if (event == LOAD_MORE)
            fetch(listenerPaginated)
    }

    /**
     * fetch images
     */
    private fun fetch(callback: (Any?, List<ImageObject>?) -> Unit) {
        model.getRandomTagImages(tag.tag, callback)
    }

    /**
     * set details
     */
    private fun setDetails() {
        ImageHandler.setImageOnVerticalCols(tagsImage, tag.image)
        tagsName.text = F.capWord(tag.tag)
        fetch(listener)
    }

    /**
     * set items listener
     */
    private var listener = object : (Any?, List<ImageObject>?) -> Unit {
        override fun invoke(e: Any?, r: List<ImageObject>?) {

            tagsSwipe.isRefreshing = false
            tagsProgress.gone()

            e?.let {
                loge(it)
                toast("failed to fetch images")
                tagsLoad.show()
            }
            r?.let {
                items = it.toMutableList()
                if (items.size == 30)
                    items.add(null)

                tagsRecycler.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                adapter = RandomImagesAdapter(items, tagsRecycler)
                tagsRecycler.adapter = adapter

                if (items.size == 30)
                    adapter.setLoadMore(this@TagActivity)
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

                adapter.notifyItemRangeInserted(count, it.size + 1)
            }
        }

    }

}