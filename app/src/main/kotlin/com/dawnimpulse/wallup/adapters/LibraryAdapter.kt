package com.dawnimpulse.wallup.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.pojo.LibraryPojo
import com.dawnimpulse.wallup.utils.F
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
class LibraryAdapter(
        val list: List<LibraryPojo>
) : RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>() {
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
    }

    // library view holder
    class LibraryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.libraryName
        val info = view.libraryInfo
        val layout = view.library
    }

}