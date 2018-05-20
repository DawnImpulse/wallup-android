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
package com.dawnimpulse.wallup.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.adapters.MainAdapter
import com.dawnimpulse.wallup.network.RetroApiClient
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.source.RetroUnsplashSource
import com.dawnimpulse.wallup.utils.Config
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author Saksham
 *
 * @note Last Branch Update - recent
 * @note Created on 2018-05-15 by Saksham
 *
 * @note Updates :
 */

class MainFragment : Fragment() {

    /**
     * On create
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    /**
     * On view created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.getLatestPhotos(
                Config.UNSPLASH_API_KEY,
                1,
                10)

        call.enqueue(object : Callback<List<ImagePojo>> {

            override fun onResponse(call: Call<List<ImagePojo>>?, response: Response<List<ImagePojo>>) {
                if (response.isSuccessful){
                    mainRecycler.layoutManager = LinearLayoutManager(context)
                    mainRecycler.adapter = MainAdapter(lifecycle,response.body()!!)
                    Log.d("Test", Gson().toJson(response.body()))
                }
                else{
                    Log.d("Test", response.code().toString())
                    Log.d("Test", Gson().toJson(response.errorBody()))
                }
            }

            override fun onFailure(call: Call<List<ImagePojo>>?, t: Throwable?) {
                Log.e("Test", t.toString())
            }
        })
    }
}
