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

import com.dawnimpulse.wallup.network.source.SourceDevice
import com.dawnimpulse.wallup.objects.ObjectDevice
import com.dawnimpulse.wallup.utils.handlers.HandlerError
import com.dawnimpulse.wallup.utils.reusables.RetroApiClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object CtrlDevice {
    val client = RetroApiClient.getClient().create(SourceDevice::class.java)

    /**
     * get latest devices
     *
     * @param start
     * @param limit
     */
    suspend fun latest(start: Number, limit: Number) = suspendCoroutine<List<ObjectDevice>> { continuation ->
        val call = client.latest(start, limit)
        call.enqueue(object : Callback<List<ObjectDevice>> {
            override fun onResponse(call: Call<List<ObjectDevice>>, response: Response<List<ObjectDevice>>) {
                if (response.isSuccessful)
                    continuation.resume(response.body()!!)
                else
                    continuation.resumeWithException(Exception(Gson().toJson(HandlerError.parseError(response))))
            }

            // on failure
            override fun onFailure(call: Call<List<ObjectDevice>>, t: Throwable) {
                continuation.resumeWithException(t)
            }
        })
    }
}