package com.dawnimpulse.wallup.handlers

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget


/*
ISC License

Copyright 2018, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/

/**
 * @author Saksham
 *
 * @note Last Branch Update - recent
 * @note Created on 2018-05-15 by Saksham
 *
 * @note Updates :
 *  Saksham - 2018 05 25 - recent - adding bitmap callback
 */
object ImageHandler {

    /**
     * Use to set image on a view using glide
     * @param lifecycle
     * @param view
     * @param url
     */
    fun setImageInView(lifecycle: Lifecycle, view: ImageView, url: String) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            Glide.with(view.context)
                    .load(url)
                    .into(view)
        } else {
            lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_START)
                fun onStart() {
                    Glide.with(view.context)
                            .load(url)
                            .into(view)
                }
            })
        }
    }

    /**
     * get image as bitmap from url
     * @param lifecycle
     * @param context
     * @param url
     */
    fun getImageAsBitmap(lifecycle: Lifecycle, context: Context, url: String, callback: (bitmap: Bitmap) -> Unit) {
        lifecycle.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                Glide.with(context)
                        .load(url)
                        .asBitmap()
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                                resource.let {
                                    callback(it!!)
                                }
                            }
                        })
            }

        })
    }
}