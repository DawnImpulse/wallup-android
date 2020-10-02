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
package com.dawnimpulse.wallup.utils.handlers

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.utils.reusables.Colors
import com.dawnimpulse.wallup.utils.reusables.getNonDarkColor

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-05-10 by Saksham
 * @note Updates :
 */
object HandlerColor {
    lateinit var context: Context

    /**
     * Get a non Dark from the palette
     *
     * @param mPalette - The input palette
     * @param mContext - Context
     * @return - The required non Dark color
     */
    fun getNonDarkColor(mPalette: Palette, mContext: Context): Int {
        //the color variable we need to return
        var color: Int = mPalette.getVibrantColor(ContextCompat.getColor(mContext, R.color.black))
        //variable to store whether color is darker or not
        var colorNonDark: Boolean

        //get the contrast color of Vibrant Color
        colorNonDark = isColorNonDark(color)
        //If contrast color is not white i.e black then return it
        if (colorNonDark)
            return color

        //get the contrast color of Dominant Color
        color = mPalette.getDominantColor(ContextCompat.getColor(mContext, R.color.black))
        colorNonDark = isColorNonDark(color)
        //If contrast color is not white i.e black then return it
        if (colorNonDark)
            return color

        //get the contrast color of Light Vibrant Color
        color = mPalette.getLightVibrantColor(ContextCompat.getColor(mContext, R.color.black))
        colorNonDark = isColorNonDark(color)
        //If contrast color is not white i.e black then return it
        if (colorNonDark)
            return color

        //get the contrast color of Light Vibrant Color
        color = mPalette.getMutedColor(ContextCompat.getColor(mContext, R.color.black))
        colorNonDark = isColorNonDark(color)
        //If contrast color is not white i.e black then return it
        if (colorNonDark)
            return color

        //get the contrast color of Light Vibrant Color
        color = mPalette.getLightMutedColor(ContextCompat.getColor(mContext, R.color.black))
        colorNonDark = isColorNonDark(color)
        //If contrast color is not white i.e black then return it
        return if (colorNonDark) color else ContextCompat.getColor(mContext, R.color.colorAccent)
    }

    /**
     * get vibrant color
     *
     * @param mPalette
     */
    fun getVibrant(mPalette: Palette) : Int{
        return mPalette.getVibrantColor(mPalette.getDominantColor(mPalette.getMutedColor(mPalette.getNonDarkColor())))
    }

    /**
     * Check whether color is Not Dark
     *
     * @param color - Input Color
     * @return - true / false
     */
    fun isColorNonDark(color: Int): Boolean {
        return getContrastColor(color) == Colors.BLACK
    }

    // ge contrasting white or black for a given color
    fun getContrastColor(color: Int): Int {
        val y = ((299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000).toDouble()
        return if (y >= 128) Color.BLACK else Color.WHITE
    }
}