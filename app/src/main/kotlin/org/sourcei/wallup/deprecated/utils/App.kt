/*
ISC License

Copyright 2018-2019, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/
package org.sourcei.wallup.deprecated.utils

import android.app.Application
import android.content.ContextWrapper
import android.os.Environment
import com.crashlytics.android.Crashlytics
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.pixplicity.easyprefs.library.Prefs
import io.fabric.sdk.android.Fabric
import org.sourcei.wallup.deprecated.BuildConfig
import org.sourcei.wallup.deprecated.R
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-05-13 by Saksham
 *
 * @note Updates :
 * Saksham - 2018 07 22 - recent -
 */
class App : Application() {
    private val NAME = "App"

    // on create
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        setFonts()
        setPrefs()
        analytics()
        org.sourcei.wallup.deprecated.utils.F.connectivityListener(this)
        mkdir()
    }

    // set fonts
    private fun setFonts() {
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("font/product_sans.xml")
                .setFontAttrId(R.attr.fontPath)
                .build())
    }

    // set shared preferences
    private fun setPrefs() {
        Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(packageName)
                .setUseDefaultSharedPreference(true)
                .build()

        if (Prefs.contains(org.sourcei.wallup.deprecated.utils.C.USER_TOKEN))
            org.sourcei.wallup.deprecated.utils.Config.USER_API_KEY = Prefs.getString(org.sourcei.wallup.deprecated.utils.C.USER_TOKEN, "")

        org.sourcei.wallup.deprecated.utils.Config.IMAGE_LIST_QUALITY = Prefs.getString(org.sourcei.wallup.deprecated.utils.C.IMAGE_LIST_QUALITY, org.sourcei.wallup.deprecated.utils.Config.IMAGE_LIST_QUALITY)
        org.sourcei.wallup.deprecated.utils.Config.IMAGE_PREVIEW_QUALITY = Prefs.getString(org.sourcei.wallup.deprecated.utils.C.IMAGE_PREVIEW_QUALITY, org.sourcei.wallup.deprecated.utils.Config.IMAGE_PREVIEW_QUALITY)
        org.sourcei.wallup.deprecated.utils.Config.IMAGE_DOWNLOAD_QUALITY = Prefs.getString(org.sourcei.wallup.deprecated.utils.C.IMAGE_DOWNLOAD_QUALITY, org.sourcei.wallup.deprecated.utils.Config.IMAGE_DOWNLOAD_QUALITY)

        org.sourcei.wallup.deprecated.utils.L.d(NAME, org.sourcei.wallup.deprecated.utils.Config.USER_API_KEY)
    }

    // enabling crashlytics in release builds
    private fun analytics() {
        if (!BuildConfig.DEBUG) {
            if (Prefs.getBoolean(org.sourcei.wallup.deprecated.utils.C.CRASHLYTICS, true))
                Fabric.with(this, Crashlytics())
            FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(Prefs.getBoolean(org.sourcei.wallup.deprecated.utils.C.ANALYTICS, true))
        }
    }

    //make dir
    private fun mkdir() {
        if (Environment.getExternalStorageDirectory().exists()) {
            org.sourcei.wallup.deprecated.utils.L.d(NAME, Environment.getExternalStorageDirectory())
            if (!org.sourcei.wallup.deprecated.utils.Config.DEFAULT_DOWNLOAD_PATH.toFile().exists())
                org.sourcei.wallup.deprecated.utils.Config.DEFAULT_DOWNLOAD_PATH.toFile().mkdir()
        }
    }
}