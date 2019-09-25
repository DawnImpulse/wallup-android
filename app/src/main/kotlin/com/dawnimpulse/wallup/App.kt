/**
 * ISC License
 *
 * Copyright 2018-2019, Saksham (DawnImpulse)
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
 * WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
 * OR PERFORMANCE OF THIS SOFTWARE.
 **/
package com.dawnimpulse.wallup

import android.app.Application
import android.preference.PreferenceManager
import com.crashlytics.android.Crashlytics
import com.dawnimpulse.wallup.utils.functions.F
import com.dawnimpulse.wallup.utils.functions.putAny
import com.dawnimpulse.wallup.utils.reusables.*
import com.google.firebase.analytics.FirebaseAnalytics
import io.fabric.sdk.android.Fabric
import java.io.File

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - develop
 *
 * @note Created on 2019-06-10 by Saksham
 * @note Updates :
 *  Saksham - 2019 09 25 - develop - firebase messaging init
 */
class App : Application() {

    // on create
    override fun onCreate() {
        super.onCreate()

        Prefs = PreferenceManager.getDefaultSharedPreferences(this)

        // set analytics
        analytics()

        // default search terms
        if (!Prefs.contains("search"))
            Prefs.putAny("search", "nature,landscape")

        // default cache images
        if (!Prefs.contains(CACHE_NUMBER))
            Prefs.putAny(CACHE_NUMBER, "25")

        // make cached dir
        if (!File(filesDir, CACHED).exists())
            File(filesDir, CACHED).mkdir()

        F.removeDuplicates(filesDir.listFiles().toList())
    }

    // enabling crashlytics in release builds
    private fun analytics() {
        if (!BuildConfig.DEBUG) {
            if (Prefs.getBoolean(CRASHLYTICS, true))
                Fabric.with(this, Crashlytics())
            FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(Prefs.getBoolean(ANALYTICS, true))
        }
    }

}