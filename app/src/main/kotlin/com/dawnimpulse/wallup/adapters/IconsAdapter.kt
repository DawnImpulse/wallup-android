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
            F.startWeb(context,icon.link)
        }
        holder.material.setOnClickListener {
            F.startWeb(context,"https://materialdesignicons.com")
        }

        if(position == 0)
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
    }

}