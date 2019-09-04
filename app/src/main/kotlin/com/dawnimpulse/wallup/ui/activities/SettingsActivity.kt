package com.dawnimpulse.wallup.ui.activities

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.*
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.crashlytics.android.Crashlytics
import com.dawnimpulse.wallup.BuildConfig
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.utils.functions.F
import com.dawnimpulse.wallup.utils.functions.putAny
import com.dawnimpulse.wallup.utils.functions.remove
import com.dawnimpulse.wallup.utils.functions.toast
import com.dawnimpulse.wallup.utils.handlers.DialogHandler
import com.dawnimpulse.wallup.utils.reusables.*
import com.dawnimpulse.wallup.workers.AutoWallpaper
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author Saksham
 *
 * @note Last Branch Update - develop
 * @note Created on 2019-07 by Saksham
 *
 * @note Updates :
 *  Saksham - 2019 09 02 - develop - clear cache on search term change + cache options
 */
class SettingsActivity : AppCompatActivity() {

    /**
     * create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        setContentView(R.layout.activity_settings)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, MySettingsFragment())
                .commit()
    }

    /**
     * attaching preferences
     */
    class MySettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

        private lateinit var wallStatus: SwitchPreference
        private lateinit var wallInterval: ListPreference
        private lateinit var wallWifi: SwitchPreference
        private lateinit var crashlytics: SwitchPreference
        private lateinit var analytics: SwitchPreference
        private lateinit var wallChange: SwitchPreference
        private lateinit var search: EditTextPreference
        private lateinit var cacheNumber: ListPreference
        private lateinit var cacheClear: Preference

        /**
         * set preference layout
         */
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)

            // get preferences
            wallStatus = findPreference("wallStatus")!!
            wallInterval = findPreference("wallInterval")!!
            wallWifi = findPreference("wallWifi")!!
            crashlytics = findPreference("crashlytics")!!
            analytics = findPreference("analytics")!!
            wallChange = findPreference("wallChange")!!
            search = findPreference("search")!!
            cacheNumber = findPreference(CACHE_NUMBER)!!
            cacheClear = findPreference(DELETE_CACHE)!!

            // setting application version
            findPreference<Preference>("version")!!.summary = "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"

            // setting time interval
            val time = Prefs.getString("wallInterval", "1440")!!.toLong()

            // interval naming
            val timing = when (time) {
                15.toLong() -> "15 min"
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

            // search text
            if (Prefs.getString("search", "")!!.isEmpty())
                search.summary = "(no search term, will show random images)"
            else
                search.summary = Prefs.getString("search", "")

            // cached number of images
            cacheNumber.summary = "Caching upto ${Prefs.getString(CACHE_NUMBER, "25")} images"

            // listeners
            wallStatus.onPreferenceChangeListener = this
            wallInterval.onPreferenceChangeListener = this
            wallWifi.onPreferenceChangeListener = this
            search.onPreferenceChangeListener = this
            cacheNumber.onPreferenceChangeListener = this

            // clear cache listener
            cacheClear.setOnPreferenceClickListener {
                DialogHandler.simpleOk(context!!, "Clear cache", "Wish to clear all images in cache ?", DialogInterface.OnClickListener { _, _ ->
                    F.deleteAllCached(context!!)
                    context!!.toast("cleared cache")
                })
                true
            }

        }

        /**
         * preference change
         */
        override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
            when (preference) {
                // wallpaper
                wallStatus -> {
                    newValue as Boolean
                    // if wallpaper enables
                    if (newValue) {

                        setWallpaper()

                    } else {
                        // wallpaper disabled

                        WorkManager.getInstance(context!!).cancelWorkById(UUID.fromString(Prefs.getString(AUTO_WALLPAPER, "")))
                        Prefs.remove(AUTO_WALLPAPER)
                        //FileUtils.deleteDirectory(context!!.filesDir)
                    }
                }

                // interval
                wallInterval -> {
                    Prefs.putAny("wallInterval", newValue as String)
                    setWallpaper()
                }

                // wifi
                wallWifi -> {
                    Prefs.putAny("wallWifi", newValue as Boolean)
                    setWallpaper()
                }

                // crashlytics
                crashlytics -> Prefs.putAny(CRASHLYTICS, newValue as Boolean)

                // analytics
                analytics -> Prefs.putAny(ANALYTICS, newValue as Boolean)

                // search
                search -> {
                    context!!.toast("will start showing new wallpapers on next refresh")

                    // clear cached images
                    val files = context!!.filesDir.listFiles().filter { it.name.contains(".jpg") }.toTypedArray()
                    try {
                        files.forEach { it.delete() }
                    } catch (e: Exception) {
                        Crashlytics.logException(e)
                        e.printStackTrace()
                    }

                    // change title
                    if (newValue.toString().isEmpty())
                        search.summary = "(no search term, will show random images)"
                    else
                        search.summary = newValue.toString()
                }

                // cache number
                cacheNumber -> {
                    val amount = (newValue as String).toInt()
                    F.deleteCached(context!!, amount)
                    cacheNumber.summary = "Caching upto $amount images"
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
                WorkManager.getInstance(context!!).cancelWorkById(UUID.fromString(Prefs.getString(AUTO_WALLPAPER, "")))
                Prefs.remove(AUTO_WALLPAPER)
            }

            // get time
            val time = Prefs.getString("wallInterval", "1440")!!.toLong()
            val isWifi = Prefs.getBoolean("wallWifi", true)


            val builder = Constraints.Builder()
                    .setRequiredNetworkType(if (isWifi) NetworkType.UNMETERED else NetworkType.CONNECTED)
                    .build()

            // start wallpaper worker
            val uploadWorkRequest =
                    PeriodicWorkRequestBuilder<AutoWallpaper>(time, TimeUnit.MINUTES)
                            .setConstraints(builder)
                            .build()

            WorkManager.getInstance(context!!).enqueue(uploadWorkRequest)
            Prefs.putAny(AUTO_WALLPAPER, uploadWorkRequest.id.toString())

            // interval naming
            val timing = when (time) {
                15.toLong() -> "15 min"
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

            context!!.toast("please wait few minutes for wallpapers to download on device")
        }
    }
}