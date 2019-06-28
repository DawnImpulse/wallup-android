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
import com.dawnimpulse.wallup.ui.objects.*
import com.dawnimpulse.wallup.utils.error.ErrorWallupUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-11 by Saksham
 * @note Updates :
 *  Saksham - 2019 06 24 - master - new endpoints
 */
object WallupRepo {
    private val client = RetroApiClient.getClientWallup()


    /**
     * get random images
     *
     * @param callback
     */
    fun getRandomImages(callback: (Any?, List<ImageObject>?) -> Unit) {

        val call = client.randomImages()

        call.enqueue(object : Callback<ImageList> {

            // response
            override fun onResponse(call: Call<ImageList>, response: Response<ImageList>) {
                if (response.isSuccessful)
                    callback(null, response.body()!!.details)
                else
                    callback(ErrorWallupUtil.parseError(response), null)
            }

            // on failure
            override fun onFailure(call: Call<ImageList>, t: Throwable) {
                callback(t.toString(), null)
            }
        })
    }


    /**
     * get homescreen
     *
     * @param callback
     */
    fun getHomescreen(callback: (Any?, HomescreenObject?) -> Unit) {

        val call = client.homescreen()

        call.enqueue(object : Callback<HomescreenDetailsObject> {

            // response
            override fun onResponse(call: Call<HomescreenDetailsObject>, response: Response<HomescreenDetailsObject>) {
                if (response.isSuccessful)
                    callback(null, response.body()!!.details)
                else
                    callback(ErrorWallupUtil.parseError(response), null)
            }

            // on failure
            override fun onFailure(call: Call<HomescreenDetailsObject>, t: Throwable) {
                callback(t.toString(), null)
            }
        })
    }


    /**
     * get homescreen cols
     *
     * @param callback
     */
    fun getHomescreenCols(callback: (Any?, List<CollectionHomescreenObject>?) -> Unit) {

        val call = client.homescreenCols()

        call.enqueue(object : Callback<CollectionHomescreenList> {

            // response
            override fun onResponse(call: Call<CollectionHomescreenList>, response: Response<CollectionHomescreenList>) {
                if (response.isSuccessful)
                    callback(null, response.body()!!.details)
                else
                    callback(ErrorWallupUtil.parseError(response), null)
            }

            // on failure
            override fun onFailure(call: Call<CollectionHomescreenList>, t: Throwable) {
                callback(t.toString(), null)
            }
        })
    }


    /**
     * get sorted cols
     *
     * @param callback
     */
    fun getSortedCols(page: Int, limit: Int, callback: (Any?, List<CollectionObject>?) -> Unit) {

        val call = client.sortedCols(page, limit)

        call.enqueue(object : Callback<CollectionList> {

            // response
            override fun onResponse(call: Call<CollectionList>, response: Response<CollectionList>) {
                if (response.isSuccessful)
                    callback(null, response.body()!!.details)
                else
                    callback(ErrorWallupUtil.parseError(response), null)
            }

            // on failure
            override fun onFailure(call: Call<CollectionList>, t: Throwable) {
                callback(t.toString(), null)
            }
        })
    }

    /**
     * get sorted collection images
     *
     * @param callback
     */
    fun getSortedCollectionImages(cid: String, page: Int, callback: (Any?, List<ImageObject>?) -> Unit) {

        val call = client.sortedCollectionImages(cid, page)

        call.enqueue(object : Callback<ImageList> {

            // response
            override fun onResponse(call: Call<ImageList>, response: Response<ImageList>) {
                if (response.isSuccessful)
                    callback(null, response.body()!!.details)
                else
                    callback(ErrorWallupUtil.parseError(response), null)
            }

            // on failure
            override fun onFailure(call: Call<ImageList>, t: Throwable) {
                callback(t.toString(), null)
            }
        })
    }
}