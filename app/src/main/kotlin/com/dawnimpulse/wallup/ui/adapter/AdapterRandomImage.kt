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
package com.dawnimpulse.wallup.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.pojo.PojoImage
import com.dawnimpulse.wallup.ui.holders.HolderRandomImage

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-03-03 by Saksham
 * @note Updates :
 */
class AdapterRandomImage(private val items: List<PojoImage>) : RecyclerView.Adapter<HolderRandomImage>() {
    private lateinit var context: Context

    /**
     * (default) get items in adapter
     *
     * @return Int
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * (default) create view holder for random image
     *
     * @param parent
     * @param viewType
     * @return HolderRandomImage
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderRandomImage {
        context = parent.context
        return HolderRandomImage(LayoutInflater.from(context).inflate(R.layout.holder_random_image, parent, false))
    }

    /**
     * (default) binding view to data
     *
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: HolderRandomImage, position: Int) {
        holder.bind(items[position])
    }

}