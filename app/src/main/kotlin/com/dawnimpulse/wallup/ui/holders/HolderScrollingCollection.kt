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
package com.dawnimpulse.wallup.ui.holders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.objects.ObjectCollection
import com.dawnimpulse.wallup.ui.adapters.AdapterScrollingCollection
import kotlinx.android.synthetic.main.adapter_scrolling_collection.view.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-04-29 by Saksham
 * @note Updates :
 */
class HolderScrollingCollection(view: View) : RecyclerView.ViewHolder(view) {
    private val recycler = view.adapter_scrolling_collection_recycler
    private lateinit var adapter: AdapterScrollingCollection
    private var context = view.context

    /**
     * bind collections to recycler
     */
    fun bind(collections: List<ObjectCollection>) {
        if (!::adapter.isInitialized) {
            adapter = AdapterScrollingCollection(collections)
            recycler.layoutManager = LinearLayoutManager(context)
            recycler.adapter = adapter
        }
    }
}