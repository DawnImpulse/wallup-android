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
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.ui.activities.ImageActivity
import com.dawnimpulse.wallup.ui.objects.ImageObject
import com.dawnimpulse.wallup.utils.functions.F
import com.dawnimpulse.wallup.utils.functions.openActivity
import com.dawnimpulse.wallup.utils.functions.toJson
import com.dawnimpulse.wallup.utils.handlers.GradientHandler
import com.dawnimpulse.wallup.utils.handlers.ImageHandler
import com.dawnimpulse.wallup.utils.reusables.TYPE
import com.dawnimpulse.wallup.utils.reusables.WALLUP
import kotlinx.android.synthetic.main.inflator_image.view.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-07 by Saksham
 * @note Updates :
 */
class RandomImageHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val image = view.imageMain!!
    val layout = view.imageL!!
    val context = view.context!!

    // binding items to view
    fun bind(item: ImageObject) {
        val point = F.displayDimensions(context)
        val width = point.x / 2
        val height = F.getDynamicHeight(context, point.x / 2, point.y, item.width, item.height)

        // change layout parameters based on image
        layout.layoutParams = FrameLayout.LayoutParams(width, height)
        image.layoutParams = RelativeLayout.LayoutParams(width - F.dpToPx(4, context), height)

        // set background
        GradientHandler.randomGradientOnBg(image)

        // set images
        ImageHandler.setImageOnStaggered(image, item.links.url)

        // set on click
        layout.setOnClickListener {
            context.openActivity(ImageActivity::class.java) {
                putString(TYPE, WALLUP)
                putString(WALLUP, toJson(item))
            }
        }
    }
}