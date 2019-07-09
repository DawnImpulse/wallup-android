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
package com.dawnimpulse.wallup.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.holders.CollectionVerticalHolder
import com.dawnimpulse.wallup.ui.holders.CollectionVerticalInitialHolder
import com.dawnimpulse.wallup.ui.holders.LoadingFHolder
import com.dawnimpulse.wallup.ui.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.ui.objects.CollectionTransferObject
import com.dawnimpulse.wallup.ui.objects.ImageObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-28 by Saksham
 * @note Updates :
 */
class CollectionVerticalAdapter(
        val items: List<Any?>,
        recyclerView: RecyclerView
) : CustomAdapter(2, recyclerView) {


    private var onLoadMoreListener: OnLoadMoreListener? = null
    private lateinit var context: Context
    private val VIEW_INITIAL = 0
    private val VIEW_ITEM = 1
    private val VIEW_LOADING = 2

    /**
     * get count of items
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * get type of item
     */
    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is CollectionTransferObject -> VIEW_INITIAL
            is ImageObject -> VIEW_ITEM
            else -> VIEW_LOADING
        }
    }

    /**
     * creating item type
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context

        return when (viewType) {
            VIEW_INITIAL -> CollectionVerticalInitialHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_collections_vertical_init, parent, false))
            VIEW_ITEM -> CollectionVerticalHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_image_fullscreen, parent, false))
            else -> LoadingFHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_loading_full, parent, false))
        }
    }

    /**
     * data binding
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CollectionVerticalInitialHolder -> holder.bind(items[0] as CollectionTransferObject)
            is CollectionVerticalHolder -> holder.bind(items[position] as ImageObject)
            is LoadingFHolder -> holder.bind()
        }
    }

    /**
     * start animation & info
     */
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)

        if (holder is CollectionVerticalHolder) {

            // thread sleep
            GlobalScope.launch {
                delay(1500)
                (context as AppCompatActivity).runOnUiThread {
                    holder.hideInfo()
                }
            }

            // binding image
            holder.details(items[holder.adapterPosition] as ImageObject)
        }
    }

    /**
     * when loading triggers
     */
    override fun onLoading() {

        if (onLoadMoreListener != null)
            onLoadMoreListener!!.onLoadMore()
        else
            onLoaded()
    }

    /**
     *
     */
    fun setLoadMore(onLoadMoreListener: OnLoadMoreListener?) {
        this.onLoadMoreListener = onLoadMoreListener
    }
}