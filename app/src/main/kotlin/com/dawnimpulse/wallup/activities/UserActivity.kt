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

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.toast
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.UserPojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Config
import com.dawnimpulse.wallup.utils.Dialog
import com.dawnimpulse.wallup.utils.L
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_user.*

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-10-04 by Saksham
 *
 * @note Updates :
 */
class UserActivity : AppCompatActivity() {
    private val NAME = "UserActivity"
    private lateinit var model: UnsplashModel

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        // fetching details from prefs
        if (Prefs.contains(C.USER))
            setDetails(Gson().fromJson(Prefs.getString(C.USER, ""), UserPojo::class.java))

        // fetching details from unsplash
        model = UnsplashModel(lifecycle)
        model.selfProfile() { e, r ->
            e?.let {
                L.d(NAME, e)
                toast("error fetching profile")
            }
            r?.let {
                Prefs.putString(C.USER, Gson().toJson(it))
                setDetails(it as UserPojo)
            }

        }

        // logout user
        logoutL.setOnClickListener {
            Dialog.simpleOk(this,
                    "User Profile Logout",
                    "Wish to logout from your profile ?",
                    DialogInterface.OnClickListener { dialog, _ ->
                        Prefs.remove(C.USER_TOKEN)
                        Prefs.remove(C.USER)
                        Config.USER_API_KEY = ""
                        dialog.dismiss()
                        toast("Successfully logout from your profile")
                        finish()
                    })
        }
    }

    //set user details
    private fun setDetails(user: UserPojo) {
        userFullName.text = user.name
        userName.text = "@${user.username}"
        ImageHandler.setImageInView(lifecycle, userImage, user.profile_image.large)
        ImageHandler.setImageInView(lifecycle, userBg, "${C.UNSPLASH_SOURCE}/user/${user.username}/720x1280")
    }
}
