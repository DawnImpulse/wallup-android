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

import androidx.core.content.ContextCompat
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.App

object Colors {
    val ACCENT = ContextCompat.getColor(App.context, R.color.colorAccent)
    val WHITE = ContextCompat.getColor(App.context, R.color.white)
    val BLACK = ContextCompat.getColor(App.context, R.color.black)
    val PRIMARY = ContextCompat.getColor(App.context, R.color.colorPrimary)
    val TEXT_PRIMARY = ContextCompat.getColor(App.context, R.color.colorTextPrimary)
    val RED = ContextCompat.getColor(App.context, R.color.red)
    val GREY = ContextCompat.getColor(App.context, R.color.grey)
    val GREEN = ContextCompat.getColor(App.context, R.color.green)
}