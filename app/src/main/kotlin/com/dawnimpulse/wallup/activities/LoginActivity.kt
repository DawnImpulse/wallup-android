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
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Config
import com.dawnimpulse.wallup.utils.F
import com.dawnimpulse.wallup.utils.L
import kotlinx.android.synthetic.main.activity_login.*

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
                toast("making next call")
                val model = UnsplashModel(lifecycle)
                val body = BearerBody(Config.UNSPLASH_API_KEY.replace("Client-ID ",""), Config.UNSPLASH_SECRET, C.REDIRECT, code, "authorization_code")
                //val body = BearerBody(Config.UNSPLASH_API_KEY.replace("Client-ID ",""), Config.UNSPLASH_SECRET, C.REDIRECT, "7cf79952614bca27b93f8e1812878a52f9347d2ac2f4898ffb54cf17ccdd7eea", "authorization_code")
                model.bearerToken(body) { e, r ->
                    L.dO(NAME,body)
                    e?.let {
                        L.d(NAME,e)
                        toast(e.toString())
                    }
                    r?.let {
                        L.dO(NAME, r)
                    }

                }
            }
        }
        loginButton.setOnClickListener {
            F.startWeb(this, "${C.UNSPLASH_OAUTH}" +
                    "?client_id=${Config.UNSPLASH_API_KEY.replace("Client-ID ", "")}" +
                    "&redirect_uri=${C.REDIRECT}" +
                    "&response_type=code" +
                    "&scope=public+read_user+write_user+read_photos+write_photos+write_likes+write_followers+read_collections+write_collections")
        }
    }
}
