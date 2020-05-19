/**
 * ISC License
 *
 * Copyright 2020, Saksham (DawnImpulse)
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
 * WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
 * OR PERFORMANCE OF THIS SOFTWARE.
 **/
package com.dawnimpulse.wallup.ui.holders

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.auth.AuthGoogle
import com.dawnimpulse.wallup.ui.sheets.SheetUser
import com.dawnimpulse.wallup.utils.reusables.AUTH
import com.dawnimpulse.wallup.utils.reusables.openActivity
import kotlinx.android.synthetic.main.adapter_home_header.view.*

class HolderHomeHeader(view: View) : RecyclerView.ViewHolder(view) {
    private val user = view.activity_home_header_user
    private val context = view.context
    private var sheetUser: SheetUser = SheetUser()

    init {
        user.setOnClickListener {
            sheetUser.show((context as AppCompatActivity).supportFragmentManager, sheetUser.tag)
        }
    }
}