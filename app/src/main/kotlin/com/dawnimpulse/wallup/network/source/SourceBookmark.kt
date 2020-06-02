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
package com.dawnimpulse.wallup.network.source

import com.dawnimpulse.wallup.objects.ObjectBookmark
import com.dawnimpulse.wallup.objects.ObjectSuccess
import com.dawnimpulse.wallup.utils.reusables.LIMIT
import com.dawnimpulse.wallup.utils.reusables.START
import retrofit2.Call
import retrofit2.http.*

interface SourceBookmark {

    //---------------
    //    create
    //---------------
    @POST("/bookmarks")
    @FormUrlEncoded
    fun create(
            @Field("image") image: String,
            @Header("token") token: String
    ): Call<ObjectBookmark>

    //---------------
    //    latest
    //---------------
    @GET("/bookmarks")
    fun latest(
            @Query(START) start: Number,
            @Query(LIMIT) limit: Number,
            @Header("token") token: String,
            @Query("_sort") sort: String = "createdAt:DESC"
    ): Call<List<ObjectBookmark>>

    //---------------
    //    delete
    //---------------
    @GET("/bookmarks/{id}")
    fun delete(
            @Path("id") id: String,
            @Header("token") token: String
    ): Call<ObjectSuccess>
}