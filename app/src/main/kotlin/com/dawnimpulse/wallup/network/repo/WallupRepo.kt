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
import com.dawnimpulse.wallup.ui.objects.HomescreenDetailsObject
import com.dawnimpulse.wallup.ui.objects.HomescreenObject
import com.dawnimpulse.wallup.ui.objects.WallupCollectionList
import com.dawnimpulse.wallup.ui.objects.WallupCollectionObject
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
 */
object WallupRepo {
    private val client = RetroApiClient.getClientWallup()

    // ----------------------------
    //    get sorted collections
    // ----------------------------
    fun getSortedCollections(page: Int, callback: (Any?, List<WallupCollectionObject>?) -> Unit) {

        val call = client.sortedCollections(page)

        call.enqueue(object : Callback<WallupCollectionList> {

            // response
            override fun onResponse(call: Call<WallupCollectionList>, response: Response<WallupCollectionList>) {
                if (response.isSuccessful)
                    callback(null, response.body()!!.details)
                else
                    callback(ErrorWallupUtil.parseError(response), null)
            }

            // on failure
            override fun onFailure(call: Call<WallupCollectionList>, t: Throwable) {
                callback(t.toString(), null)
            }
        })
    }

    // -----------------
    //    homescreen
    // -----------------
    fun homescreen(callback: (Any?, HomescreenObject?) -> Unit) {

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

    // -----------------------
    //    homescreen random
    // -----------------------
    fun homescreenRandom(callback: (Any?, HomescreenObject?) -> Unit) {

        val call = client.homescreenRandom()

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
}