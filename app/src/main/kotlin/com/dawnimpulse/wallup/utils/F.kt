package com.dawnimpulse.wallup.utils

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.dawnimpulse.wallup.BuildConfig
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.models.UnsplashModel
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
 *  Saksham - 2018 10 03 - master - like button
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
    fun startWeb(context: Context, string: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(string)))
    }

    // send mail
    fun sendMail(context: Context) {
        val emailIntent = Intent(android.content.Intent.ACTION_SEND)
        val text = "SDK INT : ${android.os.Build.VERSION.SDK_INT} \n" +
                "MODEL : ${android.os.Build.MODEL} \n"

        emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        emailIntent.type = "vnd.android.cursor.item/email"
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf("dawnimpulse@gmail.com"))
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Wallup Feedback  v${BuildConfig.VERSION_NAME}")
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(emailIntent, "Send mail using..."))
    }

    // like image
    // @param view - view to change
    // @param id - photo id
    // @param color - false for red / true for white
    // @param liked - if image is liked / not
    fun like(context: Context, view: AppCompatImageView, id: String, color: Boolean = false) {
        var model = UnsplashModel((context as AppCompatActivity).lifecycle)
        var like = ContextCompat.getDrawable(context, R.drawable.vd_like)
        var unlike = ContextCompat.getDrawable(context, R.drawable.vd_like_outline)

        if (color) {
            like!!.setColorFilter(Colors(context).WHITE, PorterDuff.Mode.SRC_ATOP)
            unlike!!.setColorFilter(Colors(context).WHITE, PorterDuff.Mode.SRC_ATOP)
        }

        if (view.drawable.constantState == like!!.constantState) {
            view.setImageDrawable(unlike)
            model.unlikePhoto(id)
        } else {
            view.setImageDrawable(like)
            model.likePhoto(id)
        }
    }

    // sort labels
    /*fun sortLabels(labels: List<FirebaseVisionLabel>): List<FirebaseVisionLabel> {
        Collections.sort(labels) { o1, o2 ->
            o2.confidence.compareTo(o1.confidence)
        }
        return labels
    }*/
}