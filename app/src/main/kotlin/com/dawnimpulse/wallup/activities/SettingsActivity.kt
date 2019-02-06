package com.dawnimpulse.wallup.activities

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.handlers.DialogHandler
import com.dawnimpulse.wallup.utils.*
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_settings.*

/**
 * @author Saksham
 *
 * @note Last Branch Update - recent
 * @note Created on 2018-12 by Saksham
 *
 * @note Updates :
 *  Saksham - 2018 12 28 - master - wallpaper quality
 */
class SettingsActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener {
    private val NAME = "SettingsActivity"
    private var showToast = true
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
        settingCacheL.setOnClickListener(this)

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

        /*settingWallpaperAsk.setOnCheckedChangeListener { _, isChecked ->
            Prefs.putBoolean(C.IMAGE_WALLPAPER_ASK, isChecked)
        }*/

        settingCrashlytics.setOnCheckedChangeListener { _, isChecked ->
            Prefs.putBoolean(C.CRASHLYTICS, isChecked)
        }
        settingAnalytics.setOnCheckedChangeListener { _, isChecked ->
            Prefs.putBoolean(C.ANALYTICS, isChecked)
        }

        setDetails()
        F.appCache(this) {
            settingCache.text = "$it (tap to clean cache)"
        }
    }

    // on click
    override fun onClick(v: View) {
        val drawable = ContextCompat.getDrawable(this, R.drawable.bt_round_complete_corners)
        val white = Colors(this).WHITE
        val black = Colors(this).BLACK
        when (v.id) {
            settingPreviewListHQ.id -> {
                if (showToast)
                    toast("Not Recommended. Image quality can be very less , only select if you are on a data plan.", Toast.LENGTH_LONG)
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
                if (showToast)
                    toast("Not Recommended. Image quality can be very less , only select if you are on a data plan.", Toast.LENGTH_LONG)
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
                Prefs.putString(C.IMAGE_DOWNLOAD_QUALITY, C.O)
                Config.IMAGE_DOWNLOAD_QUALITY = C.O

                settingDownloadFHDT.background = null
                settingDownloadFHDT.setTextColor(white)

                settingDownloadUHDT.background = null
                settingDownloadUHDT.setTextColor(white)

                settingDownloadOriginalT.background = drawable
                settingDownloadOriginalT.setTextColor(black)
            }
            /*settingWallpaperFHD.id -> {
                Prefs.putString(C.IMAGE_WALLPAPER_QUALITY, C.FHD)
                Config.IMAGE_WALLPAPER_QUALITY = C.FHD

                settingWallpaperFHDT.background = drawable
                settingWallpaperFHDT.setTextColor(black)

                settingWallpaperUHDT.background = null
                settingWallpaperUHDT.setTextColor(white)
            }
            settingWallpaperUHD.id -> {
                Prefs.putString(C.IMAGE_WALLPAPER_QUALITY, C.UHD)
                Config.IMAGE_WALLPAPER_QUALITY = C.UHD

                settingWallpaperFHDT.background = null
                settingWallpaperFHDT.setTextColor(white)

                settingWallpaperUHDT.background = drawable
                settingWallpaperUHDT.setTextColor(black)
            }*/
            settingCacheL.id -> {
                DialogHandler.simpleOk(this, "Clear Application Cache", "", DialogInterface.OnClickListener { _, _ ->
                    F.deleteCache(this)
                    toast("Cache cleared")
                    settingCache.text = "0MB"
                })
            }
        }
        showToast = true
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
            /*settingWallpaperFHD.id -> showToast("Full HD (1080p)")
            settingWallpaperUHD.id -> showToast("Ultra HD (2160p)")*/
        }
        return true
    }

    // applying details from Prefs
    private fun setDetails() {
        val list = Prefs.getString(C.IMAGE_LIST_QUALITY, Config.IMAGE_LIST_QUALITY)
        val preview = Prefs.getString(C.IMAGE_PREVIEW_QUALITY, Config.IMAGE_PREVIEW_QUALITY)
        val download = Prefs.getString(C.IMAGE_DOWNLOAD_QUALITY, Config.IMAGE_DOWNLOAD_QUALITY)
        val wallpaper = Prefs.getString(C.IMAGE_WALLPAPER_QUALITY, Config.IMAGE_WALLPAPER_QUALITY)

        when (list) {
            C.HQ -> {
                showToast = false
                settingPreviewListHQ.performClick()
            }
            C.HD -> settingPreviewListHD.performClick()
            C.FHD -> settingPreviewListFHD.performClick()
        }

        when (preview) {
            C.HQ -> {
                showToast = false
                settingPreviewImageHQ.performClick()
            }
            C.HD -> settingPreviewImageHD.performClick()
            C.FHD -> settingPreviewImageFHD.performClick()
        }

        when (download) {
            C.FHD -> settingDownloadFHD.performClick()
            C.UHD -> settingDownloadUHD.performClick()
            C.O -> settingDownloadOriginal.performClick()
        }

        /*when(wallpaper){
            C.FHD -> settingWallpaperFHD.performClick()
            C.UHD -> settingWallpaperUHD.performClick()
        }*/

        settingDownloadAsk.isChecked = Prefs.getBoolean(C.IMAGE_DOWNLOAD_ASK, true)
        //settingWallpaperAsk.isChecked = Prefs.getBoolean(C.IMAGE_WALLPAPER_ASK, true)
        settingCrashlytics.isChecked = Prefs.getBoolean(C.CRASHLYTICS, true)
        settingAnalytics.isChecked = Prefs.getBoolean(C.ANALYTICS, true)
    }
}
