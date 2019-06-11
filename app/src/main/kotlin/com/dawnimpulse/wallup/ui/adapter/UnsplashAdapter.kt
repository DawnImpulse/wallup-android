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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.ui.objects.UnsplashImageObject
import com.dawnimpulse.wallup.ui.holders.UnsplashHolder


/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-10 by Saksham
 * @note Updates :
 */
class UnsplashAdapter(
        private val data: MutableList<UnsplashImageObject>,
        recycler: RecyclerView
) : RecyclerView.Adapter<UnsplashHolder>() {

    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private val visibleThreshold = 10
    private var isLoading: Boolean = false
    private var loadMoreListener: OnLoadMoreListener? = null
    private var VIEW_TYPE_LOADING = 0
    private var VIEW_TYPE_ITEM = 1

    init {
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val layoutManager = recyclerView.layoutManager

                // last visible item for staggered grid
                fun getLastVisibleItem(lastVisibleItemPositions: List<Int>): Int {
                    var maxSize = 0
                    lastVisibleItemPositions.forEachIndexed { index, i ->
                        if (index == 0) {
                            maxSize = lastVisibleItemPositions[index]
                        } else if (lastVisibleItemPositions[index] > maxSize) {
                            maxSize = lastVisibleItemPositions[index]
                        }
                    }

                    return maxSize
                }

                totalItemCount = layoutManager!!.itemCount

                if (layoutManager is StaggeredGridLayoutManager)
                    lastVisibleItem = getLastVisibleItem(layoutManager.findLastVisibleItemPositions(null).toList())

                if (layoutManager is LinearLayoutManager)
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    if (loadMoreListener != null) {
                        loadMoreListener!!.onLoadMore()
                    }
                    isLoading = true
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnsplashHolder {
        return UnsplashHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_image, parent, false))
    }

    override fun onBindViewHolder(holder: UnsplashHolder, position: Int) {
        holder.bind(data[position])
    }

    // set load more listener
    fun setOnLoadMoreListener(loadMoreListener: OnLoadMoreListener) {
        this.loadMoreListener = loadMoreListener
    }

    // is loading
    fun setLoaded() {
        isLoading = false
    }


}