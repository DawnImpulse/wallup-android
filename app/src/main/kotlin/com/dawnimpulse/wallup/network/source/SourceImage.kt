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
import com.dawnimpulse.wallup.utils.reusables.LIMIT
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @info - various routes for images
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-02-29 by Saksham
 * @note Updates :
 */
interface SourceImage {

    @GET("/images/random")
    fun random(
            @Query(LIMIT) limit: Number
    ): Call<List<ObjectImage>>
}