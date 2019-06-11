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
        val point = displayDimensions(context)
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
}