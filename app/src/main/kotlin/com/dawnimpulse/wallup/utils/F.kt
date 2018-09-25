package com.dawnimpulse.wallup.utils

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.TextView
import java.text.SimpleDateFormat


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
 *  Saksham - 2018 09 08 - master - first word string + date convert
 *  Saksham - 2018 09 20 - master - sort Firebase Image Labels
 */
object F {
    private val NAME = "F"

    // underline a text
    fun underline(view: TextView) {
        view.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }

    // add suffix to number
    fun withSuffix(count: Int): String {
        if (count < 1000) return "" + count
        val exp = (Math.log(count.toDouble()) / Math.log(1000.0)).toInt()
        return String.format("%.1f %c",
                count / Math.pow(1000.0, exp.toDouble()),
                "kMGTPE"[exp - 1])
    }

    // create unsplash user referral link
    fun unsplashUser(username: String): String {
        return "https://unsplash.com/@$username${C.UTM}"
    }

    // create unsplash image referral link
    fun unsplashImage(id: String): String {
        return "https://unsplash.com/photos/$id${C.UTM}"
    }

    //pick first word from string
    fun firstWord(string: String): String {
        var chars = string.toCharArray()
        for (ch in chars.indices) {
            if (chars[ch].toString() == " ") {
                return string.substring(0, ch)
            }
        }
        return string
    }

    // change date format
    fun dateConvert(date: String): String {
        var sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        var sdfN = SimpleDateFormat("dd MMMM '`'yy")
        return sdfN.format(sdf.parse(date))
    }

    // for html string
    @Suppress("DEPRECATION")
    fun fromHtml(source: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(source)
        }
    }

    // capital letter word
    fun capWord(string: String): String {
        return if (string.isNotEmpty()) {
            val result = StringBuilder(string.length)
            val words = string.split("\\ ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in words.indices) {
                if (words[i].isNotEmpty())
                    result.append(Character.toUpperCase(words[i][0])).append(words[i].substring(1)).append(" ")
            }
            result.toString()
        } else
            string
    }

    // start intent
    fun startWeb(context: Context,string: String){
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(string)))
    }


    // sort labels
    /*fun sortLabels(labels: List<FirebaseVisionLabel>): List<FirebaseVisionLabel> {
        Collections.sort(labels) { o1, o2 ->
            o2.confidence.compareTo(o1.confidence)
        }
        return labels
    }*/
}