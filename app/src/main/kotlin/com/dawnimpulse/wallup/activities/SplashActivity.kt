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
package com.dawnimpulse.wallup.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dawnimpulse.wallup.BuildConfig
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.openActivity
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-02 by Saksham
 *
 * @note Updates :
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()

        launch {
            delay(2000)

            if (Prefs.contains(C.VERSION_CODE)) {
                when {
                    Prefs.getInt(C.VERSION_CODE, 10) == BuildConfig.VERSION_CODE ->
                        this@SplashActivity.openActivity(MainActivity::class.java)
                    else -> this@SplashActivity.openActivity(ChangesActivity::class.java) {
                        putBoolean(C.TYPE, true)
                    }
                }
            } else
                startActivity(Intent(this@SplashActivity, IntroActivity::class.java))

            Prefs.putInt(C.VERSION_CODE, BuildConfig.VERSION_CODE)
            finish()
        }
    }
}
