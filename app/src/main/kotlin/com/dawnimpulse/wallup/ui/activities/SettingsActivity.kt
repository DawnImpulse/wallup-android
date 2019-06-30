package com.dawnimpulse.wallup.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.utils.functions.putAny
import com.dawnimpulse.wallup.utils.functions.remove
import com.dawnimpulse.wallup.utils.functions.toast
import com.dawnimpulse.wallup.utils.reusables.AUTO_WALLPAPER
import com.dawnimpulse.wallup.utils.reusables.Prefs
import com.dawnimpulse.wallup.workers.AutoWallpaper
import org.apache.commons.io.FileUtils
import java.util.*
import java.util.concurrent.TimeUnit

class SettingsActivity : AppCompatActivity() {

    /**
     * create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, MySettingsFragment())
                .commit()
    }

    /**
     * attaching preferences
     */
    class MySettingsFragment : PreferenceFragmentCompat(), androidx.preference.Preference.OnPreferenceChangeListener {

        private lateinit var wallStatus: SwitchPreference
        private lateinit var wallInterval: ListPreference
        private lateinit var wallWifi: SwitchPreference
        private lateinit var wallStorage: SwitchPreference

        /**
         * set preference layout
         */
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root, rootKey)

            wallStatus = findPreference("wallStatus")!!
            wallInterval = findPreference("wallInterval")!!
            wallWifi = findPreference("wallWifi")!!
//            wallStorage = findPreference("wallStorage")!!

            val time = Prefs.getString("wallInterval", "1440")!!.toLong()

            // interval naming
            val timing = when (time) {
                30.toLong() -> "30 min"
                60.toLong() -> "1 hour"
                180.toLong() -> "3 hours"
                360.toLong() -> "6 hours"
                720.toLong() -> "12 hours"
                1440.toLong() -> "1 day"
                4320.toLong() -> "3 days"
                else -> "7 days"
            }
            wallInterval.summary = "Change every $timing (tap to change)"

            wallStatus.onPreferenceChangeListener = this
            wallInterval.onPreferenceChangeListener = this
            wallWifi.onPreferenceChangeListener = this

        }

        /**
         * preference change
         */
        override fun onPreferenceChange(preference: androidx.preference.Preference?, newValue: Any?): Boolean {
            when (preference) {
                // wallpaper
                wallStatus -> {
                    newValue as Boolean
                    // if wallpaper enables
                    if (newValue) {

                        setWallpaper()

                    } else {
                        // wallpaper disabled

                        WorkManager.getInstance().cancelWorkById(UUID.fromString(Prefs.getString(AUTO_WALLPAPER, "")))
                        Prefs.remove(AUTO_WALLPAPER)
                        FileUtils.deleteDirectory(context!!.filesDir)
                    }
                }

                // interval
                wallInterval -> {
                    Prefs.putAny("wallInterval", newValue as String)
                    setWallpaper()
                }

                //wifi
                wallWifi -> {
                    Prefs.putAny("wallWifi", newValue as Boolean)
                    setWallpaper()
                }
            }

            return true
        }

        /**
         * remove & set wallpaper again
         */
        private fun setWallpaper() {

            // remove wallpaper if exists
            if (Prefs.contains(AUTO_WALLPAPER)) {
                WorkManager.getInstance().cancelWorkById(UUID.fromString(Prefs.getString(AUTO_WALLPAPER, "")))
                Prefs.remove(AUTO_WALLPAPER)
            }

            // get time
            val time = Prefs.getString("wallInterval", "1440")!!.toLong()
            val isWifi = Prefs.getBoolean("wallWifi", true)


            context!!.toast(time.toString())
            val builder = Constraints.Builder()
                    .setRequiredNetworkType(if (isWifi) NetworkType.UNMETERED else NetworkType.CONNECTED)
                    .build()

            // start wallpaper worker
            val uploadWorkRequest =
                    PeriodicWorkRequestBuilder<AutoWallpaper>(time, TimeUnit.MINUTES)
                            .setConstraints(builder)
                            .build()

            WorkManager.getInstance().enqueue(uploadWorkRequest)
            Prefs.putAny(AUTO_WALLPAPER, uploadWorkRequest.id.toString())

            // interval naming
            val timing = when (time) {
                30.toLong() -> "30 min"
                60.toLong() -> "1 hour"
                180.toLong() -> "3 hours"
                360.toLong() -> "6 hours"
                720.toLong() -> "12 hours"
                1440.toLong() -> "1 day"
                4320.toLong() -> "3 days"
                else -> "7 days"
            }
            wallInterval.summary = "Change every $timing (tap to change)"
        }
    }
}