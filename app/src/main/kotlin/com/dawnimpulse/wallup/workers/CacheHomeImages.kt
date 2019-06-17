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
package com.dawnimpulse.wallup.workers

import android.content.Context
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.dawnimpulse.wallup.utils.functions.logd
import com.dawnimpulse.wallup.utils.handlers.ImageHandler
import com.dawnimpulse.wallup.utils.reusables.Config
import com.dawnimpulse.wallup.utils.reusables.PEXELS
import com.dawnimpulse.wallup.utils.reusables.UNSPLASH
import com.google.common.util.concurrent.ListenableFuture
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-17 by Saksham
 * @note Updates :
 */
class CacheHomeImages(private val appContext: Context, workerParams: WorkerParameters) : ListenableWorker(appContext, workerParams) {
    private var compositeDisposable = CompositeDisposable()

    // ----------------
    //   start work
    // ----------------
    override fun startWork(): ListenableFuture<Result> {
        return CallbackToFutureAdapter.getFuture { completer ->
            val images = Config.homeImages + Config.editorialImages
            wallpaperCaching(images) {
                completer.set(Result.success())
            }
        }
    }

    // ----------------
    //   stop work
    // ----------------
    override fun onStopped() {
        super.onStopped()
        compositeDisposable.clear()
    }

    // ----------------------
    //   only cache images
    // ----------------------
    private fun wallpaperCaching(images: List<String>, callback: () -> Unit) {

        // image observable
        fun image(image: String): Observable<Boolean> {
            return Observable.create<Boolean> { em ->
                if(image.contains(UNSPLASH) || image.contains(PEXELS)){
                    ImageHandler.cacheImageCallback(appContext,image,480){
                        em.onComplete()
                    }
                }
            }
        }

        compositeDisposable.add(
                Observable.fromIterable(images)
                        .flatMap { image(it) }
                        .toList()
                        .subscribe { it ->
                            logd("all images cached")
                            callback()
                        }
        )
    }
}
