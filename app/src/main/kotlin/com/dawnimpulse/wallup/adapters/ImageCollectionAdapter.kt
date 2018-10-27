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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.toast
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.activities.CollectionActivity
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.CollectionPojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Config
import com.dawnimpulse.wallup.utils.Event
import com.dawnimpulse.wallup.utils.L
import com.dawnimpulse.wallup.viewholders.ImageColViewHolder
import com.dawnimpulse.wallup.viewholders.LoadingViewHolder
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

/**
 * @author Saksham
 * @info - adding image to collection adapter
 * @info - if image is null then we need to view image only
 *
 * @note Last Branch Update - master
 * @note Created on 2018-10-21 by Saksham
 *
 * @note Updates :
 */
class ImageCollectionAdapter(private val lifecycle: Lifecycle,
                             private val cols: List<CollectionPojo?>,
                             private var imageCols: MutableList<String?>?,
                             private val image: String?,
                             private val recycler: RecyclerView)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var NAME = "ImageCollectionAdapter"
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private val visibleThreshold = 5
    private var isLoading: Boolean = false
    private var loadMoreListener: OnLoadMoreListener? = null
    private var VIEW_TYPE_LOADING = 0
    private var VIEW_TYPE_ITEM = 1

    private lateinit var context: Context
    private lateinit var model: UnsplashModel

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
        return cols.size
    }

    // get item view type
    override fun getItemViewType(position: Int): Int {
        return if (cols.elementAtOrNull(position) == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    // create view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        model = UnsplashModel(lifecycle)
        return if (viewType == VIEW_TYPE_ITEM)
            ImageColViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_image_collection, parent, false))
        else
            LoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.inflator_loading, parent, false))
    }

    // binding view holder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImageColViewHolder) {
            var available = false
            holder.text.text = cols[position]!!.title
            cols[position]!!.cover_photo?.let {
                ImageHandler.setImageInView(lifecycle, holder.image, it.urls!!.full + Config.IMAGE_HEIGHT)
            }

            if (imageCols!![position] != null) {
                holder.bg.visibility = View.GONE
                holder.bgS.visibility = View.VISIBLE
                available = true
            } else {
                holder.bg.visibility = View.VISIBLE
                holder.bgS.visibility = View.GONE
            }

            holder.image.setOnClickListener {
                if (image == null) {
                    // to only view collection
                    var intent = Intent(context, CollectionActivity::class.java)
                    intent.putExtra(C.TYPE, C.FEATURED)
                    intent.putExtra(C.COLLECTION, Gson().toJson(cols[position]))
                    context.startActivity(intent)
                } else {
                    if (available) {
                        sendEvent(cols[position]!!, false, position)
                        model.removeImageInCollection(image!!, cols[position]!!.id) { e, _ ->
                            e?.let {
                                L.d(NAME, e)
                                context.toast("error removing from collection")
                                sendEvent(cols[position]!!, true, position)
                            }
                        }

                    } else {
                        sendEvent(cols[position]!!, true, position)
                        model.addImageInCollection(image!!, cols[position]!!.id) { e, _ ->
                            e?.let {
                                L.d(NAME, e)
                                context.toast("error adding to collection")
                                sendEvent(cols[position]!!, false, position)
                            }
                        }
                    }
                }
            }
        }
    }

    // send event to the image activity
    private fun sendEvent(col: CollectionPojo, isAdded: Boolean, position: Int) {
        var json = JSONObject()
        json.put(C.TYPE, C.IMAGE_TO_COLLECTION)
        json.put(C.IS_ADDED, isAdded)
        json.put(C.COLLECTION_ID, col.id)
        json.put(C.POSITION, position)
        json.put(C.COLLECTION, Gson().toJson(col))
        EventBus.getDefault().post(Event(json))
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