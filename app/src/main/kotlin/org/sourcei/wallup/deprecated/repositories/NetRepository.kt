/*
ISC License

Copyright 2018-2019, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/
package org.sourcei.wallup.deprecated.repositories

import com.google.gson.Gson
import org.sourcei.wallup.deprecated.utils.RemoteConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-26 by Saksham
 *
 * @note Updates :
 */
object NetRepository {

    // post bug / feature to slack
    fun postSlack(type: String, data: org.sourcei.wallup.deprecated.pojo.SlackPojo, callback: (Any?, Any?) -> Unit) {
        val apiClient = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.create(org.sourcei.wallup.deprecated.source.RetroUnsplashSource::class.java)
        var url = RemoteConfig.getFeatureUrl()

        if (type == org.sourcei.wallup.deprecated.utils.C.BUG)
            RemoteConfig.getBugUrl()

        val call = apiClient.postSlack(url, data)

        call.enqueue(object : Callback<Any> {

            override fun onResponse(call: Call<Any>?, response: Response<Any>) {
                if (response.isSuccessful) {
                    callback(null, true)
                } else {
                    callback(Gson().toJson(response.errorBody()).toString(), null)
                }
            }

            override fun onFailure(call: Call<Any>?, t: Throwable?) {
                callback(t.toString(), null)
            }
        })
    }
}