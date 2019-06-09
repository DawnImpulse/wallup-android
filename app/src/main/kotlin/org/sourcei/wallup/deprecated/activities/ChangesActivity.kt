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
package org.sourcei.wallup.deprecated.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_changes.*
import org.sourcei.wallup.deprecated.R


/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-02 by Saksham
 *
 * @note Updates :
 */
class ChangesActivity : AppCompatActivity(), View.OnClickListener {

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_changes)
        changesFab.setOnClickListener(this)
    }

    // on click
    override fun onClick(v: View) {
        when (v.id) {
            changesFab.id -> {
                if (intent.extras != null)
                    startActivity(Intent(this, org.sourcei.wallup.deprecated.activities.MainActivity::class.java))
                finish()
            }
        }
    }
}
