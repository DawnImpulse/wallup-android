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
import com.dawnimpulse.wallup.pojo.LibraryPojo
import com.dawnimpulse.wallup.utils.F
import com.dawnimpulse.wallup.utils.gone
import com.dawnimpulse.wallup.utils.show
import kotlinx.android.synthetic.main.inflator_library.view.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2018-12-15 by Saksham
 * @note Updates :
 */
class LibraryAdapter(val list: List<LibraryPojo>) : RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>() {
    private val NAME = "LibraryAdapter"
    private lateinit var context: Context

    // item count
    override fun getItemCount(): Int {
        return list.size
    }

    // on create
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        context = parent.context
        return LibraryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_library, parent, false))
    }

    // on bind view
    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        val library = list[position]
        holder.name.text = library.name
        holder.info.text = library.info
        holder.layout.setOnClickListener {
            F.startWeb(context, library.link)
        }
        if(position == 0)
            holder.heading.show()
        else
            holder.heading.gone()
    }

    // library view holder
    class LibraryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.libraryName
        val info = view.libraryInfo
        val layout = view.library
        val heading = view.libraryHeading
    }

}