package com.dawnimpulse.wallup.utils

import android.graphics.Paint
import android.widget.TextView

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
 * @note Created on 2018-05-27 by Saksham
 *
 * @note Updates :
 *  Saksham - 2018 09 02 - master - unsplash referral
 *  Saksham - 2018 09 06 - master - unsplash image referral
 */
object F {

    /**
     * underline a text
     * @param view
     */
    fun underline(view: TextView) {
        view.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }

    /**
     * add suffix to number
     * @param count
     * @return formatted string
     */
    fun withSuffix(count: Int): String {
        if (count < 1000) return "" + count
        val exp = (Math.log(count.toDouble()) / Math.log(1000.0)).toInt()
        return String.format("%.1f %c",
                count / Math.pow(1000.0, exp.toDouble()),
                "kMGTPE"[exp - 1])
    }

    /**
     * create unsplash user referral link
     */
    fun unsplashUser(username: String): String {
        return "https://unsplash.com/@$username${C.UTM}"
    }

    /**
     * create unsplash image referral link
     */
    fun unsplashImage(id: String): String {
        return "https://unsplash.com/photos/$id${C.UTM}"
    }
}