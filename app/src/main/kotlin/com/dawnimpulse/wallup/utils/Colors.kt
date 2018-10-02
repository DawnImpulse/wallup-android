/*
ISC License

Copyright 2018, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/package com.dawnimpulse.wallup.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.dawnimpulse.wallup.R

/**
 * @author Saksham
 *
 * @note Last Branch Update -
 * @note Created on 2018-09-07 by Saksham
 *
 * @note Updates :
 */
class Colors(context: Context){
    val TRANSPARENT = ContextCompat.getColor(context,android.R.color.transparent)
    val BLACK = ContextCompat.getColor(context, R.color.black)
    val WHITE = ContextCompat.getColor(context, R.color.white)
    val GREY_300 = ContextCompat.getColor(context, R.color.grey300)
    val GREY_400 = ContextCompat.getColor(context, R.color.grey400)
    val GREY_500 = ContextCompat.getColor(context, R.color.grey500)
}