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

import android.graphics.Color
import android.view.View
import co.revely.gradient.RevelyGradient

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-07-02 by Saksham
 * @note Updates :
 */
object GradientHandler {

    /**
     * add a random gradient background on view
     */
    fun randomGradientOnBg(view: View) {

        RevelyGradient
                .linear()
                .colors(gradients.random())
                .angle((0..180).random().toFloat())
                .onBackgroundOf(view)
    }

    /**
     * add a random gradient background on view
     */
    fun randomGradientOnBgTransparency(view: View, value: Float) {

        RevelyGradient
                .linear()
                .colors(gradients.random())
                .angle((0..180).random().toFloat())
                .alpha(value)
                .onBackgroundOf(view)
    }

    // different gradients
    var gradients = listOf(
            intArrayOf(Color.parseColor("#2193b0"), Color.parseColor("#6dd5ed")),
            intArrayOf(Color.parseColor("#2980B9"), Color.parseColor("#6DD5FA"), Color.parseColor("#FFFFFF")),
            intArrayOf(Color.parseColor("#1f4037"), Color.parseColor("#99f2c8")),
            intArrayOf(Color.parseColor("#005AA7"), Color.parseColor("#FFFDE4")),
            intArrayOf(Color.parseColor("#DA4453"), Color.parseColor("#89216B")),
            intArrayOf(Color.parseColor("#ad5389"), Color.parseColor("#3c1053")),
            intArrayOf(Color.parseColor("#a8c0ff"), Color.parseColor("#3f2b96")),
            intArrayOf(Color.parseColor("#333333"), Color.parseColor("#dd1818")),
            intArrayOf(Color.parseColor("#bc4e9c"), Color.parseColor("#f80759")),
            intArrayOf(Color.parseColor("#11998e"), Color.parseColor("#38ef7d")),
            intArrayOf(Color.parseColor("#FC5C7D"), Color.parseColor("#6A82FB")),
            intArrayOf(Color.parseColor("#23074d"), Color.parseColor("#cc5333")),
            intArrayOf(Color.parseColor("#CAC531"), Color.parseColor("#F3F9A7")),
            intArrayOf(Color.parseColor("#800080"), Color.parseColor("#ffc0cb")),
            intArrayOf(Color.parseColor("#00F260"), Color.parseColor("#0575E6")),
            intArrayOf(Color.parseColor("#fc4a1a"), Color.parseColor("#f7b733")),
            intArrayOf(Color.parseColor("#74ebd5"), Color.parseColor("#ACB6E5")),
            intArrayOf(Color.parseColor("#6D6027"), Color.parseColor("#D3CBB8")),
            intArrayOf(Color.parseColor("#e1eec3"), Color.parseColor("#f05053")),
            intArrayOf(Color.parseColor("#7F00FF"), Color.parseColor("#E100FF")),
            intArrayOf(Color.parseColor("#06beb6"), Color.parseColor("#48b1bf")),
            intArrayOf(Color.parseColor("#642B73"), Color.parseColor("#C6426E")),
            intArrayOf(Color.parseColor("#1c92d2"), Color.parseColor("#f2fcfe")),
            intArrayOf(Color.parseColor("#36D1DC"), Color.parseColor("#5B86E5")),
            intArrayOf(Color.parseColor("#CB356B"), Color.parseColor("#BD3F32")),
            intArrayOf(Color.parseColor("#283c86"), Color.parseColor("#45a247")),
            intArrayOf(Color.parseColor("#EF3B36"), Color.parseColor("#FFFFFF")),
            intArrayOf(Color.parseColor("#c0392b"), Color.parseColor("#8e44ad")),
            intArrayOf(Color.parseColor("#159957"), Color.parseColor("#155799")),
            intArrayOf(Color.parseColor("#000046"), Color.parseColor("#1CB5E0")),
            intArrayOf(Color.parseColor("#007991"), Color.parseColor("#78ffd6")),
            intArrayOf(Color.parseColor("#56CCF2"), Color.parseColor("#2F80ED")),
            intArrayOf(Color.parseColor("#F2994A"), Color.parseColor("#F2C94C")),
            intArrayOf(Color.parseColor("#EB5757"), Color.parseColor("#000000")),
            intArrayOf(Color.parseColor("#E44D26"), Color.parseColor("#F16529")),
            intArrayOf(Color.parseColor("#4AC29A"), Color.parseColor("#BDFFF3")),
            intArrayOf(Color.parseColor("#D66D75"), Color.parseColor("#E29587")),
            intArrayOf(Color.parseColor("#20002c"), Color.parseColor("#cbb4d4")),
            intArrayOf(Color.parseColor("#C33764"), Color.parseColor("#1D2671")),
            intArrayOf(Color.parseColor("#F7971E"), Color.parseColor("#FFD200")),
            intArrayOf(Color.parseColor("#34e89e"), Color.parseColor("#0f3443")),
            intArrayOf(Color.parseColor("#6190E8"), Color.parseColor("#A7BFE8")),
            intArrayOf(Color.parseColor("#44A08D"), Color.parseColor("#093637")),
            intArrayOf(Color.parseColor("#200122"), Color.parseColor("#6f0000")),
            intArrayOf(Color.parseColor("#0575E6"), Color.parseColor("#021B79")),
            intArrayOf(Color.parseColor("#4568DC"), Color.parseColor("#B06AB3")),
            intArrayOf(Color.parseColor("#43C6AC"), Color.parseColor("#191654")),
            intArrayOf(Color.parseColor("#093028"), Color.parseColor("#237A57")),
            intArrayOf(Color.parseColor("#43C6AC"), Color.parseColor("#F8FFAE")),
            intArrayOf(Color.parseColor("#FFAFBD"), Color.parseColor("#ffc3a0")),
            intArrayOf(Color.parseColor("#F0F2F0"), Color.parseColor("#000C40")),
            intArrayOf(Color.parseColor("#E8CBC0"), Color.parseColor("#636FA4")),
            intArrayOf(Color.parseColor("#DCE35B"), Color.parseColor("#DCE35B")),
            intArrayOf(Color.parseColor("#c0c0aa"), Color.parseColor("#1cefff")),
            intArrayOf(Color.parseColor("#DBE6F6"), Color.parseColor("#C5796D")),
            intArrayOf(Color.parseColor("#3494E6"), Color.parseColor("#EC6EAD")),
            intArrayOf(Color.parseColor("#67B26F"), Color.parseColor("#4ca2cd")),
            intArrayOf(Color.parseColor("#F3904F"), Color.parseColor("#3B4371")),
            intArrayOf(Color.parseColor("#ee0979"), Color.parseColor("#ff6a00")),
            intArrayOf(Color.parseColor("#41295a"), Color.parseColor("#2F0743")),
            intArrayOf(Color.parseColor("#f4c4f3"), Color.parseColor("#fc67fa")),
            intArrayOf(Color.parseColor("#00c3ff"), Color.parseColor("#ffff1c")),
            intArrayOf(Color.parseColor("#ff7e5f"), Color.parseColor("#feb47b")),
            intArrayOf(Color.parseColor("#fffc00"), Color.parseColor("#ffffff")),
            intArrayOf(Color.parseColor("#ff00cc"), Color.parseColor("#333399")),
            intArrayOf(Color.parseColor("#de6161"), Color.parseColor("#2657eb")),
            intArrayOf(Color.parseColor("#ef32d9"), Color.parseColor("#89fffd")),
            intArrayOf(Color.parseColor("#3a6186"), Color.parseColor("#89253e")),
            intArrayOf(Color.parseColor("#4ECDC4"), Color.parseColor("#556270")),
            intArrayOf(Color.parseColor("#7F7FD5"), Color.parseColor("#86A8E7"), Color.parseColor("#91EAE4")),
            intArrayOf(Color.parseColor("#355C7D"), Color.parseColor("#6C5B7B"), Color.parseColor("#C06C84")),
            intArrayOf(Color.parseColor("#A770EF"), Color.parseColor("#CF8BF3"), Color.parseColor("#FDB99B")),
            intArrayOf(Color.parseColor("#9CECFB"), Color.parseColor("#65C7F7"), Color.parseColor("#0052D4"))
    )
}