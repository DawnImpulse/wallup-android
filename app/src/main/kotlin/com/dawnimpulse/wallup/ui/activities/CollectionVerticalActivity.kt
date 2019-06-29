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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.adapter.CollectionVerticalAdapter
import com.dawnimpulse.wallup.ui.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.ui.models.WallupViewModel
import com.dawnimpulse.wallup.ui.objects.CollectionTransferObject
import com.dawnimpulse.wallup.utils.functions.logd
import com.dawnimpulse.wallup.utils.functions.loge
import com.dawnimpulse.wallup.utils.functions.toast
import com.dawnimpulse.wallup.utils.reusables.COLLECTION
import com.dawnimpulse.wallup.utils.reusables.Config
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_general_recycler.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-28 by Saksham
 * @note Updates :
 */
class CollectionVerticalActivity : AppCompatActivity(), OnLoadMoreListener {
    private lateinit var col: CollectionTransferObject
    private lateinit var list: MutableList<Any?>
    private lateinit var adapter: CollectionVerticalAdapter
    private lateinit var model: WallupViewModel
    private var page = 2

    /**
     * on create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_recycler)


        if (savedInstanceState == null) {
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(generalRecycler)
        }

        col = Gson().fromJson(intent.getStringExtra(COLLECTION), CollectionTransferObject::class.java)
        model = WallupViewModel(this)
        setDetails()
    }


    /**
     * load more items
     */
    override fun onLoadMore() {
        model.getSortedCollectionImages(col.cid, page) { e, r ->

            // set loaded
            adapter.onLoaded()

            e?.let {
                loge(e)
                toast("error fetching images")
            }
            r?.let {

                page++

                // remove loading from list
                list.asSequence().withIndex().filter { it.value == null }.map { it.index }
                        .forEach {
                            list.removeAt(it)
                            adapter.notifyItemRemoved(it)
                        }

                if (it.isNotEmpty()) {
                    // add items
                    val count = list.size
                    list.addAll(it)
                    list.add(null)

                    adapter.notifyItemRangeInserted(count, it.size)
                }else
                    adapter.setLoadMore(null)
            }
        }
    }

    /**
     * clear disposables
     */
    override fun onDestroy() {
        Config.disposableCollectionsActivity.clear()
        logd("collection destroyed")
        super.onDestroy()
    }

    /**
     * set initial details
     */
    private fun setDetails() {
        list = mutableListOf()
        list.add(col)
        list.add(null)

        adapter = CollectionVerticalAdapter(list, generalRecycler)
        generalRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        generalRecycler.adapter = adapter

        model.getSortedCollectionImages(col.cid, 1) { e, r ->

            e?.let {
                loge(it)
                toast("error fetching images")

                // remove loading from list
                list.asSequence().withIndex().filter { it.value == null }.map { it.index }
                        .forEach {
                            list.removeAt(it)
                            adapter.notifyItemRemoved(it)
                        }
            }
            r?.let {

                // remove loading from list
                list.asSequence().withIndex().filter { it.value == null }.map { it.index }
                        .forEach {
                            list.removeAt(it)
                            adapter.notifyItemRemoved(it)
                        }

                // add items

                val count = list.size
                list.addAll(it)
                list.add(null)

                adapter.notifyItemRangeInserted(count, it.size)
                adapter.setLoadMore(this)
            }
        }
    }
}