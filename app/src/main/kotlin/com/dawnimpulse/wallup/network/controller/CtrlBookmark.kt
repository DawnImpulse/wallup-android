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

import com.dawnimpulse.wallup.network.source.SourceBookmark
import com.dawnimpulse.wallup.objects.ObjectBookmark
import com.dawnimpulse.wallup.objects.ObjectSuccess
import com.dawnimpulse.wallup.utils.handlers.HandlerError
import com.dawnimpulse.wallup.utils.reusables.F
import com.dawnimpulse.wallup.utils.reusables.RetroApiClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object CtrlBookmark {
    val client = RetroApiClient.getClient().create(SourceBookmark::class.java)

    /**
     * create bookmark
     *
     * @param image
     */
    suspend fun create(image: String) = suspendCoroutine<ObjectBookmark> { continuation ->
        F.getFirebaseToken {
            if (it != null) {
                val call = client.create(image, it)
                call.enqueue(object : Callback<ObjectBookmark> {
                    override fun onResponse(call: Call<ObjectBookmark>, response: Response<ObjectBookmark>) {
                        if (response.isSuccessful)
                            continuation.resume(response.body()!!)
                        else
                            continuation.resumeWithException(Exception(Gson().toJson(HandlerError.parseError(response))))
                    }

                    // on failure
                    override fun onFailure(call: Call<ObjectBookmark>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                })
            } else
                continuation.resumeWithException(Exception("firebase token not found"))
        }
    }

    /**
     * latest bookmarks
     *
     * @param start
     * @param limit
     */
    suspend fun create(start: Int, limit: Int) = suspendCoroutine<List<ObjectBookmark>> { continuation ->
        F.getFirebaseToken {
            if (it != null) {
                val call = client.latest(start, limit, it)
                call.enqueue(object : Callback<List<ObjectBookmark>> {
                    override fun onResponse(call: Call<List<ObjectBookmark>>, response: Response<List<ObjectBookmark>>) {
                        if (response.isSuccessful)
                            continuation.resume(response.body()!!)
                        else
                            continuation.resumeWithException(Exception(Gson().toJson(HandlerError.parseError(response))))
                    }

                    // on failure
                    override fun onFailure(call: Call<List<ObjectBookmark>>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                })
            } else
                continuation.resumeWithException(Exception("firebase token not found"))
        }
    }

    /**
     * delete bookmark
     *
     * @param id
     */
    suspend fun delete(id: String) = suspendCoroutine<ObjectSuccess> { continuation ->
        F.getFirebaseToken {
            if (it != null) {
                val call = client.delete(id, it)
                call.enqueue(object : Callback<ObjectSuccess> {
                    override fun onResponse(call: Call<ObjectSuccess>, response: Response<ObjectSuccess>) {
                        if (response.isSuccessful)
                            continuation.resume(response.body()!!)
                        else
                            continuation.resumeWithException(Exception(Gson().toJson(HandlerError.parseError(response))))
                    }

                    // on failure
                    override fun onFailure(call: Call<ObjectSuccess>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                })
            } else
                continuation.resumeWithException(Exception("firebase token not found"))
        }
    }
}