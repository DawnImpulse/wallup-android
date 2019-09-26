/**
 * ISC License
 *
 * Copyright 2019, Saksham (DawnImpulse)
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
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.utils.functions.F
import com.dawnimpulse.wallup.utils.functions.toast
import com.dawnimpulse.wallup.utils.handlers.StorageHandler
import com.dawnimpulse.wallup.utils.handlers.WallpaperHandler
import com.dawnimpulse.wallup.utils.reusables.Config
import com.dawnimpulse.wallup.utils.reusables.IMAGE
import kotlinx.android.synthetic.main.activity_image.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.sourcei.android.permissions.Permissions
import java.io.File

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-09-26 by Saksham
 * @note Updates :
 */
class ImageActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var image: Bitmap

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        image = BitmapFactory.decodeFile(intent.getStringExtra(IMAGE))
        bgWallpaper.setImageBitmap(image)

        download.setOnClickListener(this)
        setWallpaper.setOnClickListener(this)
        back.setOnClickListener(this)
    }

    // on click
    override fun onClick(v: View) {
        when (v.id) {

            setWallpaper.id -> {
                // check permissions
                Permissions.askWriteExternalStoragePermission(this) { e, r ->
                    e?.let {
                        toast("kindly provide storage permissions")
                    }
                    r?.let {
                        F.mkdir()
                        toast("please wait")
                        GlobalScope.launch {
                            val file = File(Config.DEFAULT_DOWNLOAD_PATH, "${F.shortid()}.jpg")
                            StorageHandler.storeBitmapInFile(image, file)
                            WallpaperHandler.setWallpaper(this@ImageActivity, image)
                            // make available for media scanner
                            MediaScannerConnection.scanFile(this@ImageActivity, arrayOf(file.toString()), arrayOf("image/jpeg"), null)

                            runOnUiThread {
                                toast("wallpaper applied successfully")
                            }

                        }
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
                        toast("please wait")
                        GlobalScope.launch {
                            val file = File(Config.DEFAULT_DOWNLOAD_PATH, "${F.shortid()}.jpg")
                            StorageHandler.storeBitmapInFile(image, file)

                            // make available for media scanner
                            MediaScannerConnection.scanFile(this@ImageActivity, arrayOf(file.toString()), arrayOf("image/jpeg"), null)

                            runOnUiThread {
                                toast("image saved successfully")
                            }
                        }
                    }
                }
            }


            back.id -> finish()
        }
    }
}