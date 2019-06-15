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

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.dawnimpulse.wallup.network.repo.WallupRepo
import com.dawnimpulse.wallup.ui.objects.WallupImageObject
import com.dawnimpulse.wallup.utils.functions.logd
import com.dawnimpulse.wallup.utils.functions.toast
import com.dawnimpulse.wallup.utils.handlers.ImageHandler
import com.dawnimpulse.wallup.utils.handlers.StorageHandler
import com.dawnimpulse.wallup.utils.reusables.PEXELS
import com.dawnimpulse.wallup.utils.reusables.UNSPLASH
import com.google.common.util.concurrent.ListenableFuture
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.apache.commons.io.FileUtils
import org.apache.commons.io.comparator.LastModifiedFileComparator
import java.io.File
import java.util.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-14 by Saksham
 * @note Updates :
 */
class AutoWallpaper(private val appContext: Context, workerParams: WorkerParameters) : ListenableWorker(appContext, workerParams) {
    private lateinit var wallpaperManager: WallpaperManager
    private lateinit var handler: Handler
    private var compositeDisposable = CompositeDisposable()

    // ----------------
    //   start work
    // ----------------
    override fun startWork(): ListenableFuture<Result> {
        return CallbackToFutureAdapter.getFuture { completer ->
            wallpaperManager = WallpaperManager.getInstance(appContext)
            handler = Handler(Looper.getMainLooper())
            wallpaperChange {
                if (it)
                    completer.set(Result.success())
                else
                    completer.set(Result.retry())
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


    // -----------------------------
    //   wallpaper change handling
    // -----------------------------
    private fun wallpaperChange(callback: (Boolean) -> Unit) {
        val files = appContext.filesDir.listFiles()
        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR)

        // if < 5 wallpaper available in cache then get more
        if (files.size < 5) {
            // if images available in cache
            if (files.isNotEmpty()) {
                setWallpaper(callback)
                imagesFetching {
                    // do nothing for either case since wallpaper is set
                }
            } else {
                // no images available in cache, get some & then set wallpaper
                imagesFetching {
                    if (it) {
                        // after images are fetched
                        val files = appContext.filesDir.listFiles()

                        // if wallpapers are cached
                        if (files.isNotEmpty())
                            setWallpaper(callback)
                        else
                            callback(false)

                    } else
                    // issue with image fetching
                        callback(false)
                }
            }
        } else
        // get wallpaper for file from dir
            setWallpaper(callback)


    }


    // -------------------
    //   set wallpaper
    // -------------------
    private fun setWallpaper(callback: (Boolean) -> Unit) {
        val files = appContext.filesDir.listFiles()
        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR)

        val file = files[0]

        // file found
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.path)
            // check if bitmap is not null
            if (bitmap != null) {
                // set bitmap and increment lastWall
                wallpaperManager.setBitmap(bitmap)
                handler.post {
                    appContext.toast("wallpaper changed")
                }
                logd("wallpaper set ${file.name}")
                // delete the file
                FileUtils.deleteQuietly(file)
                callback(true)

            } else {
                // bitmap is null
                // delete the file
                FileUtils.deleteQuietly(file)
                callback(false)
            }
        } else {
            // file not found
            // we need to retry with next wallpaper
            callback(false)
        }
    }

    // ----------------------------
    //   fetch images from server
    // ----------------------------
    private fun imagesFetching(callback: (Boolean) -> Unit) {
        WallupRepo.editorialImages(10) { e, r ->
            e?.let {
                callback(false)
            }
            r?.let {
                wallpaperCaching(it) {
                    callback(true)
                }
            }
        }
    }

    // ----------------------------
    //   save images to internal
    // ----------------------------
    private fun wallpaperCaching(images: List<WallupImageObject>, callback: () -> Unit) {

        // image observable
        fun image(wall: WallupImageObject): Observable<Boolean> {
            return Observable.create<Boolean> { em ->
                when (wall.issuer) {
                    UNSPLASH, PEXELS -> {
                        // get bitmap for imgix
                        ImageHandler.getImageImgixBitmapCacheCallback(appContext, wall.urls[0], 1080, 95) {
                            // store in files dir
                            it?.let {
                                // get recent files

                                val file = File(appContext.filesDir, "${wall.iid}.jpg")
                                StorageHandler.storeBitmapInFile(it, file)
                                logd("image ${wall.iid} cached")
                            }
                            em.onComplete()
                        }
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
