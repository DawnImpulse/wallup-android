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

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.objects.ObjectCollection
import com.dawnimpulse.wallup.ui.holders.HolderScrollingCollection
import com.dawnimpulse.wallup.ui.holders.HolderScrollingCollectionItem

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-04-30 by Saksham
 * @note Updates :
 */
class AdapterScrollingCollection(private val collections: List<ObjectCollection>) : RecyclerView.Adapter<HolderScrollingCollectionItem>() {

    /**
     * get item count
     */
    override fun getItemCount(): Int = collections.size

    /**
     * create view holder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderScrollingCollectionItem =
            HolderScrollingCollectionItem(LayoutInflater.from(parent.context).inflate(R.layout.inflate_scrolling_collection, parent, false))

    /**
     * bind view holder
     */
    override fun onBindViewHolder(holder: HolderScrollingCollectionItem, position: Int) {
        holder.bind(collections[position])
    }

}