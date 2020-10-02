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

import kotlin.properties.Delegates

/**
 * @info - a live observable variable
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-05-05 by Saksham
 * @note Updates :
 */
class Live<T>(private val value1: T) {
    private lateinit var change: (T) -> Unit

    var value: T by Delegates.observable(value1) { _, _, new ->
        if (::change.isInitialized)
            change(new)
    }

    fun onChange(change: (T) -> Unit) {
        this.change = change
    }
}