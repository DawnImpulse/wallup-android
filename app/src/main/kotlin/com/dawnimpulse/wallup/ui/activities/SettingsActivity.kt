package com.dawnimpulse.wallup.ui.activities

import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.utils.reusables.AUTO_WALLPAPER
import com.dawnimpulse.wallup.utils.reusables.Prefs
import com.dawnimpulse.wallup.workers.AutoWallpaper
import kotlinx.android.synthetic.main.activity_settings.*
import org.apache.commons.io.FileUtils
import java.util.*
import java.util.concurrent.TimeUnit

class SettingsActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {
    private var list = mutableListOf<Int>()

    // -------------
    //    create
    // -------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        if (Prefs.contains(AUTO_WALLPAPER))
            settingAutoWallpaper.isChecked = true

        settingAutoWallpaper.setOnCheckedChangeListener(this)
    }

    // -----------------
    //    check change
    // -----------------
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        buttonView?.let {
            when (buttonView.id) {
                // auto wallpaper
                settingAutoWallpaper.id -> {
                    // is checked
                    if (isChecked) {
                        if (!Prefs.contains(AUTO_WALLPAPER)) {
                            val uploadWorkRequest = PeriodicWorkRequestBuilder<AutoWallpaper>(15, TimeUnit.MINUTES).build()
                            WorkManager.getInstance().enqueue(uploadWorkRequest)
                            Prefs.edit().putString(AUTO_WALLPAPER, uploadWorkRequest.id.toString()).apply()
                        }
                    } else {

                        // not checked
                        WorkManager.getInstance().cancelWorkById(UUID.fromString(Prefs.getString(AUTO_WALLPAPER, "")))
                        Prefs.edit().remove(AUTO_WALLPAPER).apply()
                        FileUtils.deleteDirectory(filesDir)

                    }
                }
                else -> {

                }
            }
        }
    }
}