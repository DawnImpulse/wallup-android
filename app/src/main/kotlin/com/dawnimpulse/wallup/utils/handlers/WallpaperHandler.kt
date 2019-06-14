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

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import com.dawnimpulse.wallup.utils.functions.F

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-14 by Saksham
 * @note Updates :
 */
object WallpaperHandler {

    // set wallpaper
    fun setWallpaper(context: Context, bitmap: Bitmap) {
        val manager = WallpaperManager.getInstance(context)
        manager.setBitmap(bitmap)
    }

    // get cropped bitmap
    private fun croppedBitmap(context: Context, bitmap: Bitmap) : Bitmap {
        val screenDimens = F.displayDimensions(context)

        // getting required width/height of screen from wallpaper manager
        val reqWidth = screenDimens.x
        val reqHeight = screenDimens.y


        // bitmap width/height
        val bitWidth = bitmap.width
        val bitHeight = bitmap.height

        // get a ratio of required width/height
        val ratioWidth = reqWidth.toFloat().div(1000)
        val ratioHeight = reqHeight.toFloat().div(1000)


        var w = 0f
        var h = 0f
        val w0: Int
        val h0: Int

        // get width & height of cropping rectangle (with ratio maintained)
        while (w + ratioWidth <= bitWidth && h + ratioHeight <= bitHeight) {
            w += ratioWidth
            h += ratioHeight
        }

        // converting to integer
        val ww = w.toInt()
        val hh = h.toInt()

        // starting points of rectangle's width
        w0 = if (ww != bitWidth) {
            val m = bitWidth - ww
            m / 2
        } else
            0

        // starting points of rectangle's height
        h0 = if (hh != bitHeight) {
            val m = bitHeight - hh
            m / 2
        } else
            0

        // crop bitmap
        return Bitmap.createBitmap(bitmap, w0, h0, ww, hh)
    }
}