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
package com.dawnimpulse.wallup.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.dawnimpulse.wallup.R

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2018-12-03 by Saksham
 * @note Updates :
 */
class Drawables(context: Context){
    val latest = ContextCompat.getDrawable(context, R.drawable.vd_latest_accent)
    val latest_outline = ContextCompat.getDrawable(context, R.drawable.vd_latest_outline)
    val shuffle = ContextCompat.getDrawable(context, R.drawable.vd_shuffle)
    val shuffle1 = ContextCompat.getDrawable(context, R.drawable.vd_shuffle_1)
    val shuffle2 = ContextCompat.getDrawable(context, R.drawable.vd_shuffle_2_accent)
    val shuffle_outline = ContextCompat.getDrawable(context, R.drawable.vd_shuffle_outline)
}