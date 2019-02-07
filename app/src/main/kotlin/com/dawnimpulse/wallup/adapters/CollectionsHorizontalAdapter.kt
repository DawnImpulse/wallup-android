/*
ISC License

Copyright 2018-2019, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/
package com.dawnimpulse.wallup.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.activities.CollectionActivity
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.pojo.CollectionPojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.F
import com.dawnimpulse.wallup.viewholders.CollectionsHorizontalViewHolder
import com.google.gson.Gson

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018 09 14 by Saksham
 *
 * @note Updates :
 */
class CollectionsHorizontalAdapter(private val lifecycle: Lifecycle,
                                   private val cols: List<CollectionPojo?>)
    : RecyclerView.Adapter<CollectionsHorizontalViewHolder>() {

    private val NAME = "CollectionsAdapter"
    private lateinit var context: Context

    override fun getItemCount(): Int {
        return cols.size
    }

    // create view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionsHorizontalViewHolder {
        context = parent.context
        return CollectionsHorizontalViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_collection_horizontal, parent, false))
    }

    // bind view
    override fun onBindViewHolder(holder: CollectionsHorizontalViewHolder, position: Int) {
        cols[position]?.let {
            it.cover_photo?.let {
                ImageHandler.setImageInView(lifecycle, holder.image, it.urls!!.small)
            }
            holder.text.text = F.capWord(cols[position]!!.title)
            holder.image.setOnClickListener {
                var intent = Intent(context, CollectionActivity::class.java)
                intent.putExtra(C.TYPE, C.FEATURED)
                intent.putExtra(C.COLLECTION, Gson().toJson(cols[position]))
                context.startActivity(intent)
            }
        }
    }
}