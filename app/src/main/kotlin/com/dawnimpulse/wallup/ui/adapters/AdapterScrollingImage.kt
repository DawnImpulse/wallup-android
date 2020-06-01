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
import com.dawnimpulse.wallup.objects.ObjectUnsplashImage

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-04-28 by Saksham
 * @note Updates :
 */
class AdapterScrollingImage(private val images: List<ObjectUnsplashImage>) : RecyclerView.Adapter<HolderScrollingImageItem>() {

    /**
     * get total items
     */
    override fun getItemCount(): Int = images.size

    /**
     * on create view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderScrollingImageItem =
            HolderScrollingImageItem(LayoutInflater.from(parent.context).inflate(R.layout.inflate_scrolling_image, parent, false))


    /**
     * bind view holder
     */
    override fun onBindViewHolder(holder: HolderScrollingImageItem, position: Int) {
        holder.bind(images[position])
    }

}