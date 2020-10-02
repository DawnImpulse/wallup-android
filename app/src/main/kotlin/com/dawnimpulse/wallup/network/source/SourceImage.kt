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

import com.dawnimpulse.wallup.objects.ObjectImage
import com.dawnimpulse.wallup.utils.reusables.AVAILABLE
import com.dawnimpulse.wallup.utils.reusables.LIMIT
import com.dawnimpulse.wallup.utils.reusables.RestMap
import com.dawnimpulse.wallup.utils.reusables.START
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SourceImage {

    //---------------
    //    RANDOM
    //---------------
    @GET("/images/random")
    fun random(
            @Query(LIMIT) limit: Number,
            @Header("restmap") restmap:String = RestMap.images
    ): Call<List<ObjectImage>>

    //---------------
    //    LATEST
    //---------------
    @GET("/images")
    fun latest(
            @Query(START) start: Number,
            @Query(LIMIT) limit: Number,
            @Query("_sort") sort: String = "createdAt:DESC",
            @Query(AVAILABLE) available: Boolean = true,
            @Header("restmap") restmap:String = RestMap.images
    ): Call<List<ObjectImage>>

    //---------------
    //    DEVICE
    //---------------
    @GET("/images")
    fun device(
            @Query(START) start: Number,
            @Query(LIMIT) limit: Number,
            @Query("device") device: String,
            @Query("_sort") sort: String = "createdAt:DESC",
            @Query(AVAILABLE) available: Boolean = true,
            @Header("restmap") restmap:String = RestMap.images
    ): Call<List<ObjectImage>>
}