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
package com.dawnimpulse.wallup.utils.handlers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.utils.functions.F


/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-07 by Saksham
 * @note Updates :
 */
object ImageHandler {

    // -------------------------
    //     set image in view
    // -------------------------
    fun setImageImgix(view: ImageView, url: String, height: Int = 480) {
        Glide.with(view.context)
                .load("${F.addQuery(url)}fm=webp&h=$height&q=80")
                //.thumbnail(Glide.with(view.context).load("${F.addQuery(url)}fm=webp&h=256&blur=1200"))
                .into(view)
                .clearOnDetach()
    }


    // -------------------------
    //     set image in view
    // -------------------------
    fun getImageImgixBitmapCallback(context: Context, url: String, height: Int, callback: (Bitmap?) -> Unit) {
        Glide.with(context)
                .asBitmap()
                .load("${F.addQuery(url)}fm=webp&h=$height&q=80")
                .listener(object : RequestListener<Bitmap> {

                    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        callback(resource)
                        return true
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        callback(null)
                        return true
                    }
                })
                .submit()
    }

    // ---------------------------
    //     set image fadein
    // ---------------------------
    fun setImageImgixFadein(view: ImageView, url: String, height: Int = 480) {
        Glide.with(view.context)
                .load("${F.addQuery(url)}fm=webp&h=$height&q=80")
                .thumbnail(Glide.with(view.context).load("${F.addQuery(url)}fm=webp&h=256&blur=1200"))
                .transition(GenericTransitionOptions.with(R.anim.fade_in_animation))
                .into(view)
                .clearOnDetach()
    }

    // -----------------------
    //     set image slide
    // -----------------------
    fun setImageImgixSlide(view: ImageView, url: String, height: Int = 480) {
        Glide.with(view.context)
                .load("${F.addQuery(url)}fm=webp&h=$height&q=80")
                .thumbnail(Glide.with(view.context).load("${F.addQuery(url)}fm=webp&h=256&blur=1200"))
                .transition(GenericTransitionOptions.with(R.anim.enter_from_right))
                .into(view)
                .clearOnDetach()
    }

    // -------------------
    //     load image
    // -------------------
    fun cacheImage(context: Context, url: String, height: Int = 480) {
        Glide.with(context)
                .load("${F.addQuery(url)}fm=webp&h=$height&q=80")
                .onlyRetrieveFromCache(true)
                .addListener(object : RequestListener<Drawable> {

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return true
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        Glide.with(context)
                                .load("${F.addQuery(url)}fm=webp&h=$height&q=80")
                                .thumbnail(Glide.with(context).load("${F.addQuery(url)}fm=webp&h=256&q=80"))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .preload()

                        return true
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .preload()

    }
}
