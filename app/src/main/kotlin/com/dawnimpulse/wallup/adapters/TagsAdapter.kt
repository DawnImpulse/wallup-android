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

import android.arch.lifecycle.Lifecycle
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.viewholders.TagsHolder
import com.google.firebase.ml.vision.label.FirebaseVisionLabel

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-20 by Saksham
 *
 * @note Updates :
 */
class TagsAdapter(private val lifecycle: Lifecycle, private val tags: List<FirebaseVisionLabel?>)
    : RecyclerView.Adapter<TagsHolder>() {

    private lateinit var context: Context

    // get total no of items for adapter
    override fun getItemCount(): Int {
        return tags.size
    }

    // create view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsHolder {
        context = parent.context
        return TagsHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_tags, parent, false))
    }

    // binding view holder
    override fun onBindViewHolder(holder: TagsHolder, position: Int) {
        holder.label.text = tags[position]!!.label
        ImageHandler.setImageInView(lifecycle,holder.image,"${C.TAGS_SOURCE}${tags[position]!!.label}")
    }
}