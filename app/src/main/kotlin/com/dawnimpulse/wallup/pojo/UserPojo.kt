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
package com.dawnimpulse.wallup.pojo

import com.google.gson.annotations.SerializedName

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-08-31 by Saksham
 *
 * @note Updates :
 */
data class UserPojo(
        @SerializedName("id") val id: String = "",
        @SerializedName("first_name") val first_name: String = "",
        @SerializedName("last_name") val last_name: String? = "",
        @SerializedName("name") val name: String,
        @SerializedName("portfolio_url") val portfolio_url: String? = null,
        @SerializedName("bio") val bio: String? = null,
        @SerializedName("location") val location: String? = null,
        @SerializedName("user_links") val user_links: UserLinks? = null,
        @SerializedName("username") val username: String = "",
        @SerializedName("total_photos") val total_photos: Int = 0,
        @SerializedName("total_collections") val total_collections: Int = 0,
        @SerializedName("total_likes") val total_likes: Int = 0,
        @SerializedName("profile_image") val profile_image: ProfileImage? = null
)

data class UserLinks(
        @SerializedName("html") val html:String = ""
)