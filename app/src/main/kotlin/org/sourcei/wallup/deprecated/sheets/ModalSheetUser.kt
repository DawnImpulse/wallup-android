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
package org.sourcei.wallup.deprecated.sheets

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.bottom_sheet_user.*
import org.sourcei.wallup.deprecated.R
import org.sourcei.wallup.deprecated.utils.*


/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-12-19 by Saksham
 *
 * @note Updates :
 */
class ModalSheetUser : org.sourcei.wallup.deprecated.sheets.RoundedBottomSheetDialogFragment(), View.OnClickListener {
    private val NAME = "ModalSheetUser"
    private var user: org.sourcei.wallup.deprecated.pojo.UserPojo? = null
    private lateinit var model: org.sourcei.wallup.deprecated.models.UnsplashModel
    private lateinit var colSheet: org.sourcei.wallup.deprecated.sheets.ModalSheetCollection

    // on create
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_user, container, false)
    }

    // view created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // fetching details from prefs
        if (Prefs.contains(org.sourcei.wallup.deprecated.utils.C.USER))
            setDetails(Gson().fromJson(Prefs.getString(org.sourcei.wallup.deprecated.utils.C.USER, ""), org.sourcei.wallup.deprecated.pojo.UserPojo::class.java))

        // fetching details from unsplash
        model = org.sourcei.wallup.deprecated.models.UnsplashModel(lifecycle)
        colSheet = org.sourcei.wallup.deprecated.sheets.ModalSheetCollection()

        userDetails()

        userLogout.setOnClickListener(this)
        userPhotosL.setOnClickListener(this)
        userCollectionL.setOnClickListener(this)
        userLikesL.setOnClickListener(this)
        userReload.setOnClickListener(this)

    }

    // on click
    override fun onClick(v: View) {
        when (v.id) {
            userLogout.id -> {
                org.sourcei.wallup.deprecated.handlers.DialogHandler.simpleOk(context!!,
                        "User Profile Logout",
                        "Wish to logout from your profile ?",
                        DialogInterface.OnClickListener { dialog, _ ->
                            Prefs.remove(org.sourcei.wallup.deprecated.utils.C.USER_TOKEN)
                            Prefs.remove(org.sourcei.wallup.deprecated.utils.C.USER)
                            org.sourcei.wallup.deprecated.utils.Config.USER_API_KEY = ""
                            dialog.dismiss()
                            context!!.toast("Successfully logout from your profile")
                            dismiss()
                        })
            }
            userPhotosL.id -> context!!.openActivity(org.sourcei.wallup.deprecated.activities.GeneralImagesActivity::class.java) {
                putString(org.sourcei.wallup.deprecated.utils.C.TYPE, org.sourcei.wallup.deprecated.utils.C.ARTIST_IMAGES)
                putString(org.sourcei.wallup.deprecated.utils.C.USERNAME, user!!.username)
            }
            userCollectionL.id -> {
                colSheet.arguments = bundleOf(Pair(org.sourcei.wallup.deprecated.utils.C.VIEW, org.sourcei.wallup.deprecated.utils.C.VIEW))
                colSheet.show((context as AppCompatActivity).supportFragmentManager, colSheet.tag)
            }
            userLikesL.id -> {
                if (user != null) {
                    context!!.openActivity(org.sourcei.wallup.deprecated.activities.GeneralImagesActivity::class.java) {
                        putString(org.sourcei.wallup.deprecated.utils.C.TYPE, org.sourcei.wallup.deprecated.utils.C.LIKE)
                        putString(org.sourcei.wallup.deprecated.utils.C.USERNAME, user?.username)
                    }
                } else
                    context!!.toast( "kindly wait while loading user details.")
            }
            userReload.id -> userDetails()
        }
    }

    //user details
    private fun userDetails() {
        model.selfProfile() { e, r ->
            e?.let {
                L.d(NAME, e)
                context!!.toast("error fetching profile")
                userProgress.gone()
                userReload.show()
            }
            r?.let {
                Prefs.putString(org.sourcei.wallup.deprecated.utils.C.USER, Gson().toJson(it))
                setDetails(it as org.sourcei.wallup.deprecated.pojo.UserPojo)
            }

        }
    }

    //set user details
    private fun setDetails(user: org.sourcei.wallup.deprecated.pojo.UserPojo) {
        userProgress.gone()
        userLayout.show()

        this.user = user
        val grey = org.sourcei.wallup.deprecated.utils.Colors(context!!).GREY_400
        userFirstName.text = user.first_name
        userLastName.text = user.last_name
        org.sourcei.wallup.deprecated.handlers.ImageHandler.setImageInView(lifecycle, userImage, user.profile_image.large)

        userPhotos.text = F.fromHtml("${F.withSuffix(user.total_photos)} <font color=\"$grey\"> Photos</font>")
        userCollection.text = F.fromHtml("${F.withSuffix(user.total_collections)} <font color=\"$grey\"> Collections</font>")
        userLikes.text = F.fromHtml("${F.withSuffix(user.total_likes)} <font color=\"$grey\"> Likes</font>")
    }

}