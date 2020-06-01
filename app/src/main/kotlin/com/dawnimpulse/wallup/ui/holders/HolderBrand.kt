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
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.objects.ObjectBrand
import com.dawnimpulse.wallup.utils.reusables.imageTransform
import kotlinx.android.synthetic.main.holder_brand.view.*

class HolderBrand (view:View) : RecyclerView.ViewHolder(view){
    private val logo = view.holder_brand_logo
    private val image = view.holder_brand_bg

    /**
     * bind data to view
     *
     * @param brand
     */
    fun bind(brand: ObjectBrand){
        brand.logo.imageTransform(logo).apply()
        brand.cover.imageTransform(image)
                .height(360)
                .apply()
    }
}