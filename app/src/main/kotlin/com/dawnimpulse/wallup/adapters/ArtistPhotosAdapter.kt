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
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.activities.ImageActivity
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Config
import com.dawnimpulse.wallup.viewholders.ArtistPhotosHolder
import com.google.gson.Gson

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-08-31 by Saksham
 *
 * @note Updates :
 */
class ArtistPhotosAdapter(private val context: Context, private val lifecycle: Lifecycle,
                          private val images: List<ImagePojo?>) : RecyclerView.Adapter<ArtistPhotosHolder>() {

    override fun getItemCount(): Int {
        if (images.isNotEmpty()) {
            if (images.size < 8) {
                if (images.size < 2)
                    return 1
                if (images.size < 4)
                    return 2
                if (images.size < 7)
                    return 3
            } else
                return 4
        }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistPhotosHolder {
        return ArtistPhotosHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_artist_photos, parent, false))
    }

    override fun onBindViewHolder(holder: ArtistPhotosHolder, position: Int) {
        var intent = Intent(context, ImageActivity::class.java)

        when (position) {
            0 -> {
                holder.layout0.visibility = View.VISIBLE
                ImageHandler.setImageInView(lifecycle, holder.image1, images[0]!!.urls!!.full + Config.IMAGE_HEIGHT)
            }
            1 -> {
                holder.layout1.visibility = View.VISIBLE
                if (images.size > 1) ImageHandler.setImageInView(lifecycle, holder.image2, images[1]!!.urls!!.full + Config.IMAGE_HEIGHT) else holder.image2L.visibility = View.GONE
                if (images.size > 2) ImageHandler.setImageInView(lifecycle, holder.image3, images[2]!!.urls!!.full + Config.IMAGE_HEIGHT) else holder.image3L.visibility = View.GONE
                if (images.size > 3) ImageHandler.setImageInView(lifecycle, holder.image4, images[3]!!.urls!!.full + Config.IMAGE_HEIGHT) else holder.image4L.visibility = View.GONE
            }
            2 -> {
                holder.layout2.visibility = View.VISIBLE
                if (images.size > 4) ImageHandler.setImageInView(lifecycle, holder.image5, images[4]!!.urls!!.full + Config.IMAGE_HEIGHT) else holder.image5L.visibility = View.GONE
                if (images.size > 5) ImageHandler.setImageInView(lifecycle, holder.image6, images[5]!!.urls!!.full + Config.IMAGE_HEIGHT) else holder.image6L.visibility = View.GONE
                if (images.size > 6) ImageHandler.setImageInView(lifecycle, holder.image7, images[6]!!.urls!!.full + Config.IMAGE_HEIGHT) else holder.image7L.visibility = View.GONE
            }
            3 -> {
                holder.layout0.visibility = View.VISIBLE
                ImageHandler.setImageInView(lifecycle, holder.image1, images[7]!!.urls!!.full + Config.IMAGE_HEIGHT)
            }
        }

        holder.image1.setOnClickListener {
            if (position === 0)
                intent.putExtra(C.IMAGE_POJO, Gson().toJson(images[0]))
            else
                intent.putExtra(C.IMAGE_POJO, Gson().toJson(images[7]))
            context.startActivity(intent)
        }

        holder.image2.setOnClickListener {
            intent.putExtra(C.IMAGE_POJO, Gson().toJson(images[1]))
            context.startActivity(intent)
        }

        holder.image3.setOnClickListener {
            intent.putExtra(C.IMAGE_POJO, Gson().toJson(images[2]))
            context.startActivity(intent)
        }

        holder.image4.setOnClickListener {
            intent.putExtra(C.IMAGE_POJO, Gson().toJson(images[3]))
            context.startActivity(intent)
        }

        holder.image5.setOnClickListener {
            intent.putExtra(C.IMAGE_POJO, Gson().toJson(images[4]))
            context.startActivity(intent)
        }

        holder.image6.setOnClickListener {
            intent.putExtra(C.IMAGE_POJO, Gson().toJson(images[5]))
            context.startActivity(intent)
        }

        holder.image7.setOnClickListener {
            intent.putExtra(C.IMAGE_POJO, Gson().toJson(images[6]))
            context.startActivity(intent)
        }
    }

}