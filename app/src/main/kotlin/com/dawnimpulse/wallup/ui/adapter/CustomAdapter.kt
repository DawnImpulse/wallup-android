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

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dawnimpulse.wallup.utils.functions.logd
import com.jakewharton.rxbinding3.recyclerview.scrollStateChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-11 by Saksham
 * @note Updates :
 */
open class CustomAdapter(compositeDisposable: CompositeDisposable, recyclerView: RecyclerView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var isLoading = false

    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private val visibleThreshold = 3

    init {

        // scroll state change
        compositeDisposable.add(
                recyclerView.scrollStateChanges()
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .filter { !isLoading }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
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

                            if (totalItemCount <= lastVisibleItem + visibleThreshold) {
                                isLoading = true
                                onLoading()
                            }
                        }, {
                            logd(it)
                        })
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    open fun onLoading() {}

    open fun onLoaded() {
        isLoading = false
    }
}