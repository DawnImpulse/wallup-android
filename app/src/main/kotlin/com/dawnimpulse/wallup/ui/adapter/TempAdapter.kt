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
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import kotlinx.android.synthetic.main.inflator_test.view.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-17 by Saksham
 * @note Updates :
 */
class TempAdapter(val items: List<String>, val internal: Boolean = false)
    : RecyclerView.Adapter<abc>() {


    // --------------
    //    count
    // --------------
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): abc {
        return abc(LayoutInflater.from(parent.context).inflate(if (!internal) R.layout.inflator_test else R.layout.inflator_test2, parent, false))
    }

    override fun onBindViewHolder(holder: abc, position: Int) {
        holder.text.text = position.toString()
        if (position == 2 && !internal)
            holder.bind()
    }

}

class abc(val view: View) : RecyclerView.ViewHolder(view) {
    val text = view.demo
    val rec = view.demoR
    val context = view.context
    private lateinit var snapHelper: PagerSnapHelper

    fun bind() {
        if (!::snapHelper.isInitialized) {
            snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(rec)
            val list = (11..20).map { it.toString() }.toMutableList()
            rec.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rec.adapter = TempAdapter(list, true)
        }
    }
}