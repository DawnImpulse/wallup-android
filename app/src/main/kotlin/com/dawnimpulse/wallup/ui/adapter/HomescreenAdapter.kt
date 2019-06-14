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
import com.dawnimpulse.wallup.ui.holders.BannerHolder
import com.dawnimpulse.wallup.ui.holders.CollectionHolder
import com.dawnimpulse.wallup.ui.holders.LoadingHolder
import com.dawnimpulse.wallup.ui.holders.WallupImageHolder
import com.dawnimpulse.wallup.ui.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.ui.objects.BannerObject
import com.dawnimpulse.wallup.ui.objects.WallupCollectionObject
import com.dawnimpulse.wallup.ui.objects.WallupImageObject
import com.dawnimpulse.wallup.utils.reusables.Config


/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-12 by Saksham
 * @note Updates :
 */
class HomescreenAdapter(
        val items: List<Any?>,
        recycler: RecyclerView
) : CustomAdapter(Config.disposableHomescreenActivity, recycler) {

    private val VIEW_BANNER = 0
    private val VIEW_COLLECTION = 1
    private val VIEW_LOADING = 2
    private val VIEW_IMAGE = 3
    private lateinit var onLoadMoreListener:OnLoadMoreListener

    // -------------------
    //      items
    // -------------------
    override fun getItemCount(): Int {
        return items.size
    }

    // -------------------
    //      item type
    // -------------------
    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            null -> VIEW_LOADING
            is WallupImageObject -> VIEW_IMAGE
            is BannerObject -> VIEW_BANNER
            is WallupCollectionObject -> VIEW_COLLECTION
            else -> VIEW_LOADING
        }
    }

    // -------------------
    //      create
    // -------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_LOADING -> LoadingHolder(LayoutInflater.from(parent.context).inflate(com.dawnimpulse.wallup.R.layout.inflator_loading, parent, false))
            VIEW_BANNER -> BannerHolder(LayoutInflater.from(parent.context).inflate(com.dawnimpulse.wallup.R.layout.inflator_banner, parent, false))
            VIEW_COLLECTION -> CollectionHolder(LayoutInflater.from(parent.context).inflate(com.dawnimpulse.wallup.R.layout.inflator_homescreen_collection, parent, false))
            VIEW_IMAGE -> WallupImageHolder(LayoutInflater.from(parent.context).inflate(com.dawnimpulse.wallup.R.layout.inflator_image, parent, false))
            else -> LoadingHolder(LayoutInflater.from(parent.context).inflate(com.dawnimpulse.wallup.R.layout.inflator_loading, parent, false))
        }
    }

    // ---------------------
    //      bind views
    // ---------------------
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is BannerHolder -> {
                (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
                holder.bind(items[position] as BannerObject)
            }
            is WallupImageHolder -> holder.bind(items[position] as WallupImageObject)
            is CollectionHolder -> holder.bind(items[position] as WallupCollectionObject)
        }
    }

    // ------------------------
    //      dispose recycled
    // ------------------------
    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is CollectionHolder)
            Config.disposableCollectionViewHolder[holder.adapterPosition]?.dispose()
    }

    // -------------------------
    //      clear disposables
    // -------------------------
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        Config.disposableCollectionViewHolder.forEach { it.value.dispose() }
    }

    // --------------------
    //     set listener
    // --------------------
    fun setOnLoadMoreListener(loadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = loadMoreListener
    }

    // ---------------
    //     loading
    // ---------------
    override fun onLoading() {
        super.onLoading()

        onLoadMoreListener.onLoadMore()
    }
}
