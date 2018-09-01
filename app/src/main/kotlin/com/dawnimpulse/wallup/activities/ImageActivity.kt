/*
ISC License

Copyright 2018, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/
package com.dawnimpulse.wallup.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.toast
import com.dawnimpulse.permissions.android.Permissions
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.handlers.DateHandler
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.handlers.WallpaperHandler
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.utils.*
import com.dawnimpulse.wallup.utils.sheets.ModalSheetExif
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_image.*
import kotlinx.android.synthetic.main.content_image.*

/**
 * @author Saksham
 *
 * @note Last Branch Update - recent
 * @note Created on 2018-05-24 by Saksham
 *
 * @note Updates :
 *  Saksham - 2018 05 25 - recent - working with single image display
 *  Saksham - 2018 07 20 - recent - adding listeners
 *  Saksham - 2018 07 26 - recent - downloading
 *  Saksham - 2018 09 01 - master - exif bottom sheet
 */
class ImageActivity : AppCompatActivity(), View.OnClickListener {
    private val NAME = "ImageActivity"
    private var setBitmap = false
    private var bitmap: Bitmap? = null
    private lateinit var details: ImagePojo
    private lateinit var model: UnsplashModel
    private lateinit var exifSheet: ModalSheetExif
    private var color: Int = 0

    /**
     * On create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        exifSheet = ModalSheetExif()

        var params = intent.extras
        details = Gson().fromJson(params.getString(C.IMAGE_POJO), ImagePojo::class.java)

        ImageHandler.getImageAsBitmap(lifecycle, this, details.urls!!.full + Config.IMAGE_HEIGHT) {
            color = ColorModifier.getNonDarkColor(Palette.from(it).generate(), this)
            color()
            imagePreviewLow.setImageBitmap(it)
        }
        ImageHandler.getImageAsBitmap(lifecycle, this, details.urls!!.full) {
            bitmap = it
            movingImage.setImageBitmap(bitmap)
            movingImage.visibility = View.VISIBLE
            imagePreviewLow.visibility = View.GONE
            imagePreviewProgress.visibility = View.GONE

            if (setBitmap) {
                Config.imageBitmap = bitmap!!
                WallpaperHandler.setHomescreenWallpaper(this@ImageActivity)
            }
        }
        setImageDetails(details)

        imagePreviewWallpaper.setOnClickListener(this)
        imagePreviewDownload.setOnClickListener(this)
        imagePreviewAuthorL.setOnClickListener(this)
        imagePreviewExif.setOnClickListener(this)

    }

    /**
     * On resume
     */
    override fun onResume() {
        super.onResume()
        if (Config.imagePojo != null)
            details = Config.imagePojo!!
    }

    /**
     * On stop
     */
    override fun onStop() {
        super.onStop()
        Config.imagePojo = null
    }

    /**
     * On click for various buttons
     */
    override fun onClick(v: View) {
        when (v.id) {
            imagePreviewWallpaper.id -> {
                /*Config.imageBitmap = bitmap
                startActivity(Intent(this@ImageActivity, CropActivity::class.java))*/
                Permissions.askWriteExternalStoragePermission(this) { no, _ ->
                    if (no != null)
                        Toast.short(this@ImageActivity, "Kindly provide external storage permission in Settings")
                    else {
                        if (bitmap != null) {
                            Config.imageBitmap = bitmap!!
                            WallpaperHandler.setHomescreenWallpaper(this@ImageActivity)
                        } else {
                            toast("Waiting for High Quality Image to load ...")
                            setBitmap = true
                        }
                    }
                }
            }
            imagePreviewDownload.id -> {
                Permissions.askWriteExternalStoragePermission(this) { no, _ ->
                    if (no != null)
                        Toast.short(this@ImageActivity, "Kindly provide external storage permission in Settings")
                    else {
                        Download.downloadData(this, details.links!!.download, details.id)
                        Toast.short(this, "Downloading Image in /Downloads/Wallup/${details.id}.jpg")
                    }
                }

            }
            imagePreviewAuthorL.id -> {
                var intent = Intent(this, ArtistProfileActivity::class.java)
                intent.putExtra(C.USERNAME, details.user!!.username)
                startActivity(intent)
            }
            imagePreviewExif.id -> {
                var bundle = bundleOf(Pair(C.IMAGE_POJO, Gson().toJson(details)))
                if (color != 0) bundle.putInt(C.COLOR, color)

                exifSheet.arguments = bundle
                exifSheet.show(supportFragmentManager, exifSheet.tag)
            }
        }
    }

    /**
     * On back pressed
     */
    override fun onBackPressed() {
        finish()
    }

    /**
     * set image details on views
     *
     */
    private fun setImageDetails(details: ImagePojo) {
        imagePreviewAuthorName.text = details.user!!.name

        imagePreviewLikesCount.text = F.withSuffix(details.likes)
        imagePreviewAuthorImages.text = F.withSuffix(details.user!!.total_photos)
        imagePreviewAuthorCollections.text = F.withSuffix(details.user!!.total_collections)
        imagePreviewViewsCount.text = F.withSuffix(details.views)
        imagePreviewDownloadCount.text = F.withSuffix(details.downloads)
        imagePreviewPublishedOn.text = "Published on ${DateHandler.convertForImagePreview(details.created_at)}"

        ImageHandler.setImageInView(lifecycle, imagePreviewAuthorImage, details.user!!.profile_image!!.large)
        F.underline(imagePreviewExif)
        //F.underline(imagePreviewStatistics)

        if (details.downloads == 0) {
            imagePreviewDownloadCount.visibility = View.GONE
        }

    }

    /**
     * set color on a resources
     */
    private fun color() {
        var down = imagePreviewDownload.background.current as GradientDrawable
        var wall = imagePreviewWallpaper.background.current as GradientDrawable

        imagePreviewAuthorImagesL.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        imagePreviewAuthorCollectionsL.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        imagePreviewShareI.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        imagePreviewViewsI.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)

        down.setColor(color)
        wall.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        imagePreviewWallpaperT.setTextColor(color)
    }
}
