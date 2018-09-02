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
package com.dawnimpulse.wallup.repositories

import com.dawnimpulse.wallup.network.RetroApiClient
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.pojo.UserPojo
import com.dawnimpulse.wallup.source.RetroUnsplashSource
import com.dawnimpulse.wallup.utils.Config
import com.dawnimpulse.wallup.utils.L
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-05-20 by Saksham
 *
 * @note Updates :
 *  2018 08 03 - recent - Saksham - downloaded a photo
 *  2018 08 31 - master - Saksham - user details
 *  2018 09 01 - master - Saksham - image details
 *  2018 09 02 - master - Saksham - random user images
 */
object UnsplashRepository {
    private val NAME = "UnsplashRepository"

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

    /**
     * Downloaded a photo
     * @param id
     */
    fun downloadedPhoto(id: String) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.imageDownloaded(
                Config.UNSPLASH_API_KEY,
                id)

        call.enqueue(object : Callback<JSONObject> {

            override fun onResponse(call: Call<JSONObject>?, response: Response<JSONObject>) {
                L.d(NAME, "File downloaded linked")
            }

            override fun onFailure(call: Call<JSONObject>?, t: Throwable?) {
                L.e(NAME, t.toString())
            }
        })
    }

    /**
     * User Details
     */
    fun userDetails(username: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.userDetails(
                Config.UNSPLASH_API_KEY,
                username)

        call.enqueue(object : Callback<UserPojo> {

            override fun onResponse(call: Call<UserPojo>?, response: Response<UserPojo>) {
                callback(null, response.body())
            }

            override fun onFailure(call: Call<UserPojo>?, t: Throwable?) {
                callback(t.toString(), null)
            }
        })
    }

    /**
     * Get user photos
     * @param page
     * @param count
     * @param username
     * @param callback
     */
    fun userPhotos(page: Int, count: Int, username: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.userPhotos(
                Config.UNSPLASH_API_KEY,
                username,
                page,
                count)

        call.enqueue(object : Callback<List<ImagePojo>> {

            override fun onResponse(call: Call<List<ImagePojo>>?, response: Response<List<ImagePojo>>) {
                if (response.isSuccessful) {
                    callback(null, response.body())
                } else {
                    callback(Gson().toJson(response.errorBody()).toString(), null)
                }
            }

            override fun onFailure(call: Call<List<ImagePojo>>?, t: Throwable?) {
                callback(t.toString(), null)
            }
        })
    }

    /**
     * Get image details
     * @param id
     * @param callback
     */
    fun getImage(id: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.getImage(
                Config.UNSPLASH_API_KEY,
                id
        )

        call.enqueue(object : Callback<ImagePojo> {

            override fun onResponse(call: Call<ImagePojo>?, response: Response<ImagePojo>) {
                if (response.isSuccessful) {
                    callback(null, response.body())
                } else {
                    callback(Gson().toJson(response.errorBody()).toString(), null)
                }
            }

            override fun onFailure(call: Call<ImagePojo>?, t: Throwable?) {
                callback(t.toString(), null)
            }
        })
    }

    /**
     * Get random images
     * @param callback
     */
    fun randomImages(callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.randomImages(
                Config.UNSPLASH_API_KEY
        )

        call.enqueue(object : Callback<List<ImagePojo>> {

            override fun onResponse(call: Call<List<ImagePojo>>?, response: Response<List<ImagePojo>>) {
                if (response.isSuccessful) {
                    callback(null, response.body())
                } else {
                    callback(Gson().toJson(response.errorBody()).toString(), null)
                }
            }

            override fun onFailure(call: Call<List<ImagePojo>>?, t: Throwable?) {
                callback(t.toString(), null)
            }
        })
    }

    /**
     * Get random images
     * @param callback
     */
    fun randomUserImages(username: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.randomUserImages(
                Config.UNSPLASH_API_KEY,
                username
        )

        call.enqueue(object : Callback<List<ImagePojo>> {

            override fun onResponse(call: Call<List<ImagePojo>>?, response: Response<List<ImagePojo>>) {
                if (response.isSuccessful) {
                    callback(null, response.body())
                } else {
                    callback(Gson().toJson(response.errorBody()).toString(), null)
                }
            }

            override fun onFailure(call: Call<List<ImagePojo>>?, t: Throwable?) {
                callback(t.toString(), null)
            }
        })
    }

}