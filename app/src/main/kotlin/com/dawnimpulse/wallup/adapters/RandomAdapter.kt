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
package com.dawnimpulse.wallup.adapters

import android.arch.lifecycle.Lifecycle
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.activities.ArtistProfileActivity
import com.dawnimpulse.wallup.activities.ImageActivity
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Config
import com.dawnimpulse.wallup.viewholders.MainViewHolder
import com.google.gson.Gson

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-01 by Saksham
 *
 * @note Updates :
 */
class RandomAdapter(private val lifecycle: Lifecycle, private val images: List<ImagePojo?>)
    : RecyclerView.Adapter<MainViewHolder>() {

    private lateinit var context: Context

    /**
     * get total no of items for adapter
     */
    override fun getItemCount(): Int {
        return images.size
    }

    /**
     * create view holder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        context = parent.context
        return MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_main_layout, parent, false))
    }

    /**
     * binding view holder
     */
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        var artistClick = View.OnClickListener {
            var intent = Intent(context, ArtistProfileActivity::class.java)
            intent.putExtra(C.USERNAME, images[position]!!.user!!.username)
            context.startActivity(intent)
        }

        ImageHandler.setImageInView(lifecycle, holder.image, images[position]!!.urls!!.full + Config.IMAGE_HEIGHT)
        ImageHandler.setImageInView(lifecycle, holder.circleImage, images[position]!!.user!!.profile_image!!.large)
        holder.image.background = ColorDrawable(Color.parseColor(images[position]!!.color!!))
        holder.name.text = images[position]!!.user!!.name

        holder.image.setOnClickListener {
            var intent = Intent(context, ImageActivity::class.java)
            intent.putExtra(C.TYPE, "")
            intent.putExtra(C.IMAGE_POJO, Gson().toJson(images[position]))
            (context as AppCompatActivity).startActivity(intent)
        }
        holder.name.setOnClickListener(artistClick)
        holder.circleImage.setOnClickListener(artistClick)
    }
}