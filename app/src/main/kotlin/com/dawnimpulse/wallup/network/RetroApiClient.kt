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
package com.dawnimpulse.wallup.network

import com.dawnimpulse.wallup.network.source.UnsplashSource
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2019-06-10 by Saksham
 *
 * @note Updates :
 */
object RetroApiClient {
    private var retrofitUnsplash: UnsplashSource? = null
    private var retrofitPexels: Retrofit? = null
    private var retrofitWallup: Retrofit? = null

    fun getClientUnsplash(): UnsplashSource? {
        if (retrofitUnsplash == null) {
            retrofitUnsplash = Retrofit.Builder()
                    .baseUrl(C.UNSPLASH_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(UnsplashSource::class.java)
        }
        return retrofitUnsplash
    }

    fun getClientPexels(): Retrofit? {
        if (retrofitPexels == null) {
            retrofitPexels = Retrofit.Builder()
                    .baseUrl(C.PEXELS_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofitPexels
    }

    fun getClientWallup(): Retrofit? {
        if (retrofitWallup == null) {
            retrofitWallup = Retrofit.Builder()
                    .baseUrl(Config.WALLUP_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofitWallup
    }
}
