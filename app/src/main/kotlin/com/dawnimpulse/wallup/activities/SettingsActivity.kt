package com.dawnimpulse.wallup.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.toast
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Colors
import com.dawnimpulse.wallup.utils.Config
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener {

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settingPreviewListHQ.setOnClickListener(this)
        settingPreviewListHD.setOnClickListener(this)
        settingPreviewListFHD.setOnClickListener(this)
        settingPreviewImageHQ.setOnClickListener(this)
        settingPreviewImageHD.setOnClickListener(this)
        settingPreviewImageFHD.setOnClickListener(this)
        settingDownloadFHD.setOnClickListener(this)
        settingDownloadUHD.setOnClickListener(this)
        settingDownloadOriginal.setOnClickListener(this)

        settingPreviewListHQ.setOnLongClickListener(this)
        settingPreviewListHD.setOnLongClickListener(this)
        settingPreviewListFHD.setOnLongClickListener(this)
        settingPreviewImageHQ.setOnLongClickListener(this)
        settingPreviewImageHD.setOnLongClickListener(this)
        settingPreviewImageFHD.setOnLongClickListener(this)
        settingDownloadFHD.setOnLongClickListener(this)
        settingDownloadUHD.setOnLongClickListener(this)
        settingDownloadOriginal.setOnLongClickListener(this)

        settingDownloadAsk.setOnCheckedChangeListener { _, isChecked ->
            Prefs.putBoolean(C.IMAGE_DOWNLOAD_ASK, isChecked)
        }
    }

    // on click
    override fun onClick(v: View) {
        val drawable = ContextCompat.getDrawable(this, R.drawable.bt_round_complete_corners)
        val white = Colors(this).WHITE
        val black = Colors(this).BLACK
        when (v.id) {
            settingPreviewListHQ.id -> {
                toast("Not Recommended. Image quality can be very less , only select if you are on a data plan.")
                Prefs.putString(C.IMAGE_LIST_QUALITY, C.HQ)
                Config.IMAGE_LIST_QUALITY = C.HQ

                settingPreviewListHQT.background = drawable
                settingPreviewListHQT.setTextColor(black)

                settingPreviewListHDT.background = null
                settingPreviewListHDT.setTextColor(white)

                settingPreviewListFHDT.background = null
                settingPreviewListFHDT.setTextColor(white)
            }
            settingPreviewListHD.id -> {
                Prefs.putString(C.IMAGE_LIST_QUALITY, C.HD)
                Config.IMAGE_LIST_QUALITY = C.HD

                settingPreviewListHQT.background = null
                settingPreviewListHQT.setTextColor(white)

                settingPreviewListHDT.background = drawable
                settingPreviewListHDT.setTextColor(black)

                settingPreviewListFHDT.background = null
                settingPreviewListFHDT.setTextColor(white)
            }
            settingPreviewListFHD.id -> {
                Prefs.putString(C.IMAGE_LIST_QUALITY, C.FHD)
                Config.IMAGE_LIST_QUALITY = C.FHD

                settingPreviewListHQT.background = null
                settingPreviewListHQT.setTextColor(white)

                settingPreviewListHDT.background = null
                settingPreviewListHDT.setTextColor(white)

                settingPreviewListFHDT.background = drawable
                settingPreviewListFHDT.setTextColor(black)
            }
            settingPreviewImageHQ.id -> {
                toast("Not Recommended. Image quality can be very less , only select if you are on a data plan.")
                Prefs.putString(C.IMAGE_PREVIEW_QUALITY, C.HQ)
                Config.IMAGE_PREVIEW_QUALITY = C.HQ

                settingPreviewImageHQT.background = drawable
                settingPreviewImageHQT.setTextColor(black)

                settingPreviewImageHDT.background = null
                settingPreviewImageHDT.setTextColor(white)

                settingPreviewImageFHDT.background = null
                settingPreviewImageFHDT.setTextColor(white)
            }
            settingPreviewImageHD.id -> {
                Prefs.putString(C.IMAGE_PREVIEW_QUALITY, C.HD)
                Config.IMAGE_PREVIEW_QUALITY = C.HD

                settingPreviewImageHQT.background = null
                settingPreviewImageHQT.setTextColor(white)

                settingPreviewImageHDT.background = drawable
                settingPreviewImageHDT.setTextColor(black)

                settingPreviewImageFHDT.background = null
                settingPreviewImageFHDT.setTextColor(white)
            }
            settingPreviewImageFHD.id -> {
                Prefs.putString(C.IMAGE_PREVIEW_QUALITY, C.FHD)
                Config.IMAGE_PREVIEW_QUALITY = C.FHD

                settingPreviewImageHQT.background = null
                settingPreviewImageHQT.setTextColor(white)

                settingPreviewImageHDT.background = null
                settingPreviewImageHDT.setTextColor(white)

                settingPreviewImageFHDT.background = drawable
                settingPreviewImageFHDT.setTextColor(black)
            }
            settingDownloadFHD.id -> {
                Prefs.putString(C.IMAGE_DOWNLOAD_QUALITY, C.FHD)
                Config.IMAGE_DOWNLOAD_QUALITY = C.FHD

                settingDownloadFHDT.background = drawable
                settingDownloadFHDT.setTextColor(black)

                settingDownloadUHDT.background = null
                settingDownloadUHDT.setTextColor(white)

                settingDownloadOriginalT.background = null
                settingDownloadOriginalT.setTextColor(white)
            }
            settingDownloadUHD.id -> {
                Prefs.putString(C.IMAGE_DOWNLOAD_QUALITY, C.UHD)
                Config.IMAGE_DOWNLOAD_QUALITY = C.UHD

                settingDownloadFHDT.background = null
                settingDownloadFHDT.setTextColor(white)

                settingDownloadUHDT.background = drawable
                settingDownloadUHDT.setTextColor(black)

                settingDownloadOriginalT.background = null
                settingDownloadOriginalT.setTextColor(white)
            }
            settingDownloadOriginal.id -> {
                Prefs.putString(C.IMAGE_DOWNLOAD_QUALITY, "")
                Config.IMAGE_DOWNLOAD_QUALITY = ""

                settingDownloadFHDT.background = null
                settingDownloadFHDT.setTextColor(white)

                settingDownloadUHDT.background = null
                settingDownloadUHDT.setTextColor(white)

                settingDownloadOriginalT.background = drawable
                settingDownloadOriginalT.setTextColor(black)
            }
        }
    }

    // on long click
    override fun onLongClick(v: View): Boolean {
        when (v.id) {
            settingPreviewListHQ.id -> toast("High Quality (480p)")
            settingPreviewListHD.id -> toast("High Definition (720p)")
            settingPreviewListFHD.id -> toast("Full HD (1080p)")
            settingPreviewImageHQ.id -> toast("High Quality (480p)")
            settingPreviewImageHD.id -> toast("High Definition (720p)")
            settingPreviewImageFHD.id -> toast("Full HD (1080p)")
            settingDownloadFHD.id -> toast("Full HD (1080p)")
            settingDownloadUHD.id -> toast("Ultra HD (2160p)")
            settingDownloadOriginal.id -> toast("Original Image")
        }
        return true
    }
}
