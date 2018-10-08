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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.toast
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.BearerBody
import com.dawnimpulse.wallup.pojo.BearerToken
import com.dawnimpulse.wallup.pojo.UnsplashAuthError
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Config
import com.dawnimpulse.wallup.utils.L
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-10-01 by Saksham
 *
 * @note Updates :
 */
class LoginActivity : AppCompatActivity() {
    private val NAME = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        intent?.let {
            it.data?.let { uri ->
                val code = uri.getQueryParameter("code")
                val model = UnsplashModel(lifecycle)
                val body = BearerBody(
                        Config.UNSPLASH_API_KEY.replace("Client-ID ", ""),
                        Config.UNSPLASH_SECRET,
                        C.REDIRECT,
                        code,
                        "authorization_code")

                model.bearerToken(body) { e, r ->
                    e?.let {
                        L.dO(NAME, e)
                        toast((e as UnsplashAuthError).error)
                        finish()
                    }
                    r?.let {
                        it as BearerToken
                        toast("Logged in successfully")
                        Config.USER_API_KEY = "Bearer ${it.access_token}"
                        Prefs.putString(C.USER_TOKEN,Config.USER_API_KEY)
                        model.selfProfile() { e, r ->
                            e?.let {
                                L.d(NAME, e)
                                toast("error fetching profile")
                            }
                            r?.let {
                                Prefs.putString(C.USER, Gson().toJson(it))
                            }
                        }
                        finish()
                    }

                }
            }
        }
    }
}
