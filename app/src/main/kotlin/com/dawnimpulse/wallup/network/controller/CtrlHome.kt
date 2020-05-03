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
package com.dawnimpulse.wallup.network.controller

import com.dawnimpulse.wallup.network.source.SourceCollection
import com.dawnimpulse.wallup.network.source.SourceHomescreen
import com.dawnimpulse.wallup.objects.ObjectCollection
import com.dawnimpulse.wallup.objects.ObjectHomescreen
import com.dawnimpulse.wallup.utils.handlers.HandlerError
import com.dawnimpulse.wallup.utils.reusables.RetroApiClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-05-03 by Saksham
 * @note Updates :
 */
object CtrlHome {
    private val client = RetroApiClient.getClient().create(SourceHomescreen::class.java)

    /**
     * get random images
     */
    suspend fun homescreen() = suspendCoroutine<ObjectHomescreen> { continuation ->
        val call = client.homescreen()
        call.enqueue(object : Callback<ObjectHomescreen> {
            override fun onResponse(call: Call<ObjectHomescreen>, response: Response<ObjectHomescreen>) {
                if (response.isSuccessful)
                    continuation.resume(response.body()!!)
                else
                    continuation.resumeWithException(Exception(Gson().toJson(HandlerError.parseError(response))))
            }

            // on failure
            override fun onFailure(call: Call<ObjectHomescreen>, t: Throwable) {
                continuation.resumeWithException(t)
            }
        })
    }
}