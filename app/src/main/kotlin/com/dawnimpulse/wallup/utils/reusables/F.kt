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
package com.dawnimpulse.wallup.utils.reusables

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import com.dawnimpulse.wallup.ui.App

/**
 * @info - various utility functions
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-04-15 by Saksham
 * @note Updates :
 */

object F {

    // get display height
    fun displayDimensions(context: Context): Point {
        val point = Point()
        val mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = mWindowManager.defaultDisplay
        display.getSize(point) //The point now has display dimens
        return point
    }

    // convert dp - px
    fun dpToPx(dp: Int, context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    /**
     * get width & height for random image
     */
    fun getWidthHeightRandom(): Pair<Int, Int> {
        val point = displayDimensions(App.context)
        val width = point.x / 2 - dpToPx(8, App.context)
        val height = dpToPx((180..260).random(), App.context)

        return Pair(width, height)
    }

    /**
     * night mode settings
     */
    fun nightMode(): Int {
        if (Prefs.contains(NIGHT_MODE_SYSTEM))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        else {
            if (Prefs.getBoolean(NIGHT_MODE, false))
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        return AppCompatDelegate.getDefaultNightMode()
    }
}