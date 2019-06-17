/*
ISC License

Copyright 2018-2019, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/
package com.dawnimpulse.wallup.utils.functions

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.view.WindowManager
import com.dawnimpulse.wallup.network.repo.WallupRepo
import com.dawnimpulse.wallup.utils.handlers.ImageHandler
import com.dawnimpulse.wallup.utils.reusables.Config
import com.dawnimpulse.wallup.utils.reusables.PEXELS
import com.dawnimpulse.wallup.utils.reusables.UNSPLASH
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


/**
 * @author Saksham
 *
 * @note Last Branch Update - recent
 * @note Created on 2019-06-10 by Saksham
 *
 * @note Updates :
 */
object F {

    // start intent
    fun startWeb(context: Context, string: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(string)))
    }

    // convert dp - px
    fun dpToPx(dp: Int, context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    // convert px - dp
    fun pxToDp(px: Int, context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (px / density).toInt()
    }

    //convert to json
    fun toJson(any: Any): String {
        return Gson().toJson(any)
    }

    // get display height
    fun displayDimensions(context: Context): Point {
        val point = Point()
        val mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = mWindowManager.defaultDisplay
        display.getSize(point) //The point now has display dimens
        return point
    }

    // get height based on screen width
    fun getDynamicHeight(context: Context, screenWidth: Int, screenHeight: Int, width: Int, height: Int): Int {
        val h = ((screenWidth - dpToPx(16, context)) * height) / width

        return if (h > (screenHeight - dpToPx(48, context)))
            screenHeight - dpToPx(48, context)
        else
            h
    }

    // if contains query add & otherwise ?
    fun addQuery(url: String): String {
        return if (url.contains("?"))
            "$url&"
        else
            "$url?"
    }

    // add auto change listener
    // used for scrolling images
    fun publishInterval(): Observable<Int> {
        return Observable.interval(10, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .map { it.toInt() }
    }

    // home images
    fun homeImages(context: Context, callback: (String) -> Unit) {
        if (Config.homeImages.isNotEmpty()) {

            if (Config.homeImages.size > 1) {
                val image = Config.homeImages[1]
                if (image.contains(UNSPLASH) || image.contains(PEXELS))
                    ImageHandler.isImageCached(context, "${addQuery(image)}fm=webp&h=480&q=80") {
                        if (it) {
                            callback(Config.homeImages[1])
                            Config.homeImages.removeAt(0)
                        } else
                            callback(Config.homeImages[0])
                    }
            } else
            // returning image
                callback(Config.homeImages[0])

            // fetching more images
            if (Config.homeImages.size < 5) {
                WallupRepo.editorialImages(30) { e, r ->
                    e?.let {
                        loge(e)
                    }
                    r?.let {
                        Config.homeImages.addAll(it.map { it.urls[0] })
                        callback(Config.homeImages[0])
                        Config.homeImages.removeAt(0)
                    }
                }
            }
        } else {
            // no images in list get some more
            WallupRepo.editorialImages(30) { e, r ->
                e?.let {
                    loge(e)
                    homeImages(context, callback)
                }
                r?.let {
                    Config.homeImages = it.map { it.urls[0] }.toMutableList()
                    callback(Config.homeImages[0])
                }
            }
        }
    }

    // editorial images
    fun editorialImages(context: Context, callback: (String) -> Unit) {
        if (Config.editorialImages.isNotEmpty()) {

            if (Config.editorialImages.size > 1) {
                val image = Config.editorialImages[1]
                if (image.contains(UNSPLASH) || image.contains(PEXELS))
                    ImageHandler.isImageCached(context, "${addQuery(image)}fm=webp&h=480&q=80") {
                        if (it) {
                            callback(Config.editorialImages[1])
                            Config.editorialImages.removeAt(0)
                        } else
                            callback(Config.editorialImages[0])
                    }
            } else
            // returning image
                callback(Config.editorialImages[0])

            // fetching more images
            if (Config.editorialImages.size < 5) {
                WallupRepo.editorialImages(30) { e, r ->
                    e?.let {
                        loge(e)
                    }
                    r?.let {
                        Config.editorialImages.addAll(it.map { it.urls[0] })
                        callback(Config.editorialImages[0])
                        Config.editorialImages.removeAt(0)
                    }
                }
            }
        } else {
            // no images in list get some more
            WallupRepo.editorialImages(30) { e, r ->
                e?.let {
                    loge(e)
                    editorialImages(context, callback)
                }
                r?.let {
                    Config.editorialImages = it.map { it.urls[0] }.toMutableList()
                    callback(Config.editorialImages[0])
                }
            }
        }
    }
}