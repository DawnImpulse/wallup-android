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
package org.sourcei.wallup.deprecated.utils

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
    fun parseError(response: Response<*>): org.sourcei.wallup.deprecated.pojo.UnsplashError {
        val converter = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.responseBodyConverter<org.sourcei.wallup.deprecated.pojo.UnsplashError>(org.sourcei.wallup.deprecated.pojo.UnsplashError::class.java, arrayOfNulls<Annotation>(0))
        val error: org.sourcei.wallup.deprecated.pojo.UnsplashError

        try {
            error = converter.convert(response.errorBody())
        } catch (e: IOException) {
            return org.sourcei.wallup.deprecated.pojo.UnsplashError()
        }

        return error
    }

    // parse unsplash auth error
    fun parseErrorAuth(response: Response<*>): org.sourcei.wallup.deprecated.pojo.UnsplashAuthError {
        val converter = org.sourcei.wallup.deprecated.network.RetroApiClient.getClient()!!.responseBodyConverter<org.sourcei.wallup.deprecated.pojo.UnsplashAuthError>(org.sourcei.wallup.deprecated.pojo.UnsplashAuthError::class.java, arrayOfNulls<Annotation>(0))
        val error: org.sourcei.wallup.deprecated.pojo.UnsplashAuthError

        try {
            error = converter.convert(response.errorBody())
        } catch (e: Exception) {
            return org.sourcei.wallup.deprecated.pojo.UnsplashAuthError()
        }

        return error
    }
}