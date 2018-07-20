package com.dawnimpulse.wallup.activities

import android.app.WallpaperManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.utils.Config
import com.dawnimpulse.wallup.utils.Toast
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
class CropActivity : AppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop)

        cropImageView.setImageBitmap(Config.imageBitmap)
        cropImageView.setAspectRatio(9,16)
        cropImageView.setFixedAspectRatio(true)
        cropImageView.scaleType = CropImageView.ScaleType.CENTER_INSIDE
        cropImageView.isAutoZoomEnabled = false

        cropButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        setHomescreenWallpaper()
    }

    private fun setHomescreenWallpaper() {
        val wallpaperManager = WallpaperManager.getInstance(this)
        wallpaperManager.setBitmap(cropImageView.croppedImage)
        Toast.short(this,"Wallpaper Set")
    }
}
