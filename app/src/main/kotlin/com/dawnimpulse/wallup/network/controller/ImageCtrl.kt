/**
 * ISC License
 *
 * Copyright 2018-2020, Saksham (DawnImpulse)
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
package com.dawnimpulse.wallup.network.controller

import com.dawnimpulse.wallup.network.source.ImageSource
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.utils.reusables.RetroApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @info - route handling for image ctrl
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-02-29 by Saksham
 * @note Updates :
 */
object ImageCtrl {
    val client = RetroApiClient.getClient().create(ImageSource::class.java)

    /**
     * random function
     *
     * @param limit
     */
    suspend fun random(limit: Number = 30) = suspendCoroutine<List<ImagePojo>> { continuation ->
        val call = client.random(limit)
        call.enqueue(object : Callback<ListQuotes> {
            override fun onResponse(call: Call<ListQuotes>, response: Response<ListQuotes>) {
                if (response.isSuccessful) {
                    continuation.resumeWith(response.body()!!.details)
                } else
                    continuation.resumeWithException()
                    //callback(ErrorHandler.parseError(response), null)
            }

            // on failure
            override fun onFailure(call: Call<ListQuotes>, t: Throwable) {
                continuation.resumeWithException(t)
            }
        })
    }
}