/**
 * ISC License
 *
 * Copyright 2018-2019, Saksham (DawnImpulse)
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
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.adapter.TagsAdapter
import com.dawnimpulse.wallup.ui.objects.EditorialObject
import com.dawnimpulse.wallup.utils.handlers.ImageHandler
import kotlinx.android.synthetic.main.inflator_editorial.view.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-24 by Saksham
 * @note Updates :
 */
class EditorialHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val bg = view.editorialBg
    private val tags = view.editorialTags
    private val img1 = view.editorialImage1
    private val img2 = view.editorialImage2
    private val img3 = view.editorialImage3
    private val img4 = view.editorialImage4
    private val more = view.editorialMore
    private val below = view.editorialDown
    private val context = view.context
    private lateinit var adapter: TagsAdapter

    fun bind(data: EditorialObject) {
        below.animation = AnimationUtils.loadAnimation(context, R.anim.hover)

        // setting images
        ImageHandler.setImageOnHomescreenBackground(bg, data.image)
        ImageHandler.setImageOnStaggered(img1, data.images[0].links.url)
        ImageHandler.setImageOnStaggered(img2, data.images[1].links.url)
        ImageHandler.setImageOnStaggered(img3, data.images[2].links.url)
        ImageHandler.setImageOnStaggered(img4, data.images[3].links.url)

        // handling recycler view
        if(!::adapter.isInitialized){
            adapter = TagsAdapter(data.tags)
            tags.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            tags.adapter = adapter
        }

        // more images
        more.setOnClickListener {

        }
    }

}