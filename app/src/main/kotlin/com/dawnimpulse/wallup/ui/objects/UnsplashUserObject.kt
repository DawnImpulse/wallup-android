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
package com.dawnimpulse.wallup.ui.objects

import com.dawnimpulse.wallup.utils.functions.jsonOf
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-10 by Saksham
 * @note Updates :
 */
data class UnsplashUserObject(
        @SerializedName("name") val name: String = "",
        @SerializedName("username") val username: String = "",
        @SerializedName("profile_image") val profile_image: UnsplashUserProfileImage = Gson().fromJson(jsonOf(Pair("large", "")).toString(), UnsplashUserProfileImage::class.java),
        @SerializedName("links") val links: UnsplashUserProfileLinks
)

data class UnsplashUserProfileImage(
        @SerializedName("large") val large: String = ""
)

data class UnsplashUserProfileLinks(
        @SerializedName("html") val html: String
)