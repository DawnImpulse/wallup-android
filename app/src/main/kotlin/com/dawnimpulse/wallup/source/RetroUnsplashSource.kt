package com.dawnimpulse.wallup.source

import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.pojo.UserPojo
import com.dawnimpulse.wallup.utils.C
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

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
 *  Saksham - 2018 05 20 - recent - curated photos
 */
interface RetroUnsplashSource {


    // get latest photos

    @GET("/photos?per_page=30")
    fun getLatestPhotos(
            @Header(C.AUTHORIZATION) authorization: String,
            @Query(C.PAGE) page: Int
    ): Call<List<ImagePojo>>

    //get popular photos

    @GET("/photos?order_by=popular&per_page=30")
    fun getPopularPhotos(
            @Header(C.AUTHORIZATION) authorization: String,
            @Query(C.PAGE) page: Int
    ): Call<List<ImagePojo>>

    // get curated photos

    @GET("/photos/curated?per_page=30")
    fun getCuratedPhotos(
            @Header(C.AUTHORIZATION) authorization: String,
            @Query(C.PAGE) page: Int
    ): Call<List<ImagePojo>>

    // download image call

    @GET("/photos/{id}/download")
    fun imageDownloaded(
            @Header(C.AUTHORIZATION) authorization: String,
            @Path("id") id: String
    ): Call<JSONObject>

    // user details

    @GET("/users/{username}")
    fun userDetails(
            @Header(C.AUTHORIZATION) authorization: String,
            @Path("username") username: String
    ): Call<UserPojo>
}