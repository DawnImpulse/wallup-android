package com.dawnimpulse.wallup.ui.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.objects.ImageObject
import com.dawnimpulse.wallup.utils.functions.*
import com.dawnimpulse.wallup.utils.handlers.DialogHandler
import com.dawnimpulse.wallup.utils.handlers.DownloadHandler
import com.dawnimpulse.wallup.utils.handlers.ImageHandler
import com.dawnimpulse.wallup.utils.handlers.WallpaperHandler
import com.dawnimpulse.wallup.utils.reusables.Config
import com.dawnimpulse.wallup.utils.reusables.WALLUP
import com.google.gson.Gson
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.inflator_image_fullscreen.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.sourcei.android.permissions.Permissions

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-13 by Saksham
 * @note Updates :
 */
class ImageActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var wallup: ImageObject
    private lateinit var bitmap: Bitmap

    // ------------------
    //      create
    // ------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        details()

        previewImageAuthorLink.setOnClickListener(this)
        previewImageDownload.setOnClickListener(this)
        previewImageWallpaper.setOnClickListener(this)
        previewImageInfo.setOnClickListener(this)

        GlobalScope.launch {
            Thread.sleep(1500)
            runOnUiThread {
                hideInfo()
            }
        }
    }


    /**
     * handling click for info, link , download & wallpaper
     */
    override fun onClick(v: View?) {
        v?.let {
            when (v.id) {

                // image link
                previewImageAuthorLink.id -> {
                    F.startWeb(this, wallup.links.html)
                }

                // download
                previewImageDownload.id -> {
                    Permissions.askWriteExternalStoragePermission(this) { e, r ->
                        e?.let {
                            toast("kindly provide storage permissions")
                        }
                        r?.let {
                            toast("downloading image, check notification")
                            F.mkdir()
                            DownloadHandler.downloadData(this, "${wallup.links.url}&fm=webp", "${wallup.iid}.webp")
                        }
                    }
                }

                // wallpaper
                previewImageWallpaper.id -> {

                    // check permissions
                    Permissions.askWriteExternalStoragePermission(this) { e, r ->
                        e?.let {
                            toast("kindly provide storage permissions")
                        }
                        r?.let {
                            val file = "${Config.DEFAULT_DOWNLOAD_PATH}/${wallup.iid}.webp"

                            // file already exists
                            if (file.toFile().exists())
                                WallpaperHandler.askToSetWallpaper(this, file)
                            else {
                                // download file with progress
                                DialogHandler.downloadProgress(this, "${wallup.links.url}&fm=webp", "${wallup.iid}.webp") {
                                    if (it) {
                                        // file downloaded set it
                                        WallpaperHandler.askToSetWallpaper(this, file)
                                    } else
                                        toast("failed setting wallpaper")
                                }
                            }
                        }
                    }
                }

                // info
                previewImageInfo.id -> showInfo()

                else -> {
                }
            }
        }
    }


    /**
     * set image & details
     */
    private fun details() {
        wallup = Gson().fromJson(intent.extras!!.getString(WALLUP)!!, ImageObject::class.java)

        previewImageAuthorName.text = wallup.author

        ImageHandler.getBitmapImageFullscreen(this, wallup.links.url) {
            setBlurZoom(it)
        }
    }


    /**
     * set background blur & start zoom animation
     * @param bitmap
     */
    private fun setBlurZoom(bitmap: Bitmap?) {
        runOnUiThread {
            bitmap?.let {
                this.bitmap = it
                previewImage.setImageBitmap(it)
                Blurry.with(this)
                        .async()
                        .sampling(4)
                        .from(bitmap)
                        .into(previewImageBg)
            }

            if (wallup.width > wallup.height)
                previewImage.animation = AnimationUtils.loadAnimation(this@ImageActivity, R.anim.image_zoom_out)
            else
                previewImage.animation = AnimationUtils.loadAnimation(this@ImageActivity, R.anim.image_zoom_in_out)
        }
    }

    /**
     * hide information bars
     */
    private fun hideInfo() {
        previewImageInfo.show()
        previewImageAuthorL.animate().translationY(-previewImageButton.height.toFloat())
        previewImageButton.animate().translationY(previewImageButton.height.toFloat())
        previewImageInfo.animate().alpha(1f)
    }

    /**
     * show information bars
     */
    private fun showInfo() {
        previewImageAuthorL.animate().translationY(0f)
        previewImageButton.animate().translationY(0f)
        previewImageInfo.animate().alpha(0f)
        previewImageInfo.gone()

        GlobalScope.launch {
            Thread.sleep(3000)
            runOnUiThread {
                hideInfo()
            }
        }
    }
}
