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
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.holders.HomeHolder
import com.dawnimpulse.wallup.ui.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.utils.reusables.Config
import kotlinx.android.synthetic.main.inflator_test.view.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-17 by Saksham
 * @note Updates :
 */
class HomeAdapter(val items: List<Any>, recyclerView: RecyclerView)
    : CustomAdapter(Config.disposableHomescreenActivity, recyclerView) {

    private lateinit var onLoadMoreListener: OnLoadMoreListener
    private val VIEW_COL = 0
    private val VIEW_HOME = 1
    private val VIEW_EXPLORE = 2


    // --------------
    //    count
    // --------------
    override fun getItemCount(): Int {
        return items.size
    }

    // --------------
    //    type
    // --------------
    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> VIEW_HOME
            1 -> VIEW_EXPLORE
            else -> VIEW_COL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_HOME -> HomeHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_home, parent, false))
            VIEW_EXPLORE -> abc(LayoutInflater.from(parent.context).inflate(R.layout.inflator_test, parent, false))
            else -> abc(LayoutInflater.from(parent.context).inflate(R.layout.inflator_test, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HomeHolder)
            holder.bind()
    }

    override fun onLoading() {
        super.onLoading()
        onLoadMoreListener.onLoadMore()
    }

    fun setOnLoadMoreListener(loadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = loadMoreListener
    }

}

class abc(val view: View) : RecyclerView.ViewHolder(view) {
    val text = view.demo
    val rootDemo = view.rootDemo

    fun bind() {

    }
}