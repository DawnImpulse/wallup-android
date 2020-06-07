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
package com.dawnimpulse.wallup.ui.activities

import android.app.WallpaperManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.network.controller.CtrlBookmark
import com.dawnimpulse.wallup.network.controller.CtrlRealtime
import com.dawnimpulse.wallup.objects.ObjectImage
import com.dawnimpulse.wallup.ui.sheets.SheetId
import com.dawnimpulse.wallup.ui.sheets.SheetUser
import com.dawnimpulse.wallup.utils.handlers.HandlerColor
import com.dawnimpulse.wallup.utils.handlers.HandlerDialog
import com.dawnimpulse.wallup.utils.handlers.HandlerDownload
import com.dawnimpulse.wallup.utils.handlers.HandlerTransform
import com.dawnimpulse.wallup.utils.reusables.*
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_image.*
import kotlinx.coroutines.*
import org.sourcei.android.permissions.Permissions

class ActivityImage : AppCompatActivity(R.layout.activity_image), View.OnClickListener {
    private lateinit var image: ObjectImage
    private lateinit var id: String
    private val sheetUser = SheetUser()
    private var loaded = false
    private var bookmarked = false
    private var scope: CoroutineScope = MainScope()

    /**
     * on create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        image = Gson().fromJson(intent.extras!!.getString(IMAGE, ""), ObjectImage::class.java)
        id = image.iid
        CtrlRealtime.incrementCounter(image._id, VIEW)
        HandlerDialog.loading(this) { if (!loaded) finish() }
        if (Hawk.contains(id)) {
            bookmarked = true
            activity_image_like_drawable.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vd_like_filled))
        }

        activity_image_download.setOnClickListener(this)
        activity_image_set_wallpaper.setOnClickListener(this)
        activity_image_like.setOnClickListener(this)
        activity_image_image.setOnLongClickListener {
            SheetId(id).open(supportFragmentManager)
            true
        }
    }

    /**
     * on destroy
     */
    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    /**
     * on resume
     */
    override fun onResume() {
        super.onResume()

        when {
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES -> window.decorView.systemUiVisibility = 0
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> {
                window.statusBarColor = Colors.BLACK
            }
        }

        image.link.imageTransform(this)
                .height(1080)
                .bitmap {
                    runOnUiThread {
                        if (it != null) {
                            loaded = true
                            val color = it.getPalette().vibrant()
                            val contrast = HandlerColor.getContrastColor(color)

                            activity_image_set_wallpaper.setCardBackgroundColor(color)
                            activity_image_set_wallpaper_text.color(contrast)
                            HandlerDialog.dismiss()
                            activity_image_image.setImageBitmap(it)
                        } else {
                            StyleToast.error("error fetching images")
                            finish()
                        }
                    }
                }
    }

    /**
     * on click listener
     */
    override fun onClick(v: View?) {
        v?.let {
            when (v.id) {
                activity_image_download.id -> download()
                activity_image_set_wallpaper.id -> wallpaper()
                activity_image_like.id -> {
                    if (FirebaseAuth.getInstance().currentUser != null)
                        bookmark()
                    else {
                        StyleToast.info("login to continue")
                        scope.launch {
                            delay(1000)
                            runOnUiThread { sheetUser.open(supportFragmentManager) }
                        }
                    }
                }
                else -> {
                }
            }
        }
    }

    /**
     * download image
     */
    private fun download() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            StyleToast.info("downloading image, please wait", Toast.LENGTH_LONG)
            HandlerDownload.downloadQ(image.link, "${image.iid}.jpg")
            CtrlRealtime.incrementCounter(image._id, DOWNLOAD)
        } else
            Permissions.askWriteExternalStoragePermission(this) { no, yes ->
                no?.let { StyleToast.error("kindly provide storage permission") }
                yes?.let {
                    HandlerDownload.downloadManager(image.link, "${image.iid}.jpg")
                    CtrlRealtime.incrementCounter(image._id, DOWNLOAD)
                }
            }
    }

    /**
     * set wallpaper
     */
    private fun wallpaper() {
        var loading = true
        StyleToast.info("download/setting wallpaper")
        HandlerDialog.loading(this) { loading = false }
        HandlerTransform(image.link, this)
                .height(1440)
                .format(JPG)
                .quality(95)
                .bitmap {
                    if (it != null) {
                        if (loading) {
                            onMain { StyleToast.info("setting wallpaper") }
                            WallpaperManager.getInstance(this).setBitmap(it)
                            onMain {
                                StyleToast.success("wallpaper set")
                                CtrlRealtime.incrementCounter(image._id, WALLPAPER)
                                HandlerDialog.dismiss()
                            }
                        }
                    } else
                        onMain { StyleToast.error("issue downloading image") }
                }
    }

    /**
     * bookmark icon
     */
    private fun bookmark() {
        if (!bookmarked) {
            scope.launch {
                try {
                    setBookmark(true)
                    Hawk.put(id, CtrlBookmark.create(image._id)._id)
                    CtrlRealtime.incrementCounter(image._id, LIKE)
                    RxBusType.accept(RxType(EVENT.ADD.BOOKMARK, image))
                } catch (e: Exception) {
                    setBookmark(false)
                    Hawk.delete(id)
                    e.printStackTrace()
                }
            }
        } else {
            scope.launch {
                val bk = Hawk.get<String>(id)
                try {
                    setBookmark(false)
                    CtrlBookmark.delete(bk)
                    CtrlRealtime.decreaseCounter(image._id, LIKE)
                    RxBusType.accept(RxType(EVENT.REMOVE.BOOKMARK, id))
                    Hawk.delete(id)
                } catch (e: Exception) {
                    setBookmark(true)
                    Hawk.put(id, bk)
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * set bookmark state
     *
     * @param state
     */
    private fun setBookmark(state: Boolean) {
        bookmarked = state
        runOnUiThread {
            if (state)
                activity_image_like_drawable.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vd_like_filled))
            else
                activity_image_like_drawable.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vd_like))
        }
    }
}