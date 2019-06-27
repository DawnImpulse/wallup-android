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
package com.dawnimpulse.wallup.ui.holders

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.ui.adapter.ExploreCollectionsAdapter
import com.dawnimpulse.wallup.ui.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.ui.models.WallupViewModel
import com.dawnimpulse.wallup.ui.objects.CollectionHomescreenObject
import com.dawnimpulse.wallup.ui.objects.ExploreObject
import com.dawnimpulse.wallup.utils.functions.loge
import com.dawnimpulse.wallup.utils.functions.toast
import com.dawnimpulse.wallup.utils.handlers.ImageHandler
import kotlinx.android.synthetic.main.inflator_explore.view.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-26 by Saksham
 * @note Updates :
 */
class ExploreHolder(view: View) : RecyclerView.ViewHolder(view), OnLoadMoreListener {
    val down = view.exploreDown
    private val recycler = view.exploreCols
    private val bg = view.exploreBg

    private val context = view.context
    private lateinit var items: MutableList<CollectionHomescreenObject?>
    private lateinit var adapter: ExploreCollectionsAdapter
    private val model = WallupViewModel(context as AppCompatActivity)


    /**
     * binding data to views
     */
    fun bind(data: ExploreObject) {

        if (!::adapter.isInitialized) {
            PagerSnapHelper().attachToRecyclerView(recycler)

            // get items
            items = data.cols.toMutableList()
            items.add(null)

            // vertical adapter handling
            adapter = ExploreCollectionsAdapter(items, recycler)
            recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recycler.adapter = adapter

            adapter.setLoadMoreListener(this)
        }

        ImageHandler.setImageOnHomescreenBackground(bg, data.image)
    }

    /**
     * loading more
     */
    override fun onLoadMore() {
        model.getHomescreenCols { e, r ->

            adapter.onLoaded()

            e?.let {
                loge(it)
                context.toast("error fetching")
            }

            r?.let {

                // remove loading
                items.asSequence().withIndex().filter { it.value == null }.map { it.index }
                        .forEach {
                            items.removeAt(it)
                            adapter.notifyItemRemoved(it)
                        }

                // set items
                val count = items.size
                items.addAll(it)
                items.add(null)

            }
        }
    }

}