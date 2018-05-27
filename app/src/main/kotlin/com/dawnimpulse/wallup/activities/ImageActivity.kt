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

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.F
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
 */
class ImageActivity : AppCompatActivity() {
    private val NAME = "ImageActivity"
    private lateinit var details: ImagePojo
    private lateinit var type: String

    /**
     * On create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        var params = intent.extras
        details = Gson().fromJson(params.getString(C.IMAGE_POJO), ImagePojo::class.java)
        type = params.getString(C.TYPE)
    }

    /**
     * On resume
     */
    override fun onResume() {
        super.onResume()
        ImageHandler.getImageAsBitmap(lifecycle, this, details.urls!!.full, {
            movingImage.setImageBitmap(it)
            F.underline(imagePreviewExif)
            F.underline(imagePreviewStatistics)

            //var bottomSheet = BottomSheetImagePreview()
            //bottomSheet.show(supportFragmentManager, "bottom sheet")
        })
//        ImageHandler.setImageInView(lifecycle, movingImage, details.urls!!.full)
    }
}
