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

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.activities.ArtistProfileActivity
import com.dawnimpulse.wallup.activities.ImageActivity
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.sheets.ModalSheetUnsplash
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Config
import com.dawnimpulse.wallup.utils.Event
import com.dawnimpulse.wallup.utils.F
import com.dawnimpulse.wallup.viewholders.LoadingViewHolder
import com.dawnimpulse.wallup.viewholders.MainViewHolder
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

/**
 * @author Saksham
 *
 * @note Last Branch Update - recent
 * @note Created on 2018-05-16 by Saksham
 *
 * @note Updates :
 *  Saksham - 2018 05 25 - recent - intent to Image Activity
 */
class MainAdapter(
        private val lifecycle: Lifecycle,
        private val images: List<ImagePojo?>,
        recycler: RecyclerView,
        private val showLike: Boolean = true)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val NAME = "MainAdapter"
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private val visibleThreshold = 5
    private var isLoading: Boolean = false
    private var loadMoreListener: OnLoadMoreListener? = null
    private var VIEW_TYPE_LOADING = 0
    private var VIEW_TYPE_ITEM = 1
    private lateinit var context: Context

    // initialization for Load More Listener
    init {
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val mLinearLayoutManager = recyclerView!!.layoutManager as LinearLayoutManager

                totalItemCount = mLinearLayoutManager.itemCount
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition()

                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                    if (loadMoreListener != null) {
                        loadMoreListener!!.onLoadMore()
                    }
                    isLoading = true
                }
            }
        })
    }

    // get total no of items for adapter
    override fun getItemCount(): Int {
        return images.size
    }

    // get item view type
    override fun getItemViewType(position: Int): Int {
        return if (images.elementAtOrNull(position) == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    // create view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View
        context = parent.context
        return if (viewType == VIEW_TYPE_ITEM) {
            v = LayoutInflater.from(parent.context).inflate(R.layout.inflator_main_layout, parent, false)
            MainViewHolder(v)
        } else {
            v = LayoutInflater.from(parent.context).inflate(R.layout.inflator_loading, parent, false)
            LoadingViewHolder(v)
        }
    }

    // binding view holder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainViewHolder) {
            var image = images[position]!!
            var artistClick = View.OnClickListener {
                var intent = Intent(context, ArtistProfileActivity::class.java)
                intent.putExtra(C.USERNAME, images[position]!!.user!!.username)
                context.startActivity(intent)
            }

            // show/hide like button
            if (!showLike)
                holder.likeL.visibility = View.GONE

            // set image
            ImageHandler.setImageInView(lifecycle, holder.image, images[position]!!.urls!!.full + Config.IMAGE_HEIGHT)
            // set artist image
            ImageHandler.setImageInView(lifecycle, holder.circleImage, images[position]!!.user!!.profile_image!!.large)

            // set image background image
            holder.image.background = ColorDrawable(Color.parseColor(images[position]!!.color!!))
            holder.name.text = F.capWord(images[position]!!.user!!.name)

            // setting the like button
            F.like(context, holder.like, false, image.liked_by_user)

            // open image activity
            holder.image.setOnClickListener {
                var intent = Intent(context, ImageActivity::class.java)
                intent.putExtra(C.TYPE, "")
                intent.putExtra(C.POSITION, position)
                intent.putExtra(C.NAME, context.packageName)
                intent.putExtra(C.IMAGE_POJO, Gson().toJson(images[position]))
                (context as AppCompatActivity).startActivity(intent)
            }

            // like an image
            holder.likeL.setOnClickListener {
                var state = !image.liked_by_user
                // checking if user is logged
                if (Config.USER_API_KEY.isNotEmpty()) {
                    //firing image liked event
                    var obj = JSONObject()
                    obj.put(C.TYPE, C.LIKE)
                    obj.put(C.LIKE, state)
                    obj.put(C.ID, image.id)
                    EventBus.getDefault().postSticky(Event(obj))
                    F.like(context, holder.like, image.id, state)
                }
                // opening login sheet if user not logged in
                else {
                    var sheet = ModalSheetUnsplash()
                    sheet.show((context as AppCompatActivity).supportFragmentManager, sheet.tag)
                }
            }

            // open artist page
            holder.name.setOnClickListener(artistClick)
            holder.circleImage.setOnClickListener(artistClick)

        }
    }

    // set load more listener
    fun setOnLoadMoreListener(loadMoreListener: OnLoadMoreListener) {
        this.loadMoreListener = loadMoreListener
    }

    // is loading
    fun setLoaded() {
        isLoading = false
    }
}