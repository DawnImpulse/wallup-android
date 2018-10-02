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
import androidx.appcompat.app.AppCompatActivity
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.utils.Config
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_crop.*



/**
 * @author Saksham
 *
 * @note Last Branch Update - recent
 * @note Created on 2018-07-20 by Saksham
 *
 * @note Updates :
 */
class CropActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop)

        cropImageView.setImageBitmap(Config.imageBitmap)
        cropImageView.setAspectRatio(9,16)
        cropImageView.setFixedAspectRatio(true)
        cropImageView.scaleType = CropImageView.ScaleType.CENTER_INSIDE
        cropImageView.isAutoZoomEnabled = false
    }
}
