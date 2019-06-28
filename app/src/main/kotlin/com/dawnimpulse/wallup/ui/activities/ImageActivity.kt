package com.dawnimpulse.wallup.ui.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.objects.ImageObject
import com.dawnimpulse.wallup.utils.functions.F
import com.dawnimpulse.wallup.utils.functions.logd
import com.dawnimpulse.wallup.utils.functions.toast
import com.dawnimpulse.wallup.utils.handlers.ImageHandler
import com.dawnimpulse.wallup.utils.handlers.WallpaperHandler
import com.dawnimpulse.wallup.utils.reusables.WALLUP
import com.google.gson.Gson
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.activity_image.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
                }

                // wallpaper
                previewImageWallpaper.id -> {
                    logd("man")
                    if (::bitmap.isInitialized) {
                        logd("yeah")
                        WallpaperHandler.setWallpaper(this, bitmap)
                        logd("meah")
                    } else
                        toast("bitmap not available")
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

        ImageHandler.getBitmapImageFullscreen(this,wallup.links.url){
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
     * hide info
     */
    private fun hideInfo() {
        previewImageAuthorL.animate().translationY(-previewImageButton.height.toFloat())
        previewImageButton.animate().translationY(previewImageButton.height.toFloat())
        previewImageInfo.animate().alpha(1f)
    }

    /**
     * show info
     */
    private fun showInfo() {
        previewImageAuthorL.animate().translationY(0f)
        previewImageButton.animate().translationY(0f)
        previewImageInfo.animate().alpha(0f)

        GlobalScope.launch {
            Thread.sleep(1500)
            runOnUiThread {
                hideInfo()
            }
        }
    }
}
