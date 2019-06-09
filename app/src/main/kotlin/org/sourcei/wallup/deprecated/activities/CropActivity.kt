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
package org.sourcei.wallup.deprecated.activities

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_crop.*
import org.apache.commons.io.FileUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.sourcei.wallup.deprecated.R
import org.sourcei.wallup.deprecated.utils.*


/**
 * @author Saksham
 *
 * @note Last Branch Update - hotfixes
 * @note Created on 2018-07-20 by Saksham
 *
 * @note Updates :
 * Saksham - 2019 01 15 - hotfixes - issue in content uri & other bugs
 * Saksham - 2019 02 06 - master - exception handling
 */
class CropActivity : AppCompatActivity(), View.OnClickListener {
    private val NAME = "CropActivity"
    private lateinit var displayDimen: Pair<Int, Int>
    private lateinit var image: Bitmap
    private lateinit var path: String
    private lateinit var id: String
    private lateinit var url: String

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop)

        displayDimen = displayRatio()

        cropDefault.setOnClickListener(this)
        cropSelected.setOnClickListener(this)

        url = intent.getStringExtra(org.sourcei.wallup.deprecated.utils.C.IMAGE)
        id = intent.getStringExtra(org.sourcei.wallup.deprecated.utils.C.ID)
        path = Prefs.getString(org.sourcei.wallup.deprecated.utils.C.DOWNLOAD_PATH, org.sourcei.wallup.deprecated.utils.Config.DEFAULT_DOWNLOAD_PATH).toFileString()
        if (!FileUtils.directoryContains(path.toFile(), "$path/$id.jpg".toFile())) {
            org.sourcei.wallup.deprecated.handlers.DialogHandler.downloadProgress(this, path, url, id) {
                if (it) {
                    getImage()
                } else {
                    runOnUiThread {
                        toast("Unable to complete download , please try again!!")
                        finish()
                    }
                }
            }
        } else
            getImage()
    }

    // on start
    override fun onStart() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
        super.onStart()
    }

    // on destroy
    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    // on click
    override fun onClick(v: View) {
        toast("Applying Wallpaper")
        when (v.id) {
            cropDefault.id -> org.sourcei.wallup.deprecated.handlers.WallpaperHandler.setHomescreenWallpaper(this, image)
            //cropSelected.id -> WallpaperHandler.setHomescreenWallpaper(this, cropImageView.croppedImage, false)
        }
        finish()
    }

    // on message event
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: org.sourcei.wallup.deprecated.utils.Event) {
        if (event.obj.has(org.sourcei.wallup.deprecated.utils.C.TYPE)) {
            if (event.obj.getString(org.sourcei.wallup.deprecated.utils.C.TYPE) == org.sourcei.wallup.deprecated.utils.C.CANCEL) {
                toast("Unable to complete download , please try again!!")
                finish()
            }
        }
    }

    //get image from storage
    private fun getImage() {
        try {
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.data = "$path/$id.jpg".toFileUri()
            sendBroadcast(intent)

            val context = this@CropActivity
            val contentUri = "$path/$id.jpg".toContentUri(context)
            org.sourcei.wallup.deprecated.handlers.WallpaperHandler.cropAndSetWallpaper(context, contentUri)
        } catch (e: Exception) {
            e.printStackTrace()
            toast("Issue with getting image from SD card!! Kindly switch to Internal Storage.")
        } finally {
            finish()
        }
    }
}
