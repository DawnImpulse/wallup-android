/*
ISC License

Copyright 2020, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/
package com.dawnimpulse.wallup.utils.reusables

import com.dawnimpulse.wallup.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2020-02-29 by Saksham
 *
 * @note Updates :
 */
object RetroApiClient {
    private var retrofit: Retrofit? = null
    private var unsplashRetrofit: Retrofit? = null

    /**
     * get client for wallup backend
     *
     * @return Retrofit
     */
    fun getClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.WALLUP_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofit!!
    }

    /**
     * get client for unsplash backend
     *
     * @return Retrofit
     */
    fun getUnsplashClient(): Retrofit {
        if (unsplashRetrofit == null) {
            unsplashRetrofit = Retrofit.Builder()
                    .baseUrl(UNSPLASH_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return unsplashRetrofit!!
    }
}
