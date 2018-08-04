package com.dawnimpulse.wallup.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
