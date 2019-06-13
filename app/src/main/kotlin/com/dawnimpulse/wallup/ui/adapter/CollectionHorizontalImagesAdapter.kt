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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.holders.BannerCollectionHolder
import com.dawnimpulse.wallup.ui.holders.LoadingHolder
import com.dawnimpulse.wallup.ui.holders.WallupCollectionHolder
import com.dawnimpulse.wallup.ui.holders.WallupImageHolder
import com.dawnimpulse.wallup.ui.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.ui.objects.BannerCollectionObject
import com.dawnimpulse.wallup.ui.objects.WallupImageObject
import com.dawnimpulse.wallup.utils.Config

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-13 by Saksham
 * @note Updates :
 */
class CollectionHorizontalImagesAdapter(
        val items: List<Any?>,
        val recycler: RecyclerView
) : CustomAdapter(Config.disposableCollectionsActivity, recycler) {

    private val VIEW_ITEM = 0
    private val VIEW_LOADING = 1
    private val VIEW_BANNER = 2
    private lateinit var onLoadMoreListener:OnLoadMoreListener

    // -----------------
    //    item count
    // -----------------
    override fun getItemCount(): Int {
        return items.size
    }

    // -----------------
    //    item type
    // -----------------
    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            null -> VIEW_LOADING
            is BannerCollectionHolder -> VIEW_BANNER
            else -> VIEW_ITEM
        }
    }

    // -------------
    //    create
    // -------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_LOADING -> LoadingHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_loading, parent, false))
            VIEW_BANNER -> BannerCollectionHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_banner_collection, parent, false))
            else -> WallupCollectionHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_image, parent, false))
        }
    }

    // ------------
    //    bind
    // ------------
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WallupImageHolder -> holder.bind(items[position] as WallupImageObject)
            is BannerCollectionHolder -> {
                (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
                holder.bind(items[position] as BannerCollectionObject)
            }
        }
    }

    // ---------------
    //    loading
    // ---------------
    override fun onLoading() {
        super.onLoading()
        onLoadMoreListener.onLoadMore()
    }

    // --------------------
    //     set listener
    // --------------------
    fun setOnLoadMoreListener(loadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = loadMoreListener
    }

}