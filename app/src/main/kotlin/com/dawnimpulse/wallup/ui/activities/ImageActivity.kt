package com.dawnimpulse.wallup.ui.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.objects.PexelsImageObject
import com.dawnimpulse.wallup.ui.objects.UnsplashImageObject
import com.dawnimpulse.wallup.ui.objects.WallupImageObject
import com.dawnimpulse.wallup.utils.PEXELS
import com.dawnimpulse.wallup.utils.TYPE
import com.dawnimpulse.wallup.utils.UNSPLASH
import com.dawnimpulse.wallup.utils.WALLUP
import com.dawnimpulse.wallup.utils.functions.F
import com.dawnimpulse.wallup.utils.functions.logd
import com.dawnimpulse.wallup.utils.handlers.ImageHandler
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
    private lateinit var type: String
    private lateinit var wallup: WallupImageObject
    private lateinit var unsplash: UnsplashImageObject
    private lateinit var pexels: PexelsImageObject

    // ------------------
    //      create
    // ------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        type = intent.extras!!.getString(TYPE)!!
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

    // ------------------
    //      click
    // ------------------
    override fun onClick(v: View?) {
        v?.let {
            when (v.id) {
                // image link
                previewImageAuthorLink.id -> {
                    when (type) {
                        UNSPLASH -> F.startWeb(this, unsplash.links.html)
                        PEXELS -> F.startWeb(this, pexels.url)
                        else -> {
                            when (wallup.issuer) {
                                UNSPLASH -> F.startWeb(this, "https://unsplash.com/photos/${wallup.rid}")
                                PEXELS -> F.startWeb(this, "https://pexels.com/photo/${wallup.rid}")
                                else -> wallup.html?.let { F.startWeb(this, it) }
                            }
                        }
                    }
                }

                // download
                previewImageDownload.id -> {
                }

                // wallpaper
                previewImageWallpaper.id -> {
                }

                // info
                previewImageInfo.id -> showInfo()

                else -> {
                }
            }
        }
    }

    // -------------------
    //    set details
    // -------------------
    private fun details() {
        when (type) {
            // check source type
            WALLUP -> {
                wallup = Gson().fromJson(intent.extras!!.getString(WALLUP)!!, WallupImageObject::class.java)

                // issuer for the image
                when (wallup.issuer) {

                    UNSPLASH -> {
                        // get bitmap
                        ImageHandler.getImageImgixBitmapCallback(this, wallup.urls[0], 720) {
                            logd("here3")
                            setBlurZoom(it)
                        }
                        // set author dp
                        ImageHandler.setImageImgix(previewImageAuthorI, wallup.author.dp ?: "", 256)
                        //set name
                        previewImageAuthorName.text = wallup.author.name
                    }
                    PEXELS -> {
                        // get bitmap
                        ImageHandler.getImageImgixBitmapCallback(this, wallup.urls[0], 720) {
                            logd("here3")
                            setBlurZoom(it)
                        }
                        // set name
                        previewImageAuthorName.text = wallup.author.name
                        // set author dp & link icon
                        previewImageAuthorI.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vd_pexels))
                        previewImageAuthorLinkI.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vd_new))
                    }
                    else -> {
                        /*ImageHandler.setImageImgix(previewImage, wallup.urls[0], 720)
                        ImageHandler.setImageImgix(previewImageAuthorI, wallup.author.dp ?: "", 256)
                        previewImageAuthorName.text = wallup.author.name
                        previewImageAuthorI.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vd_pexels))*/
                    }
                }

            }

            UNSPLASH -> {
                unsplash = Gson().fromJson(intent.extras!!.getString(UNSPLASH)!!, UnsplashImageObject::class.java)

                // get bitmap
                ImageHandler.getImageImgixBitmapCallback(this, unsplash.urls.raw, 720) {
                    setBlurZoom(it)
                }
                // set author dp
                ImageHandler.setImageImgix(previewImageAuthorI, unsplash.user.profile_image.large, 256)
                // set author name
                previewImageAuthorName.text = unsplash.user.name
            }

            PEXELS -> {
                pexels = Gson().fromJson(intent.extras!!.getString(PEXELS)!!, PexelsImageObject::class.java)

                // get bitmap
                ImageHandler.getImageImgixBitmapCallback(this, pexels.src.original, 720) {
                    setBlurZoom(it)
                }
                // set author name
                previewImageAuthorName.text = unsplash.user.name
                // set link icon
                previewImageAuthorI.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vd_pexels))
                previewImageAuthorLinkI.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vd_new))
            }
        }
    }


    // -------------------------------
    //    set blur & zoom animation
    // -------------------------------
    private fun setBlurZoom(bitmap: Bitmap?) {
        runOnUiThread {
            previewImage.setImageBitmap(bitmap)
            Blurry.with(this).from(bitmap).into(previewImageBg)

            when (type) {
                UNSPLASH -> {
                    if (unsplash.width > unsplash.height)
                        previewImage.animation = AnimationUtils.loadAnimation(this@ImageActivity, R.anim.image_zoom_out)
                    else
                        previewImage.animation = AnimationUtils.loadAnimation(this@ImageActivity, R.anim.image_zoom_in_out)
                }
                PEXELS -> {
                    if (pexels.width > pexels.height)
                        previewImage.animation = AnimationUtils.loadAnimation(this@ImageActivity, R.anim.image_zoom_out)
                    else
                        previewImage.animation = AnimationUtils.loadAnimation(this@ImageActivity, R.anim.image_zoom_in_out)
                }
                else -> {
                    if (wallup.width > wallup.height)
                        previewImage.animation = AnimationUtils.loadAnimation(this@ImageActivity, R.anim.image_zoom_out)
                    else
                        previewImage.animation = AnimationUtils.loadAnimation(this@ImageActivity, R.anim.image_zoom_in_out)
                }
            }
        }
    }

    // -------------------
    //    hide info
    // -------------------
    private fun hideInfo() {
        previewImageAuthorL.animate().translationY(-previewImageButton.height.toFloat())
        previewImageButton.animate().translationY(previewImageButton.height.toFloat())
        previewImageInfo.animate().alpha(1f)
    }

    // -------------------
    //    show info
    // -------------------
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
