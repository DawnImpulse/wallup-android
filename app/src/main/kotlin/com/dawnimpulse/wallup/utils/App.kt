package com.dawnimpulse.wallup.utils

import android.app.Application
import android.util.Log
import com.dawnimpulse.wallup.BuildConfig
import com.dawnimpulse.wallup.R
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

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

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-05-13 by Saksham
 *
 * @note Updates :
 */
class App : Application() {

    /**
     * On Create
     */
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        setUpRemoteConfig()
    }

    /**
     * Setup Firebase Remote Config
     */
    private fun setUpRemoteConfig() {
        var cacheExpiration: Long = 3600 // 1 hour in seconds.
        val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()

        if (mFirebaseRemoteConfig.info.configSettings.isDeveloperModeEnabled) {
            cacheExpiration = 0
        }

        Log.d("Test",mFirebaseRemoteConfig.getString(C.UNSPLASH_API_KEY))
        Log.d("Test","Here")
        mFirebaseRemoteConfig.setConfigSettings(configSettings)
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_defaults)
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        mFirebaseRemoteConfig.activateFetched()
                        Config.UNSPLASH_API_KEY = mFirebaseRemoteConfig.getString(C.UNSPLASH_API_KEY)
                        Log.d("Test",Config.UNSPLASH_API_KEY)
                    } else
                        Log.d("Test", "Fetch failed")
                }

    }
}