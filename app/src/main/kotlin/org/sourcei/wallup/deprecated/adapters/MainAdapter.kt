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
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import org.sourcei.wallup.deprecated.R
import org.sourcei.wallup.deprecated.utils.F
import org.sourcei.wallup.deprecated.utils.show
import org.sourcei.wallup.deprecated.utils.toast
import org.sourcei.wallup.deprecated.viewholders.LoadingViewHolder
import org.sourcei.wallup.deprecated.viewholders.MainViewHolder

/**
 * @author Saksham
 *
 * @note Last Branch Update - recent
 * @note Created on 2018-05-16 by Saksham
 *
 * @note Updates :
 *  Saksham - 2018 05 25 - recent - intent to Image Activity
 *  Saksham - 2018 12 04 - master - long press toast
 *  Saksham - 2018 12 20 - master - help dialog
 */
class MainAdapter(
        private val lifecycle: Lifecycle,
        private val images: List<org.sourcei.wallup.deprecated.pojo.ImagePojo?>,
        recycler: RecyclerView,
        private val showLike: Boolean = true)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val NAME = "MainAdapter"
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private val visibleThreshold = 5
    private var isLoading: Boolean = false
    private var loadMoreListener: org.sourcei.wallup.deprecated.interfaces.OnLoadMoreListener? = null
    private var VIEW_TYPE_LOADING = 0
    private var VIEW_TYPE_ITEM = 1
    private lateinit var context: Context

    // initialization for Load More Listener
    init {
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val mLinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager

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
            val point = F.displayDimensions(context)
            var image = images[position]!!

            if (image.height > image.width)
                holder.layout.layoutParams = ViewGroup.LayoutParams(point.x, (0.75 * point.y).toInt())
            else
                holder.layout.layoutParams = ViewGroup.LayoutParams(point.x, (0.475 * point.y).toInt())

            val cardLP = holder.card.layoutParams as RelativeLayout.LayoutParams
            cardLP.setMargins(F.dpToPx(4, context))
            /*val margins = holder.layout.layoutParams as ViewGroup.MarginLayoutParams
            margins.setMargins(F.dpToPx(4, context))*/

            var artistClick = View.OnClickListener {
                var intent = Intent(context, org.sourcei.wallup.deprecated.activities.ArtistProfileActivity::class.java)
                intent.putExtra(org.sourcei.wallup.deprecated.utils.C.USERNAME, images[position]!!.user!!.username)
                context.startActivity(intent)
            }
            var longPress = View.OnLongClickListener { v ->
                when (v.id) {
                    holder.likeL.id -> context.toast("like an image")
                    holder.colL.id -> context.toast("add image to collection")
                }
                false
            }

            // show/gone like button
            if (!showLike)
                holder.likeL.visibility = View.INVISIBLE

            // set image
            org.sourcei.wallup.deprecated.handlers.ImageHandler.setImageInView(lifecycle, holder.image, images[position]!!.urls!!.full + org.sourcei.wallup.deprecated.utils.Config.IMAGE_LIST_QUALITY)
            // set artist image
            org.sourcei.wallup.deprecated.handlers.ImageHandler.setImageInView(lifecycle, holder.circleImage, images[position]!!.user!!.profile_image!!.large)

            // set image background image
            holder.image.background = ColorDrawable(Color.parseColor(images[position]!!.color!!))
            holder.name.text = F.capWord(images[position]!!.user!!.name)

            // setting the like button
            F.like(context, holder.like, false, image.liked_by_user)

            // setting col icon
            if (image.current_user_collections != null)
                if (image.current_user_collections!!.isNotEmpty())
                    holder.col.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.vd_plus_circle_accent))
                else
                    holder.col.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.vd_plus_accent))

            // open collection
            holder.colL.setOnClickListener {
                if (org.sourcei.wallup.deprecated.utils.Config.USER_API_KEY.isNotEmpty()) {
                    var colSheet = org.sourcei.wallup.deprecated.sheets.ModalSheetCollection()
                    var bundle = Bundle()
                    bundle.putString(org.sourcei.wallup.deprecated.utils.C.IMAGE_POJO, Gson().toJson(image))
                    image.current_user_collections?.let { cols ->
                        if (cols.isNotEmpty())
                            bundle.putString(org.sourcei.wallup.deprecated.utils.C.COLLECTIONS, Gson().toJson(cols))
                    }
                    colSheet.arguments = bundle
                    colSheet.show((context as AppCompatActivity).supportFragmentManager, colSheet.tag)
                } else {
                    var loginSheet = org.sourcei.wallup.deprecated.sheets.ModalSheetUnsplash()
                    loginSheet.show((context as AppCompatActivity).supportFragmentManager, loginSheet.tag)
                }
            }

            // open artist page
            holder.name.setOnClickListener(artistClick)
            holder.circleImage.setOnClickListener(artistClick)

            // long press listener
            holder.likeL.setOnLongClickListener(longPress)
            holder.colL.setOnLongClickListener(longPress)

            // like handling
            fun like() {
                var state = !image.liked_by_user
                // checking if user is logged
                if (org.sourcei.wallup.deprecated.utils.Config.USER_API_KEY.isNotEmpty()) {
                    //firing image liked event
                    var obj = JSONObject()
                    obj.put(org.sourcei.wallup.deprecated.utils.C.TYPE, org.sourcei.wallup.deprecated.utils.C.LIKE)
                    obj.put(org.sourcei.wallup.deprecated.utils.C.LIKE, state)
                    obj.put(org.sourcei.wallup.deprecated.utils.C.ID, image.id)
                    EventBus.getDefault().post(org.sourcei.wallup.deprecated.utils.Event(obj))
                    F.like(context, holder.like, image.id, state)
                }
                // opening login sheet if user not logged in
                else {
                    var sheet = org.sourcei.wallup.deprecated.sheets.ModalSheetUnsplash()
                    sheet.show((context as AppCompatActivity).supportFragmentManager, sheet.tag)
                }
            }

            // like an image
            holder.likeL.setOnClickListener {
                like()
            }

            // single & double click handling
            /*var click = 0
            fun clicksHandling() {
                if (click == 1) {
                    launch {
                        delay(300)
                        if (click == 1) {
                            // single click handling
                            (context as AppCompatActivity).runOnUiThread {
                                var intent = Intent(context, ImageActivity::class.java)
                                intent.putExtra(C.TYPE, "")
                                intent.putExtra(C.POSITION, position)
                                intent.putExtra(C.NAME, context.packageName)
                                intent.putExtra(C.IMAGE_POJO, Gson().toJson(images[position]))
                                (context as AppCompatActivity).startActivity(intent)
                            }
                        }
                        click = 0
                    }
                }
                //double click handling
                if (click == 2) {
                    like()
                }
            }*/
            holder.image.setOnClickListener {
                //click++
                var intent = Intent(context, org.sourcei.wallup.deprecated.activities.ImageActivity::class.java)
                intent.putExtra(org.sourcei.wallup.deprecated.utils.C.TYPE, "")
                intent.putExtra(org.sourcei.wallup.deprecated.utils.C.POSITION, position)
                intent.putExtra(org.sourcei.wallup.deprecated.utils.C.NAME, context.packageName)
                intent.putExtra(org.sourcei.wallup.deprecated.utils.C.IMAGE_POJO, Gson().toJson(images[position]))
                (context as AppCompatActivity).startActivity(intent)
            }


            // image long press
            holder.image.setOnLongClickListener {
                var imageSheet = org.sourcei.wallup.deprecated.sheets.ModalSheetImage()
                var bundle = Bundle()
                bundle.putString(org.sourcei.wallup.deprecated.utils.C.IMAGE_POJO, F.toJson(image))
                imageSheet.arguments = bundle
                imageSheet.show((context as AppCompatActivity).supportFragmentManager)
                true
            }

        }
    }

    // set load more listener
    fun setOnLoadMoreListener(loadMoreListener: org.sourcei.wallup.deprecated.interfaces.OnLoadMoreListener) {
        this.loadMoreListener = loadMoreListener
    }

    // is loading
    fun setLoaded() {
        isLoading = false
    }

}
