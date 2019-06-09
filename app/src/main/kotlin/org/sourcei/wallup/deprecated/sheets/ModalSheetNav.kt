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

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.bottom_sheet_navigation.*
import org.sourcei.wallup.deprecated.BuildConfig
import org.sourcei.wallup.deprecated.R
import org.sourcei.wallup.deprecated.utils.F
import org.sourcei.wallup.deprecated.utils.RemoteConfig


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
class ModalSheetNav : org.sourcei.wallup.deprecated.sheets.RoundedBottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var sheet: org.sourcei.wallup.deprecated.sheets.ModalSheetUnsplash

    // on create
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_navigation, container, false);
    }

    // view created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sheet = org.sourcei.wallup.deprecated.sheets.ModalSheetUnsplash()

        sheetNavFeedback.setOnClickListener(this)
        sheetNavCollection.setOnClickListener(this)
        sheetNavAbout.setOnClickListener(this)
        sheetNavSettings.setOnClickListener(this)

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
            sheetNavCollection.id -> {
                startActivity(Intent(activity, org.sourcei.wallup.deprecated.activities.CollectionLayoutActivity::class.java))
                dismiss()
            }
            sheetNavAbout.id -> {
                startActivity(Intent(activity, org.sourcei.wallup.deprecated.activities.AboutActivity::class.java))
                dismiss()
            }
            sheetNavFeedback.id -> F.sendMail(activity!!)
            sheetNavUpdateL.id -> F.startWeb(context!!, org.sourcei.wallup.deprecated.utils.C.WALLUP_PLAY)
            sheetNavSettings.id -> startActivity(Intent(context, org.sourcei.wallup.deprecated.activities.SettingsActivity::class.java))
        }
    }
}