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
package com.dawnimpulse.wallup.utils

import com.dawnimpulse.wallup.network.RetroApiClient
import com.dawnimpulse.wallup.pojo.UnsplashAuthError
import com.dawnimpulse.wallup.pojo.UnsplashError
import retrofit2.Response
import java.io.IOException

/**
 * @author Saksham
 *
 * @note Last Branch Update - hotfixes
 * @note Created on 2018-10-02 by Saksham
 *
 * @note Updates :
 * Saksham - 2019 01 05 - hotfixes - general exception for unsplash auth error
 */
object ErrorUtils {

    // parse unsplash errors
    fun parseError(response: Response<*>): UnsplashError {
        val converter = RetroApiClient.getClient()!!.responseBodyConverter<UnsplashError>(UnsplashError::class.java, arrayOfNulls<Annotation>(0))
        val error: UnsplashError

        try {
            error = converter.convert(response.errorBody())
        } catch (e: IOException) {
            return UnsplashError()
        }

        return error
    }

    // parse unsplash auth error
    fun parseErrorAuth(response: Response<*>): UnsplashAuthError {
        val converter = RetroApiClient.getClient()!!.responseBodyConverter<UnsplashAuthError>(UnsplashAuthError::class.java, arrayOfNulls<Annotation>(0))
        val error: UnsplashAuthError

        try {
            error = converter.convert(response.errorBody())
        } catch (e: Exception) {
            return UnsplashAuthError()
        }

        return error
    }
}