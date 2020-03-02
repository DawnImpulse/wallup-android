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
package com.dawnimpulse.wallup.ui.models

import androidx.appcompat.app.AppCompatActivity
import com.dawnimpulse.wallup.network.controller.ImageCtrl
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.utils.reusables.Lifecycle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @info - bridge between ui & controller
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-03-02 by Saksham
 * @note Updates :
 */
class ModelImage(private val activity: AppCompatActivity) {

    /**
     * get random images
     *
     * @param callback
     */
    fun getRandomQuote(limit: Number, callback: (Any?, List<ImagePojo>?) -> Unit) {
        Lifecycle.onStart(activity) {

            var images: List<ImagePojo>? = null
            var error: java.lang.Exception? = null

            GlobalScope.launch {
                try {
                    images = ImageCtrl.random(limit)
                } catch (e: Exception) {
                    error = e
                } finally {
                    activity.runOnUiThread {
                        if (images == null)
                            callback(error, null)
                        else
                            callback(null, images)
                    }
                }
            }
        }
    }
}