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

import com.dawnimpulse.wallup.network.source.SourceImage
import com.dawnimpulse.wallup.network.source.SourceUnsplash
import com.dawnimpulse.wallup.objects.ObjectImage
import com.dawnimpulse.wallup.objects.ObjectUnsplashImage
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
 * @info - unsplash controller
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-04-20 by Saksham
 * @note Updates :
 */
object CtrlUnsplash{
    private val client = RetroApiClient.getUnsplashClient().create(SourceUnsplash::class.java)

    /**
     * get random images
     *
     */
    suspend fun randomImages()  = suspendCoroutine<List<ObjectUnsplashImage>> { continuation ->
        val call = client.randomImages()
        call.enqueue(object : Callback<List<ObjectUnsplashImage>> {
            override fun onResponse(call: Call<List<ObjectUnsplashImage>>, response: Response<List<ObjectUnsplashImage>>) {
                if (response.isSuccessful)
                    continuation.resume(response.body()!!)
                else
                    continuation.resumeWithException(Exception(Gson().toJson(HandlerError.parseError(response))))
            }

            // on failure
            override fun onFailure(call: Call<List<ObjectUnsplashImage>>, t: Throwable) {
                continuation.resumeWithException(t)
            }
        })
    }

        /*call.enqueue(object : Callback<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>> {

            override fun onResponse(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, response: Response<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(org.sourcei.wallup.deprecated.utils.ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })*/
}