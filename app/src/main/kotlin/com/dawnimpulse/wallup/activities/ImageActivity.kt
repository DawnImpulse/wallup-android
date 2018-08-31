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
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.dawnimpulse.permissions.android.Permissions
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.handlers.DateHandler
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.handlers.WallpaperHandler
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.utils.*
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
 */
class ImageActivity : AppCompatActivity(), View.OnClickListener {
    private val NAME = "ImageActivity"
    private lateinit var details: ImagePojo
    private lateinit var bitmap: Bitmap

    /**
     * On create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        var params = intent.extras
        details = Gson().fromJson(params.getString(C.IMAGE_POJO), ImagePojo::class.java)
        setImageDetails(details)

        imagePreviewWallpaper.setOnClickListener(this)
        imagePreviewDownload.setOnClickListener(this)
        imagePreviewAuthorL.setOnClickListener(this)

    }

    /**
     * On resume
     */
    override fun onResume() {
        super.onResume()
        ImageHandler.getImageAsBitmap(lifecycle, this, details.urls!!.full) {
            bitmap = it
            movingImage.setImageBitmap(bitmap)
            //var bottomSheet = BottomSheetImagePreview()
            //bottomSheet.show(supportFragmentManager, "bottom sheet")
        }
//        ImageHandler.setImageInView(lifecycle, movingImage, details.urls!!.full)
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
                        Config.imageBitmap = bitmap
                        WallpaperHandler.setHomescreenWallpaper(this@ImageActivity)
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
                var intent = Intent(this, ArtistProfile::class.java)
                intent.putExtra(C.USERNAME,details.user!!.username)
                startActivity(intent)
            }
        }
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
        F.underline(imagePreviewStatistics)

        if (details.downloads == 0) {
            imagePreviewDownloadCount.visibility = View.GONE
        }

    }
}
