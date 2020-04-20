package com.dawnimpulse.wallup.network.source

import com.dawnimpulse.wallup.BuildConfig
import com.dawnimpulse.wallup.objects.ObjectUnsplashImage
import com.dawnimpulse.wallup.utils.reusables.AUTHORIZATION
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-04-20 by Saksham
 * @note Updates :
 */
interface SourceUnsplash {

    @GET("/images/random")
    fun randomImages(
        @Header(AUTHORIZATION) authorization: String = "Client-ID ${BuildConfig.UNSPLASH_ACCESS_KEY}"
    ): Call<List<ObjectUnsplashImage>>
}