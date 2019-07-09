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


/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-07 by Saksham
 * @note Updates :
 *  Saksham - 2019 06 24 - master - handling for cloudvry only
 */
object ImageHandler {


    /**
     * Image in a staggered layout
     * @param view
     * @param url
     */
    fun setImageOnStaggered(view: ImageView, url: String) {
        Glide.with(view.context)
                .load("$url&fm=webp&h=480&q=80")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(GenericTransitionOptions.with(R.anim.fade_in_animation))
                .into(view)
                .clearOnDetach()
    }

    /**
     * Image on homescreen background
     * @param view
     * @param url
     */
    fun setImageOnHomescreenBackground(view: ImageView, url: String) {
        Glide.with(view.context)
                .load("$url&fm=webp&h=720&q=75&bl=5")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(GenericTransitionOptions.with(R.anim.fade_in_animation))
                .into(view)
                .clearOnDetach()
    }

    /**
     * cache homescreen images
     * @param context
     * @param url
     */
    fun cacheHomescreenImage(context: Context, url: String) {
        Glide.with(context)
                .load("$url&fm=webp&h=720&q=75&bl=5")
                .onlyRetrieveFromCache(true)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        return true
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        Glide.with(context)
                                .downloadOnly()
                                .diskCacheStrategy(DiskCacheStrategy.DATA) // Cache resource before it's decoded
                                .load("$url&fm=webp&h=720&q=75&bl=5")
                                .submit(1280, 720)
                                .get()
                        return true
                    }

                })
                .preload()

    }

    /**
     * image on tag view
     * @param view
     * @param url
     */
    fun setImageOnTag(view: ImageView, url: String) {
        Glide.with(view.context)
                .load("$url&fm=webp&h=480&q=80")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(GenericTransitionOptions.with(R.anim.fade_in_animation))
                .into(view)
                .clearOnDetach()
    }

    /**
     * image on vertical collections
     * @param view
     * @param url
     */
    fun setImageOnVerticalCols(view: ImageView, url: String) {
        Glide.with(view.context)
                .load("$url&fm=webp&h=720&q=85")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(GenericTransitionOptions.with(R.anim.fade_in_animation))
                .into(view)
                .clearOnDetach()
    }


    /**
     * get bitmap for fullscreen image
     * @param context
     * @param url
     */
    fun getBitmapImageFullscreen(context: Context, url: String, callback: (Bitmap?) -> Unit) {
        Glide.with(context)
                .asBitmap()
                .load("$url&fm=webp&h=720&q=95")
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


    /**
     * get bitmap for fullscreen image
     * @param context
     * @param url
     */
    fun getBitmapWallpaper(context: Context, url: String, callback: (Bitmap?) -> Unit) {
        Glide.with(context)
                .asBitmap()
                .load("$url&fm=webp")
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
}
