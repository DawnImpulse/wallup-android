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
package com.dawnimpulse.wallup.utils

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import com.dawnimpulse.wallup.R


/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-01 by Saksham
 *
 * @note Updates :
 */
object ColorModifier{

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
     * Check whether color is Not Dark
     *
     * @param color - Input Color
     * @return - true / false
     */
    private fun isColorNonDark(color: Int): Boolean {
        //getting red color intensity
        val red = Color.red(color)
        //getting blue color intensity
        val blue = Color.blue(color)
        //getting green color intensity
        val green = Color.green(color)

        // Check whether the color lies in scale favour to contrast WHITE or BLACK
        return red * 0.299 + green * 0.587 + blue * 0.114 > 80 && red * 0.299 + green * 0.587 + blue * 0.114 < 220
    }
}