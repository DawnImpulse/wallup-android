package com.dawnimpulse.wallup.utils

import android.graphics.Bitmap
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.pojo.UserPojo

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
 * @note Last Branch Update - master
 * @note Created on 2018-05-13 by Saksham
 *
 * @note Updates :
 */
object Config {
    const val UNSPLASH_URL = "https://api.unsplash.com/"
    const val TEMP_IMAGE = "https://images.unsplash.com/profile-1509765804209-a406dae9ac02?dpr=1&auto=format&fit=crop&w=128&h=128&q=60&cs=tinysrgb&crop=faces&bg=fff"

    var UNSPLASH_API_KEY = ""
    var USER_API_KEY = ""
    var UNSPLASH_SECRET = ""
    var HEIGHT = "720"
    var IMAGE_HEIGHT = "&h=$HEIGHT"
    var CONNECTED = true

    lateinit var imageBitmap: Bitmap
    lateinit var userPojo: UserPojo
    var imagePojo: ImagePojo? = null

    // return api key
    fun apiKey(): String {
        return if (USER_API_KEY.isNotEmpty()) USER_API_KEY else UNSPLASH_API_KEY
    }
}