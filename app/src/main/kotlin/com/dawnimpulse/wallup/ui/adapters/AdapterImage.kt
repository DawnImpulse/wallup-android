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
import com.dawnimpulse.wallup.objects.ObjectImage
import com.dawnimpulse.wallup.ui.holders.HolderImage

class AdapterImage(private val images:List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val IMAGE = 0
    private val LOADING = 1
    private val RELOAD = 2
    private val UNAVAILABLE = 3

    /**
     * get total items
     */
    override fun getItemCount(): Int  = images.size

    /**
     * get item type
     */
    override fun getItemViewType(position: Int): Int =
            when(images[position]){
                is ObjectImage -> IMAGE
                else -> IMAGE
            }

    /**
     * create view holder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when(viewType){
                IMAGE -> HolderImage(LayoutInflater.from(parent.context).inflate(R.layout.adapter_image, parent, false))
                else -> HolderImage(LayoutInflater.from(parent.context).inflate(R.layout.adapter_image, parent, false))
            }

    /**
     * bind view holder
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is HolderImage -> holder.bind(images[position] as ObjectImage)
        }
    }

}