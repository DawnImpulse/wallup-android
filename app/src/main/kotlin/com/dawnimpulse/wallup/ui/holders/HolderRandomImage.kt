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
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.BuildConfig
import com.dawnimpulse.wallup.objects.ObjectImage
import com.dawnimpulse.wallup.objects.ObjectUnsplashImage
import com.dawnimpulse.wallup.utils.handlers.HandlerImage
import com.dawnimpulse.wallup.utils.reusables.F
import kotlinx.android.synthetic.main.holder_random_image.view.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-03-02 by Saksham
 * @note Updates :
 */
class HolderRandomImage(view: View) : RecyclerView.ViewHolder(view) {
    private val layout = view.holder_random_image_layout
    private val image = view.holder_random_image_pic
    private val likeLayout = view.holder_random_image_like_layout
    private val likeIcon = view.holder_random_image_like_icon

    private val context = view.context;

    fun bind(objectImage: ObjectUnsplashImage) {

        // set random image height
        val point = F.displayDimensions(context)
        val width = point.x / 2 - F.dpToPx(8, context)
        val height = F.dpToPx((180..260).random(), context)
        layout.layoutParams = ViewGroup.LayoutParams(width, height)

        val link = objectImage.urls!!.small
        //HandlerImage.setImageInRecycler(image, "${BuildConfig.WALLUP_API_URL}/${link.path}/${link.id}")
        HandlerImage.setImageInRecycler(image, link)
    }
}