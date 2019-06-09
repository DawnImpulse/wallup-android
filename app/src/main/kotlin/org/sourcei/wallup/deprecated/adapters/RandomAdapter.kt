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
package org.sourcei.wallup.deprecated.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import org.sourcei.wallup.deprecated.R
import org.sourcei.wallup.deprecated.utils.F
import org.sourcei.wallup.deprecated.viewholders.MainViewHolder

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-01 by Saksham
 *
 * @note Updates :
 *  Saksham - 2018 12 21 - master - fix issue collection add
 */
class RandomAdapter(private val lifecycle: Lifecycle, private val images: List<org.sourcei.wallup.deprecated.pojo.ImagePojo?>)
    : RecyclerView.Adapter<MainViewHolder>() {

    private lateinit var context: Context

    // get total no of items for adapter
    override fun getItemCount(): Int {
        return images.size
    }

    // create view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        context = parent.context
        return MainViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_main_layout, parent, false))
    }

    // binding view holder
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        var image = images[position]!!
        var artistClick = View.OnClickListener {
            var intent = Intent(context, org.sourcei.wallup.deprecated.activities.ArtistProfileActivity::class.java)
            intent.putExtra(org.sourcei.wallup.deprecated.utils.C.USERNAME, images[position]!!.user!!.username)
            context.startActivity(intent)
        }

        // setting image in view
        org.sourcei.wallup.deprecated.handlers.ImageHandler.setImageInView(lifecycle, holder.image, images[position]!!.urls!!.full + org.sourcei.wallup.deprecated.utils.Config.IMAGE_LIST_QUALITY)
        // setting artist image
        org.sourcei.wallup.deprecated.handlers.ImageHandler.setImageInView(lifecycle, holder.circleImage, images[position]!!.user!!.profile_image!!.large)

        holder.image.background = ColorDrawable(Color.parseColor(images[position]!!.color!!))
        holder.name.text = F.capWord(images[position]!!.user!!.name)

        // setting the like button
        F.like(context, holder.like, false, image.liked_by_user)

        // open image details
        holder.image.setOnClickListener {
            var intent = Intent(context, org.sourcei.wallup.deprecated.activities.ImageActivity::class.java)
            intent.putExtra(org.sourcei.wallup.deprecated.utils.C.TYPE, "")
            intent.putExtra(org.sourcei.wallup.deprecated.utils.C.POSITION, position)
            intent.putExtra(org.sourcei.wallup.deprecated.utils.C.IMAGE_POJO, Gson().toJson(images[position]))
            (context as AppCompatActivity).startActivity(intent)
        }

        // like an image
        holder.likeL.setOnClickListener {
            // checking if user is logged
            if (org.sourcei.wallup.deprecated.utils.Config.USER_API_KEY.isNotEmpty()) {
                //firing image liked event
                var obj = JSONObject()
                obj.put(org.sourcei.wallup.deprecated.utils.C.TYPE, org.sourcei.wallup.deprecated.utils.C.LIKE)
                obj.put(org.sourcei.wallup.deprecated.utils.C.LIKE, !image.liked_by_user)
                obj.put(org.sourcei.wallup.deprecated.utils.C.ID, image.id)
                EventBus.getDefault().post(org.sourcei.wallup.deprecated.utils.Event(obj))
            }
            // opening login sheet if user not logged in
            else {
                var sheet = org.sourcei.wallup.deprecated.sheets.ModalSheetUnsplash()
                sheet.show((context as AppCompatActivity).supportFragmentManager, sheet.tag)
            }
        }

        // open artist page
        holder.name.setOnClickListener(artistClick)
        holder.circleImage.setOnClickListener(artistClick)
    }
}