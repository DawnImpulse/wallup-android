/**
 * ISC License
 *
 * Copyright 2020, Saksham (DawnImpulse)
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
package com.dawnimpulse.wallup.ui.adapters

import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

open class CustomAdapter<T : RecyclerView.ViewHolder>(recyclerView: RecyclerView, val visibleThreshold: Int = 2) : RecyclerView.Adapter<T>() {
    var isLoading = false
    val liveData = MutableLiveData<Void>()

    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0

    init {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                if (!isLoading) {
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
                        lastVisibleItem = getLastVisibleItem(layoutManager.findLastVisibleItemPositions(null).toList())

                    if (layoutManager is LinearLayoutManager)
                        lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                    if (totalItemCount <= lastVisibleItem + visibleThreshold) {
                        isLoading = true
                        liveData.postValue(null)
                    }
                }
            }
        })
    }

    /**
     * handling inside extending class
     *
     * @param parent
     * @param viewType
     * @return T
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
        TODO("Not yet implemented")
    }

    /**
     * handling inside extending class
     *
     * @return Int
     */
    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * handling inside extending class
     *
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: T, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * when its time to load new data
     *
     * @return LiveData<Void>
     */
    open fun onLoading(): LiveData<Void> {
        return liveData
    }

    /**
     * data is loaded , set variable false
     */
    open fun onLoaded() {
        isLoading = false
    }
}
