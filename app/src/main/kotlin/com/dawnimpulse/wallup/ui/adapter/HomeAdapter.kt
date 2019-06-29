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
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.holders.*
import com.dawnimpulse.wallup.ui.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.ui.objects.CollectionObject
import com.dawnimpulse.wallup.ui.objects.EditorialObject
import com.dawnimpulse.wallup.ui.objects.ExploreObject

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-17 by Saksham
 * @note Updates :
 *  Saksham - 2019-06-26 - master - explore
 */
class HomeAdapter(val items: List<Any?>, recyclerView: RecyclerView)
    : CustomAdapter(2, recyclerView) {

    private var onLoadMoreListener: OnLoadMoreListener? = null
    private lateinit var context: Context
    private val VIEW_HOME = 0
    private val VIEW_EDITORIAL = 1
    private val VIEW_FEATURED = 2
    private val VIEW_EXPLORE = 3
    private val VIEW_LOADING = 4


    /**
     * no of items
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * type of item
     */
    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Int -> VIEW_HOME
            is EditorialObject -> VIEW_EDITORIAL
            is ExploreObject -> VIEW_EXPLORE
            is CollectionObject -> VIEW_FEATURED
            else -> VIEW_LOADING
        }
    }

    /**
     * creating view holder based on item type
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return when (viewType) {
            VIEW_HOME -> HomeHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_home, parent, false))
            VIEW_EDITORIAL -> EditorialHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_editorial, parent, false))
            VIEW_EXPLORE -> ExploreHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_explore, parent, false))
            VIEW_FEATURED -> FeaturedHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_featured, parent, false))
            else -> LoadingHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_loading_full, parent, false))
        }
    }

    /**
     * binding views
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is HomeHolder -> holder.bind()
            is EditorialHolder -> holder.bind(items[1] as EditorialObject)
            is ExploreHolder -> holder.bind(items[2] as ExploreObject)
            is FeaturedHolder -> holder.bind(items[position] as CollectionObject)
        }

    }

    /**
     * change animation on attach
     */
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {

        when (holder) {
            is HomeHolder -> holder.homeDown.animation = AnimationUtils.loadAnimation(context, R.anim.hover)
            is EditorialHolder -> holder.down.animation = AnimationUtils.loadAnimation(context, R.anim.hover)
            is ExploreHolder -> holder.down.animation = AnimationUtils.loadAnimation(context, R.anim.hover)
        }
    }

    /**
     * if loading started
     */
    override fun onLoading() {
        super.onLoading()
        if (onLoadMoreListener != null)
            onLoadMoreListener!!.onLoadMore()
        else
            onLoaded()
    }

    /**
     * attach load more listener
     */
    fun setOnLoadMoreListener(loadMoreListener: OnLoadMoreListener?) {
        this.onLoadMoreListener = loadMoreListener
    }

}