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
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.objects.ObjectImage
import com.dawnimpulse.wallup.ui.activities.ActivityImage
import com.dawnimpulse.wallup.ui.sheets.SheetId
import com.dawnimpulse.wallup.utils.reusables.*
import kotlinx.android.synthetic.main.holder_image.view.*

class HolderImage(view: View) : RecyclerView.ViewHolder(view) {
    private val layout = view.holder_image_layout
    private val card = view.holder_image_card
    private val image = view.holder_image_image
    private val context = view.context

    /**
     * bind data to view
     */
    fun bind(objImage: ObjectImage) {
        layout.layoutParams = ViewGroup.LayoutParams(layout.layoutParams.width, objImage.height)
        objImage.link.imageTransform(image)
                .height(720)
                .apply()

        // on click
        card.setOnClickListener {
            context.openActivity(ActivityImage::class.java) {
                putString(IMAGE, objImage.toJson())
            }
        }

        // on long click
        card.setOnLongClickListener {
            SheetId(objImage.iid).open((context as AppCompatActivity).supportFragmentManager)
            true
        }
    }
}