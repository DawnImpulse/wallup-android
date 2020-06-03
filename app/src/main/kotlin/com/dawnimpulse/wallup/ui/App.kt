/**
 * ISC License
 *
 * Copyright 2020, Saksham (DawnImpulse)
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
package com.dawnimpulse.wallup.ui

import android.app.Application
import androidx.preference.PreferenceManager
import com.dawnimpulse.wallup.BuildConfig
import com.dawnimpulse.wallup.utils.reusables.Prefs
import com.dawnimpulse.wallup.utils.reusables.logd
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.ktx.database
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.orhanobut.hawk.Hawk

class App() : Application() {

    companion object {
        lateinit var context: Application
    }

    init {
        context = this
    }

    /**
     * on create handling
     */
    override fun onCreate() {
        super.onCreate()

        notification()
        analytics()
        Prefs = PreferenceManager.getDefaultSharedPreferences(this)
        Hawk.init(context).build();
        Firebase.database.setPersistenceEnabled(true)
    }

    /**
     * notification id for testing
     */
    private fun notification() {
        if (BuildConfig.DEBUG)
            FirebaseInstanceId.getInstance().instanceId
                    .addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            logd("getInstanceId failed")
                            return@OnCompleteListener
                        }

                        // Get new Instance ID token
                        val token = task.result?.token

                        // Log and toast
                        logd("fcm token : " + token!!)
                    })
    }

    /**
     * enabling analytics in release builds
     */
    private fun analytics() {
        if (!BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
            FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true)
        }
    }
}