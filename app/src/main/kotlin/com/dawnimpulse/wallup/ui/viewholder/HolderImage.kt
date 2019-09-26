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
package com.dawnimpulse.wallup.ui.viewholder

import android.graphics.BitmapFactory
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.ui.activities.ImageActivity
import com.dawnimpulse.wallup.utils.functions.F
import com.dawnimpulse.wallup.utils.functions.openActivity
import com.dawnimpulse.wallup.utils.reusables.IMAGE
import kotlinx.android.synthetic.main.inflator_image.view.*
import java.io.File

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-09-02 by Saksham
 * @note Updates :
 */
class HolderImage(view: View) : RecyclerView.ViewHolder(view) {
    private val image = view.inflatorImage
    private val layout = view.imageLayout
    private val context = view.context

    // get bitmap from file & display
    fun setImage(file: File) {
        val bitmap = BitmapFactory.decodeFile(file.path)

        bitmap?.let {

            // calculating dynamic height
            val point = F.displayDimensions(context)
            val width = point.x / 2
            val height = F.getDynamicHeight(context, point.x / 2, point.y, bitmap.width, bitmap.height)

            // change layout parameters based on image
            layout.layoutParams = FrameLayout.LayoutParams(width - F.dpToPx(4, context), height)
            image.layoutParams = FrameLayout.LayoutParams(width - F.dpToPx(8, context), height)

            // set bitmap
            image.setImageBitmap(it)

            // handle image click
            image.setOnClickListener {
                context.openActivity(ImageActivity::class.java){
                    putString(IMAGE, file.path)
                }
            }
        }
    }
}