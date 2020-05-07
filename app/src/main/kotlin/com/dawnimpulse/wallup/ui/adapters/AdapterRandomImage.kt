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

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.objects.ObjectImage
import com.dawnimpulse.wallup.objects.ObjectUnsplashImage
import com.dawnimpulse.wallup.ui.holders.HolderLoading
import com.dawnimpulse.wallup.ui.holders.HolderRandomImage
import com.dawnimpulse.wallup.utils.reusables.Live


/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-03-03 by Saksham
 * @note Updates :
 */
class AdapterRandomImage(
        private val objectImageList: List<Any?>,
        recyclerView: RecyclerView) : CustomAdapter<RecyclerView.ViewHolder>(6, recyclerView) {

    private lateinit var context: Context
    private val VIEW_ITEM = 0
    private val VIEW_LOADING = 1

    /**
     * (default) get items in adapter
     *
     * @return Int
     */
    override fun getItemCount(): Int = objectImageList.size

    /**
     * get type of item
     *
     * @param position
     */
    override fun getItemViewType(position: Int): Int =
            when {
                objectImageList[position] == null -> VIEW_LOADING
                else -> VIEW_ITEM
            }

    /**
     * (default) create view holder for random image / loading
     *
     * @param parent
     * @param viewType
     * @return RecyclerView.ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return when (viewType) {
            VIEW_ITEM -> HolderRandomImage(LayoutInflater.from(context).inflate(R.layout.holder_random_image, parent, false))
            else -> HolderLoading(LayoutInflater.from(context).inflate(R.layout.holder_loading_horizontal, parent, false))
        }
    }

    /**
     * (default) binding view to data
     *
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HolderRandomImage ->{
                if (objectImageList[position]!! is ObjectUnsplashImage)
                    holder.bindUnsplash(objectImageList[position]!! as ObjectUnsplashImage)
                else
                    holder.bindImage(objectImageList[position]!! as ObjectImage)
            }
            else -> (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
        }

    }

}