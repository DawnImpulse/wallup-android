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
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.objects.ObjectCollection
import com.dawnimpulse.wallup.utils.handlers.HandlerImage
import com.dawnimpulse.wallup.utils.reusables.setImage
import kotlinx.android.synthetic.main.inflate_scrolling_collection.view.*

/**
 * @info - scrolling collection item
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-04-30 by Saksham
 * @note Updates :
 */
class HolderScrollingCollectionItem(view: View): RecyclerView.ViewHolder(view){
    private val image = view.inflate_scrolling_collection_image
    private val name = view.inflate_scrolling_collection_name
    private val context = view.context

    /**
     * bind collection item to view
     *
     * @param collection
     */
    fun bind(collection: ObjectCollection){
        name.text = collection.name
        collection.cover.setImage(image)
    }
}