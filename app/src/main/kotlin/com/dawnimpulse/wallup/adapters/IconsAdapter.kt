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
package com.dawnimpulse.wallup.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.pojo.IconsPojo
import com.dawnimpulse.wallup.utils.F
import com.dawnimpulse.wallup.utils.gone
import com.dawnimpulse.wallup.utils.show
import kotlinx.android.synthetic.main.inflator_icons.view.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2018-12-16 by Saksham
 * @note Updates :
 */
class IconsAdapter(val list: List<IconsPojo>) : RecyclerView.Adapter<IconsAdapter.IconsViewHolder>() {
    private val NAME = "IconsAdapter"
    private lateinit var context: Context

    // item count
    override fun getItemCount(): Int {
        return list.size
    }

    // on create
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconsAdapter.IconsViewHolder {
        context = parent.context
        return IconsAdapter.IconsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_icons, parent, false))
    }

    // on bind view
    override fun onBindViewHolder(holder: IconsViewHolder, position: Int) {
        val icon = list[position]
        holder.author.text = icon.author
        holder.website.text = icon.website
        holder.icon.setImageDrawable(icon.icon)
        holder.layout.setOnClickListener {
            F.startWeb(context, icon.link)
        }
        holder.material.setOnClickListener {
            F.startWeb(context, "https://materialdesignicons.com")
        }
        holder.google.setOnClickListener {
            F.startWeb(context, "https://material.io/tools/icons/")
        }
        if (position == 0)
            holder.heading.show()
        else
            holder.heading.gone()
    }

    // library view holder
    class IconsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val heading = view.iconsHeading
        val layout = view.iconsLayout
        val icon = view.icon
        val author = view.iconAuthor
        val website = view.iconWebsite
        val material = view.iconsMaterial
        val google = view.iconsGoogle
    }

}