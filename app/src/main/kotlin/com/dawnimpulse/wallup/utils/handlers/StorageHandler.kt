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
package com.dawnimpulse.wallup.utils.handlers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.crashlytics.android.Crashlytics
import java.io.File
import java.io.FileOutputStream


/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - develop
 *
 * @note Created on 2019-06-15 by Saksham
 * @note Updates :
 *  Saksham - 2019 09 04 - develop - store bitmap with callback
 */
object StorageHandler {

    // -------------------------
    //   store bitmap in file
    // -------------------------
    fun storeBitmapInFile(bitmap: Bitmap, file: File) {

        try {
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fOut)
            fOut.flush()
            fOut.close()
        } catch (e: Exception) {
            Crashlytics.logException(e)
            e.printStackTrace()
        }
    }

    // ------------------------------
    //   store bitmap with callback
    // ------------------------------
    fun storeBitmapWithCallback(bitmap: Bitmap, file: File, callback: (Boolean) -> Unit) {

        try {
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fOut)
            fOut.flush()
            fOut.close()
            callback(true)
        } catch (e: Exception) {
            Crashlytics.logException(e)
            e.printStackTrace()
            callback(false)
        }
    }

    // -------------------------
    //   get bitmap from file
    // -------------------------
    fun getBitmapFromFile(file: File): Bitmap {
        val bmOptions = BitmapFactory.Options()
        return BitmapFactory.decodeFile(file.absolutePath, bmOptions)
    }
}