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
package com.dawnimpulse.wallup.network.source

import com.dawnimpulse.wallup.BuildConfig
import com.dawnimpulse.wallup.ui.objects.WallupCollectionList
import com.dawnimpulse.wallup.utils.LIMIT
import com.dawnimpulse.wallup.utils.PAGE
import com.dawnimpulse.wallup.utils.X_API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-10 by Saksham
 * @note Updates :
 */
interface WallupSource {

    // --------------------------
    //      RANDOM COLLECTIONS
    // --------------------------

    @GET("/v1/collections/random")
    fun sortedCollections(
            @Query(PAGE) page: Int,
            @Query(LIMIT) limit: Int = 20,
            @Header(X_API_KEY) apiKey: String = BuildConfig.WALLUP_API_KEY
    ): Call<WallupCollectionList>
}