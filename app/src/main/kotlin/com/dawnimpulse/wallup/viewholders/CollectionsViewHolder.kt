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
package com.dawnimpulse.wallup.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.inflator_collection_layout.view.*


/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-08 by Saksham
 *
 * @note Updates :
 */
class CollectionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title = itemView.colLTitle
    val image0 = itemView.colLImage0
    val image1 = itemView.colLImage1
    val image2 = itemView.colLImage2
    val image3 = itemView.colLImage3
    val count = itemView.colLImageC
    val artist = itemView.colLArtist
    val layout = itemView.colLMainL
}