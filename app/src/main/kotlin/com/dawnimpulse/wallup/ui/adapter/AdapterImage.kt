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
import com.dawnimpulse.wallup.ui.OnLoadMoreListener
import com.dawnimpulse.wallup.ui.viewholder.HolderImage
import java.io.File

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-09-02 by Saksham
 * @note Updates :
 */
class AdapterImage(val items: List<File>, recyclerView: RecyclerView) : RecyclerView.Adapter<HolderImage>() {
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private val visibleThreshold = 5
    private var isLoading: Boolean = false
    private var loadMoreListener: OnLoadMoreListener? = null

    private val VIEW_ITEM = 0
    private val VIEW_LOADING = 1

    // initialization for Load More Listener
    init {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!isLoading && loadMoreListener != null) {
                    val layoutManager = recyclerView.layoutManager

                    // last visible item for staggered grid
                    fun getLastVisibleItem(lastVisibleItemPositions: List<Int>): Int {
                        var maxSize = 0
                        lastVisibleItemPositions.forEachIndexed { index, _ ->
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
                        lastVisibleItem =
                                getLastVisibleItem(layoutManager.findLastVisibleItemPositions(null).toList())

                    if (layoutManager is LinearLayoutManager)
                        lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                    if (totalItemCount <= lastVisibleItem + visibleThreshold) {
                        isLoading = true
                        loadMoreListener!!.onLoadMore()
                    }
                }
            }
        })
    }


    // item count
    override fun getItemCount(): Int {
        return items.size
    }

    // init views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderImage {
        return HolderImage(LayoutInflater.from(parent.context).inflate(R.layout.inflator_image, parent, false))
    }

    // bind views
    override fun onBindViewHolder(holder: HolderImage, position: Int) {
        holder.setImage(items[position])
    }

    // set loaded
    fun setLoaded() {
        isLoading = false
    }

    // set load more listener
    fun setLoadMoreListener(loadMoreListener: OnLoadMoreListener) {
        this.loadMoreListener = loadMoreListener
    }

}