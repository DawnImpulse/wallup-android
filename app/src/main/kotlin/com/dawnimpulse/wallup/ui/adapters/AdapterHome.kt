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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.objects.ObjectScrollingImage
import com.dawnimpulse.wallup.objects.ObjectUnsplashImage
import com.dawnimpulse.wallup.ui.holders.HolderHomeHeader
import com.dawnimpulse.wallup.ui.holders.HolderScrollingImage
import com.dawnimpulse.wallup.utils.reusables.TYPE_HOME_HEADER
import com.dawnimpulse.wallup.utils.reusables.TYPE_SCROLLING_IMAGE

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-04-27 by Saksham
 * @note Updates :
 */
class AdapterHome(private val list: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * get item count
     */
    override fun getItemCount(): Int = list.size

    /**
     * get item type
     */
    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> TYPE_HOME_HEADER
        1 -> TYPE_SCROLLING_IMAGE
        else -> 10
    }

    /**
     * create view holder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                TYPE_HOME_HEADER -> HolderHomeHeader(LayoutInflater.from(parent.context).inflate(R.layout.adapter_home_header, parent, false))
                TYPE_SCROLLING_IMAGE -> HolderScrollingImage(LayoutInflater.from(parent.context).inflate(R.layout.adapter_scrolling_image, parent, false))
                else -> HolderHomeHeader(LayoutInflater.from(parent.context).inflate(R.layout.adapter_home_header, parent, false))
            }

    /**
     * bind view holder
     */
    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HolderScrollingImage)
            holder.bind(list[position] as List<ObjectUnsplashImage>)
    }

}