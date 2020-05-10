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
package com.dawnimpulse.wallup.ui.activities

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.objects.ObjectUnsplashImage
import com.dawnimpulse.wallup.utils.handlers.HandlerImage
import com.dawnimpulse.wallup.utils.reusables.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_image.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-05-10 by Saksham
 * @note Updates :
 */
class ActivityUnsplashImage : AppCompatActivity(R.layout.activity_image) {
    private lateinit var unsplashImage: ObjectUnsplashImage

    /**
     * on create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        unsplashImage = Gson().fromJson(intent.extras!!.getString(IMAGE, ""), ObjectUnsplashImage::class.java)
        HandlerImage.fetchImageBitmap(this, unsplashImage.urls.small) {
            it?.let {
                activity_image_image.setImageBitmap(it)
                val color = it.getPalette().vibrant()
                activity_image_back_layout.backgroundTintList = ColorStateList.valueOf(color)
                activity_image_set_wallpaper.setCardBackgroundColor(color)
            }
        }

    }
}