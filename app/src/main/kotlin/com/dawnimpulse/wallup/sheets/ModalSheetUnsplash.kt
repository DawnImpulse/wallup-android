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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Config
import com.dawnimpulse.wallup.utils.F
import kotlinx.android.synthetic.main.bottom_sheet_unsplash.*


/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-10-03 by Saksham
 *
 * @note Updates :
 */
class ModalSheetUnsplash : RoundedBottomSheetDialogFragment() {

    // on create
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_unsplash, container, false);

    }

    // view created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        unsplashLogin.setOnClickListener {
            F.startWeb(context!!, "${C.UNSPLASH_OAUTH}" +
                    "?client_id=${Config.UNSPLASH_API_KEY.replace("Client-ID ", "")}" +
                    "&redirect_uri=${C.REDIRECT}" +
                    "&response_type=code" +
                    "&scope=public+read_user+write_user+read_photos+write_photos+write_likes+write_followers+read_collections+write_collections")
            dismiss()
        }
    }

}