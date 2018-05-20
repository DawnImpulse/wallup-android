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
package com.dawnimpulse.wallup.respositories

import com.dawnimpulse.wallup.network.RetroApiClient
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.source.RetroUnsplashSource
import com.dawnimpulse.wallup.utils.Config
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author Saksham
 *
 * @note Last Branch Update -
 * @note Created on 2018-05-20 by Saksham
 *
 * @note Updates :
 */
object UnsplashRepository {

    /**
     * Get latest photos
     * @param page
     * @param callback
     */
    fun getLatestPhotos(page: Int, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.getLatestPhotos(
                Config.UNSPLASH_API_KEY,
                page)

        call.enqueue(object : Callback<List<ImagePojo>> {

            override fun onResponse(call: Call<List<ImagePojo>>?, response: Response<List<ImagePojo>>) {
                if (response.isSuccessful) {
                    callback(null, response.body())
                } else
                    callback(response.errorBody().toString(), null)
            }

            override fun onFailure(call: Call<List<ImagePojo>>?, t: Throwable?) {
                callback(t.toString(), null)
            }
        })
    }

    /**
     * Get popular photos
     * @param page
     * @param callback
     */
    fun getPopularPhotos(page: Int, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.getPopularPhotos(
                Config.UNSPLASH_API_KEY,
                page)

        call.enqueue(object : Callback<List<ImagePojo>> {

            override fun onResponse(call: Call<List<ImagePojo>>?, response: Response<List<ImagePojo>>) {
                if (response.isSuccessful) {
                    callback(null, response.body())
                } else
                    callback(response.errorBody().toString(), null)
            }

            override fun onFailure(call: Call<List<ImagePojo>>?, t: Throwable?) {
                callback(t.toString(), null)
            }
        })
    }

    /**
     * Get curated photos
     * @param page
     * @param callback
     */
    fun getCuratedPhotos(page: Int, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.getCuratedPhotos(
                Config.UNSPLASH_API_KEY,
                page)

        call.enqueue(object : Callback<List<ImagePojo>> {

            override fun onResponse(call: Call<List<ImagePojo>>?, response: Response<List<ImagePojo>>) {
                if (response.isSuccessful) {
                    callback(null, response.body())
                } else
                    callback(response.errorBody().toString(), null)
            }

            override fun onFailure(call: Call<List<ImagePojo>>?, t: Throwable?) {
                callback(t.toString(), null)
            }
        })
    }
}