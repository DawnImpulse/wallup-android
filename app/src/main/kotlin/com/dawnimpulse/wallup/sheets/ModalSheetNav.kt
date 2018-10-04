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
package com.dawnimpulse.wallup.sheets

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.dawnimpulse.wallup.BuildConfig
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.activities.AboutActivity
import com.dawnimpulse.wallup.activities.CollectionLayoutActivity
import com.dawnimpulse.wallup.activities.GeneralImagesActivity
import com.dawnimpulse.wallup.activities.UserActivity
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.F
import com.dawnimpulse.wallup.utils.RemoteConfig
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.bottom_sheet_navigation.*


/**
 * @author Saksham
 *
 * @note Last Branch Update -
 * @note Created on 2018-08-19 by Saksham
 *
 * @note Updates :
 * Saksham - 2018 09 15 - master - update handling
 * Saksham - 2018 10 04 - master - user
 */
class ModalSheetNav : RoundedBottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var sheet: ModalSheetUnsplash

    // on create
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_navigation, container, false);
    }

    // view created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sheet = ModalSheetUnsplash()

        sheetNavRandom.setOnClickListener(this)
        sheetNavFeedback.setOnClickListener(this)
        sheetNavCollection.setOnClickListener(this)
        sheetNavAbout.setOnClickListener(this)
        sheetNavUser.setOnClickListener(this)

        RemoteConfig.getProductionUpdateValues()?.let {
            if (it.next_version_code > BuildConfig.VERSION_CODE) {
                sheetNavUpdateL.visibility = View.VISIBLE
                sheetNavUpdateL.setOnClickListener(this)
            } else if (it.text_available) {
                sheetNavNextUpdate.visibility = View.VISIBLE
                sheetNavNextUpdate.text = it.text
            }
        }
    }

    // clicked
    override fun onClick(v: View) {
        when (v.id) {
            sheetNavRandom.id -> {
                var intent = Intent(activity, GeneralImagesActivity::class.java)
                intent.putExtra(C.TYPE, C.RANDOM)
                startActivity(intent)
                dismiss()
            }
            sheetNavCollection.id -> {
                startActivity(Intent(activity, CollectionLayoutActivity::class.java))
                dismiss()
            }
            sheetNavAbout.id -> {
                startActivity(Intent(activity, AboutActivity::class.java))
                dismiss()
            }
            sheetNavFeedback.id -> F.sendMail(activity!!)
            sheetNavUpdateL.id -> F.startWeb(context!!, C.WALLUP_PLAY)
            sheetNavUser.id -> {
                if (!Prefs.contains(C.USER_TOKEN))
                    sheet.show((context as AppCompatActivity).supportFragmentManager, sheet.tag)
                else
                    startActivity(Intent(context, UserActivity::class.java))

                dismiss()
            }
        }
    }
}