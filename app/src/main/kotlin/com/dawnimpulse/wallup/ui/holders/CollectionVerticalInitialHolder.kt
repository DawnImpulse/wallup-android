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

import android.graphics.Bitmap
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.objects.CollectionTransferObject
import com.dawnimpulse.wallup.utils.functions.gone
import com.dawnimpulse.wallup.utils.handlers.ImageHandler
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.inflator_vertical_collection_init.view.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-28 by Saksham
 * @note Updates :
 */
class CollectionVerticalInitialHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val image = view.verticalColsBg
    private val description = view.verticalColsDescription
    private val title = view.verticalColsTitle
    private val right = view.verticalColsRight

    private val context = view.context

    /**
     * binding data to views
     */
    fun bind(item: CollectionTransferObject) {

        // set animation
        right.animation = AnimationUtils.loadAnimation(context, R.anim.hover_right)

        // setting image
        ImageHandler.getBitmapImageFullscreen(context, item.image) {
            setBlurZoom(it)
        }

        // setting text
        title.text = item.name

        // making description gone if not available
        if (item.description != null)
            description.text = item.description
        else
            description.gone()
    }

    /**
     * blur the image
     */
    private fun setBlurZoom(bitmap: Bitmap?) {
        (context as AppCompatActivity).runOnUiThread {
            bitmap?.let {
                Blurry.with(context)
                        .async()
                        .sampling(1)
                        .from(bitmap)
                        .into(image)
            }
        }
    }
}