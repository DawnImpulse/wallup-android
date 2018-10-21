/*
ISC License

Copyright 2018, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/
package com.dawnimpulse.wallup.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.pojo.CollectionPojo
import com.dawnimpulse.wallup.utils.Colors
import com.dawnimpulse.wallup.utils.Config
import com.dawnimpulse.wallup.viewholders.ImageColViewHolder

/**
 * @author Saksham
 * @info - adding image to collection adapter
 *
 * @note Last Branch Update - master
 * @note Created on 2018-10-21 by Saksham
 *
 * @note Updates :
 */
class ImageCollectionAdapter(private val lifecycle: Lifecycle,
                             private val cols: List<CollectionPojo?>,
                             private val imageCols: List<String>?)
    : RecyclerView.Adapter<ImageColViewHolder>() {

    private lateinit var context: Context

    // get total no of items for adapter
    override fun getItemCount(): Int {
        return cols.size
    }

    // create view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageColViewHolder {
        context = parent.context
        return ImageColViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_image_collection, parent, false))
    }

    // binding view holder
    override fun onBindViewHolder(holder: ImageColViewHolder, position: Int) {
        holder.text.text = cols[position]!!.title
        cols[position]!!.cover_photo?.let {
            ImageHandler.setImageInView(lifecycle, holder.image, it.urls!!.full + Config.IMAGE_HEIGHT)
        }
        imageCols?.let {
            for (it in imageCols) {
                if (it == cols[position]!!.id) {
                    holder.bg.setBackgroundColor(Colors(context).GREEN)
                    holder.bg.alpha = 0.6f
                }
            }
        }

    }
}