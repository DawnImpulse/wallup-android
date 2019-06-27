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
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.holders.ExploreCollectionsHolder
import com.dawnimpulse.wallup.ui.holders.LoadingHolder
import com.dawnimpulse.wallup.ui.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.ui.objects.CollectionHomescreenObject
import com.dawnimpulse.wallup.utils.reusables.Config

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-26 by Saksham
 * @note Updates :
 */
class ExploreCollectionsAdapter(val items: List<CollectionHomescreenObject?>, recyclerView: RecyclerView)
    : CustomAdapter(Config.disposableHomescreenActivity, recyclerView) {

    private val VIEW_ITEM = 0
    private val VIEW_LOADING = 1
    private lateinit var onLoadMoreListener: OnLoadMoreListener

    /**
     * get count
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * get type of item
     */
    override fun getItemViewType(position: Int): Int {
        return if (items[position] == null)
            VIEW_LOADING
        else
            VIEW_ITEM
    }

    /**
     * create
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_ITEM)
            ExploreCollectionsHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_collections_vertical_cards, parent, false))
        else
            LoadingHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_loading_cols_vertical, parent, false))
    }

    /**
     * binding
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ExploreCollectionsHolder)
            holder.bind(items[position]!!)
    }

    /**
     * loading
     */
    override fun onLoading() {
        onLoadMoreListener.onLoadMore()
    }

    /**
     * attaching load more listener
     */
    fun setLoadMoreListener(onLoadMoreListener: OnLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener
    }
}