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
import android.support.v7.app.AppCompatActivity
import com.dawnimpulse.wallup.BuildConfig
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.utils.C
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_changes.*


/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-02 by Saksham
 *
 * @note Updates :
 */
class ChangesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_changes)

        if (Prefs.contains(C.VERSION_CODE)) {
            if (Prefs.getInt(C.VERSION_CODE, 0) == BuildConfig.VERSION_CODE) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Prefs.putInt(C.VERSION_CODE, BuildConfig.VERSION_CODE)
            }
        } else
            Prefs.putInt(C.VERSION_CODE, BuildConfig.VERSION_CODE)

        changesFab.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
