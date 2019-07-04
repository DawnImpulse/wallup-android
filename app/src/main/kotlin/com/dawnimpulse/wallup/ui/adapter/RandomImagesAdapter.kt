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
import com.dawnimpulse.wallup.ui.holders.LoadingHolder
import com.dawnimpulse.wallup.ui.holders.RandomImageHolder
import com.dawnimpulse.wallup.ui.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.ui.objects.ImageObject

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-29 by Saksham
 * @note Updates :
 */
class RandomImagesAdapter(
        val items: List<ImageObject?>,
        recyclerView: RecyclerView
) : CustomAdapter(10, recyclerView) {

    private var onLoadMoreListener: OnLoadMoreListener? = null
    private val VIEW_ITEM = 0
    private val VIEW_LOADING = 1

    /**
     * get item count
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * get item type
     */
    override fun getItemViewType(position: Int): Int {
        return if (items[position] == null)
            VIEW_LOADING
        else
            VIEW_ITEM
    }

    /**
     * create view based on type
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_ITEM)
            RandomImageHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_image, parent, false))
        else
            LoadingHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_loading, parent, false))
    }

    /**
     * binding to views
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RandomImageHolder)
            holder.bind(items[position]!!)
        if (holder is LoadingHolder)
            holder.bind()
    }

    /**
     * loading event
     */
    override fun onLoading() {
        if (onLoadMoreListener != null)
            onLoadMoreListener!!.onLoadMore()
        else
            onLoaded()
    }

    /**
     * attach load more listener
     */
    fun setLoadMore(onLoadMoreListener: OnLoadMoreListener?) {
        this.onLoadMoreListener = onLoadMoreListener
    }
}