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
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.handlers.DateHandler
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_image.*
import kotlinx.android.synthetic.main.content_image.*
import java.io.File

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
    private lateinit var type: String
    private lateinit var bitmap: Bitmap

    /**
     * On create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        var params = intent.extras
        details = Gson().fromJson(params.getString(C.IMAGE_POJO), ImagePojo::class.java)
        type = params.getString(C.TYPE)
        setImageDetails(details)

        imagePreviewWallpaper.setOnClickListener(this)
        imagePreviewDownload.setOnClickListener(this)
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
        if (v.id === imagePreviewWallpaper.id) {
            Config.imageBitmap = bitmap
            startActivity(Intent(this@ImageActivity, CropActivity::class.java))
        } else if (v.id === imagePreviewDownload.id) {
            Toast.short(this,"let do it")
            progress.visibility = View.VISIBLE
            val abc = "${Environment.getExternalStorageDirectory()}${File.separator}"
            val file = File(abc)

            Download.start(this,details.id, details.urls!!.raw, file.absolutePath, "${details.id}.jpg") {
                if (it != null) {
                    Toast.short(this@ImageActivity, "DOne")
                } else
                    Toast.short(this@ImageActivity, it!!)
            }

            Download.progress1(details.id) { e, p ->
                if (!e)
                    Toast.short(this, "Nah nothing")
                else {
                    progress.isIndeterminate = false
                    progress.progress = ((p!!.currentBytes / p!!.totalBytes).toInt()) * 100
                }
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

    }
}
