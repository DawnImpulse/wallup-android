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
package com.dawnimpulse.wallup.ui.activities

import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import co.revely.gradient.RevelyGradient
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.utils.functions.*
import com.dawnimpulse.wallup.utils.handlers.ImageHandler
import com.dawnimpulse.wallup.utils.handlers.StorageHandler
import com.dawnimpulse.wallup.utils.handlers.WallpaperHandler
import com.dawnimpulse.wallup.utils.reusables.*
import kotlinx.android.synthetic.main.activity_wallpaper.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.sourcei.android.permissions.Permissions
import java.io.File

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - develop
 *
 * @note Created on 2019-08-18 by Saksham
 * @note Updates :
 *  Saksham - 2019 09 01 - develop - bug fix : assign image to bitmap variable on app open
 *  Saksham - 2019 09 02 - develop - save bitmap in cache dir
 *  Saksham - 2019 09 04 - develop - duplicate bitmap handling
 */
class WallpaperActivity : AppCompatActivity(), View.OnClickListener {
    private var bitmap: Bitmap? = null
    var refreshing = false

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallpaper)

        // fab gradient
        fabGradient()

        // check for temp image & apply
        if (File(cacheDir, "homescreen.jpg").exists()) {
            bitmap = StorageHandler.getBitmapFromFile(File(cacheDir, "homescreen.jpg"))
            bgWallpaper.setImageBitmap(bitmap)
        } else {
            // if no temp image then auto load image
            refreshing = true
            mask.show()
            progress.show()
            getImage()
        }

        refresh.setOnClickListener(this)
        settings.setOnClickListener(this)
        setWallpaper.setOnClickListener(this)
        download.setOnClickListener(this)
    }

    // fab click handling
    override fun onClick(v: View) {
        when (v.id) {

            refresh.id -> {
                if (!refreshing) {
                    refreshing = true
                    mask.show()
                    progress.show()
                    getImage()
                } else
                    toast("In Progress")
            }

            settings.id -> {
                openActivity(SettingsActivity::class.java)
            }

            setWallpaper.id -> {

                // check permissions
                Permissions.askWriteExternalStoragePermission(this) { e, r ->
                    e?.let {
                        toast("kindly provide storage permissions")
                    }
                    r?.let {
                        F.mkdir()
                        if (!refreshing && bitmap != null) {
                            toast("please wait")
                            GlobalScope.launch {
                                val file = File(Config.DEFAULT_DOWNLOAD_PATH, "${F.shortid()}.jpg")
                                StorageHandler.storeBitmapInFile(bitmap!!, file)
                                WallpaperHandler.setWallpaper(this@WallpaperActivity, bitmap!!)
                                // make available for media scanner
                                MediaScannerConnection.scanFile(this@WallpaperActivity, arrayOf(file.toString()), arrayOf("image/jpeg"), null)

                                runOnUiThread {
                                    toast("wallpaper applied successfully")
                                }

                            }
                        } else
                            toast("kindly wait for wallpaper to load")
                    }
                }
            }

            download.id -> {
                Permissions.askWriteExternalStoragePermission(this) { e, r ->
                    e?.let {
                        toast("kindly provide storage permissions")
                    }
                    r?.let {
                        F.mkdir()
                        if (!refreshing && bitmap != null) {
                            toast("please wait")
                            GlobalScope.launch {
                                val file = File(Config.DEFAULT_DOWNLOAD_PATH, "${F.shortid()}.jpg")
                                StorageHandler.storeBitmapInFile(bitmap!!, file)

                                // make available for media scanner
                                MediaScannerConnection.scanFile(this@WallpaperActivity, arrayOf(file.toString()), arrayOf("image/jpeg"), null)

                                runOnUiThread {
                                    toast("image saved successfully")
                                }
                            }
                        } else
                            toast("kindly wait for wallpaper to load")
                    }
                }
            }
        }
    }

    /**
     * random gradient for fab
     */
    private fun fabGradient() {
        RevelyGradient
                .linear()
                .colors(intArrayOf(F.randomColor().toColorInt(), F.randomColor().toColorInt()))
                .onBackgroundOf(fabLayout)
    }

    /**
     * get new image
     */
    private fun getImage() {

        ImageHandler.getBitmapWallpaper(this, "https://source.unsplash.com/random/1440x3040/?${Prefs.getString("search", "")}") {
            runOnUiThread {
                if (it != null) {
                    F.compareBitmaps(this, it, bitmap) { com ->
                        runOnUiThread {
                            if (com)
                                getImage() // get image again if received the same one
                            else {
                                bitmap = it
                                bgWallpaper.setImageBitmap(it)

                                // save bitmap in temp directory
                                StorageHandler.storeBitmapInFile(it, File(cacheDir, "homescreen.jpg"))

                                // save bitmap in cached directory
                                val cached = File(filesDir, CACHED)
                                StorageHandler.storeBitmapInFile(it, File(cached, "${F.shortid()}.jpg"))

                                // if extra images in cached then delete them
                                F.deleteCached(this, Prefs.getString(CACHE_NUMBER, "25")!!.toInt())

                                // change wallpaper if allowed
                                if (Prefs.getBoolean(WALL_CHANGE, false))
                                    WallpaperHandler.setWallpaper(this, it)

                                mask.gone()
                                progress.gone()
                                refreshing = false
                            }
                        }
                    }
                } else { // if bitmap is null
                    toast("failed to fetch image")
                    mask.gone()
                    progress.gone()
                    refreshing = false
                }
            }
        }
    }

}