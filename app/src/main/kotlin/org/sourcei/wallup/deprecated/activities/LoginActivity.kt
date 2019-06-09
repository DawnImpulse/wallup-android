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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import org.sourcei.wallup.deprecated.BuildConfig
import org.sourcei.wallup.deprecated.R
import org.sourcei.wallup.deprecated.utils.L
import org.sourcei.wallup.deprecated.utils.toast

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
                val model = org.sourcei.wallup.deprecated.models.UnsplashModel(lifecycle)

                // if some issue with code or anything else
                if (code == null || code.isEmpty()) {
                    toast("Issue while authenticating with Unsplash. Please try again.")
                    finish()
                } else {
                    val body = org.sourcei.wallup.deprecated.pojo.BearerBody(
                            BuildConfig.UNSPLASH_API_KEY.replace("Client-ID ", ""),
                            BuildConfig.UNSPLASH_API_SECRET,
                            org.sourcei.wallup.deprecated.utils.C.REDIRECT,
                            code,
                            "authorization_code")

                    model.bearerToken(body) { e, r ->
                        e?.let {
                            L.d(NAME, Gson().toJson(it))
                            toast("issue with user verification from Unsplash, kindly retry.")
                            finish()
                        }
                        r?.let {
                            it as org.sourcei.wallup.deprecated.pojo.BearerToken
                            toast("Logged in successfully")
                            org.sourcei.wallup.deprecated.utils.Config.USER_API_KEY = "Bearer ${it.access_token}"
                            Prefs.putString(org.sourcei.wallup.deprecated.utils.C.USER_TOKEN, org.sourcei.wallup.deprecated.utils.Config.USER_API_KEY)
                            model.selfProfile() { e, r ->
                                e?.let {
                                    L.d(NAME, e)
                                    finish()
                                }
                                r?.let {
                                    Prefs.putString(org.sourcei.wallup.deprecated.utils.C.USER, Gson().toJson(it))
                                    finish()
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}
