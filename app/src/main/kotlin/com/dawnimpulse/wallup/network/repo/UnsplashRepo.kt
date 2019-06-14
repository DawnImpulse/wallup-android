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
package com.dawnimpulse.wallup.network.repo

import com.dawnimpulse.wallup.network.RetroApiClient
import com.dawnimpulse.wallup.ui.objects.UnsplashImageObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-10 by Saksham
 * @note Updates :
 */
object UnsplashRepo {
    val client = RetroApiClient.getClientUnsplash()

    // random images
    fun randomImages(callback: (Any?, List<UnsplashImageObject>?) -> Unit) {
        val call = client.randomImages()

        call.enqueue(object : Callback<List<UnsplashImageObject>> {

            override fun onResponse(call: Call<List<UnsplashImageObject>>?, response: Response<List<UnsplashImageObject>>) {
                if (response.isSuccessful)
                    callback(null, response.body()!!)
                else
                    callback(response.errorBody(), null)
            }

            override fun onFailure(call: Call<List<UnsplashImageObject>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // search images
    fun searchImages(query: String, callback: (Any?, List<UnsplashImageObject>?) -> Unit) {
        val call = client.searchImages(query)

        call.enqueue(object : Callback<List<UnsplashImageObject>> {

            override fun onResponse(call: Call<List<UnsplashImageObject>>?, response: Response<List<UnsplashImageObject>>) {
                if (response.isSuccessful)
                    callback(null, response.body()!!)
                else
                    callback(response.errorBody(), null)
            }

            override fun onFailure(call: Call<List<UnsplashImageObject>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }
}