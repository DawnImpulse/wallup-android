/**
 * ISC License
 *
 * Copyright 2018-2019, Saksham (DawnImpulse)
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
package com.dawnimpulse.wallup.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.holders.BannerHomescreenHolder
import com.dawnimpulse.wallup.ui.objects.WallupCollectionHomescreenObject

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-12 by Saksham
 * @note Updates :
 */
class BannerHomescreenAdapter(
        val items: List<WallupCollectionHomescreenObject>
) : RecyclerView.Adapter<BannerHomescreenHolder>() {

    // --------------------
    //      get count
    // --------------------
    override fun getItemCount(): Int {
        return items.size
    }

    // --------------------
    //      create
    // --------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerHomescreenHolder {
        return BannerHomescreenHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_homescreen_collection, parent, false))
    }

    // --------------------
    //      bind
    // --------------------
    override fun onBindViewHolder(holder: BannerHomescreenHolder, position: Int) {
        holder.bind(items[position])
    }

}