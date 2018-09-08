/*
ISC License

Copyright 2018, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/package com.dawnimpulse.wallup.pojo

import com.google.gson.annotations.SerializedName

/**
 * @author Saksham
 *
 * @note Last Branch Update -
 * @note Created on 2018-09-08 by Saksham
 *
 * @note Updates :
 */
data class CollectionPojo(
        @SerializedName("title") val title: String,
        @SerializedName("description") val description: String? = null,
        @SerializedName("published_at") val published_at: String,
        @SerializedName("updated_at") val updated_at: String,
        @SerializedName("curated") val curated: Boolean,
        @SerializedName("featured") val featured: Boolean,
        @SerializedName("total_photos") val total_photos: Int,
        @SerializedName("tags") val tags: ArrayList<Tag>? = null,
        @SerializedName("cover_photo") val cover_photo: ImagePojo,
        @SerializedName("preview_photos") val preview_photos: ArrayList<PreviewPhotos>?=null,
        @SerializedName("user") val user: UserPojo?=null,
        @SerializedName("links") val links: CollectionLinks

        )

data class Tag(
        @SerializedName("title") val title: String
)

data class PreviewPhotos(
        @SerializedName("urls") val urls: Urls
)

data class CollectionLinks(
        @SerializedName("self") val self: String = "",
        @SerializedName("html") val html: String = "",
        @SerializedName("photos") val photos: String = "",
        @SerializedName("related") val related: String? = null
)