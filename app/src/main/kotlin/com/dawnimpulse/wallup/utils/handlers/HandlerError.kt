/**
 * ISC License
 *
 * Copyright 2020, Saksham (DawnImpulse)
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
package com.dawnimpulse.wallup.utils.handlers

import com.dawnimpulse.wallup.pojo.PojoError
import com.dawnimpulse.wallup.utils.reusables.RetroApiClient
import retrofit2.Response

/**
 * @info - error handler for retrofit
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-08-20 by Saksham
 * @note Updates :
 */
object HandlerError {

    fun parseError(response: Response<*>): PojoError {
        val converter = RetroApiClient.getClient()
            .responseBodyConverter<PojoError>(PojoError::class.java, arrayOfNulls<Annotation>(0))
        val error: PojoError

        try {
            error = converter.convert(response.errorBody()!!)!!
        } catch (e: Exception) {
            return PojoError()
        }

        return error
    }
}