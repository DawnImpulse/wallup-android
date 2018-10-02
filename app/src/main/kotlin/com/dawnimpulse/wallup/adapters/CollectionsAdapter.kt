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
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.activities.CollectionActivity
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.pojo.CollectionPojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Config
import com.dawnimpulse.wallup.viewholders.CollectionsViewHolder
import com.dawnimpulse.wallup.viewholders.LoadingViewHolder
import com.google.gson.Gson

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018 09 by Saksham
 *
 * @note Updates :
 */
class CollectionsAdapter(private val lifecycle: Lifecycle,
                         private val cols: List<CollectionPojo?>,
                         private val type: String,
                         recycler: RecyclerView)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val NAME = "CollectionsAdapter"
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private val visibleThreshold = 5
    private var isLoading: Boolean = false
    private var loadMoreListener: OnLoadMoreListener? = null
    private var VIEW_TYPE_LOADING = 0
    private var VIEW_TYPE_ITEM = 1
    private lateinit var context: Context

    /**
     * initialization for Load More Listener
     */
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

    /**
     * get total no of items for adapter
     */
    override fun getItemCount(): Int {
        return cols.size
    }

    /**
     * get item view type
     */
    override fun getItemViewType(position: Int): Int {
        return if (cols.elementAtOrNull(position) == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    /**
     * create view holder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View
        context = parent.context
        return if (viewType == VIEW_TYPE_ITEM) {
            v = LayoutInflater.from(parent.context).inflate(R.layout.inflator_collection_layout, parent, false)
            CollectionsViewHolder(v)
        } else {
            v = LayoutInflater.from(parent.context).inflate(R.layout.inflator_loading, parent, false)
            LoadingViewHolder(v)
        }
    }

    /**
     * binding view holder
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CollectionsViewHolder) {
            var it = cols[position]!!
            var color = Color.parseColor(it.cover_photo.color!!)
            holder.title.text = it.title
            holder.artist.text = "Curated by ${it.user!!.name}"
            holder.count.text = "${it.total_photos} photos"
            holder.image0.background = ColorDrawable(color)
            /*holder.title.setTextColor(color)
            holder.artist.setTextColor(color)*/

            ImageHandler.setImageInView(lifecycle, holder.image0, it.cover_photo.urls!!.full + Config.IMAGE_HEIGHT)
            it.preview_photos?.let {
                if (it.size > 1)
                    ImageHandler.setImageInView(lifecycle, holder.image1, it[1].urls.full + Config.IMAGE_HEIGHT)
                if (it.size > 2)
                    ImageHandler.setImageInView(lifecycle, holder.image2, it[2].urls.full + Config.IMAGE_HEIGHT)
                if (it.size > 3)
                    ImageHandler.setImageInView(lifecycle, holder.image3, it[3].urls.full + Config.IMAGE_HEIGHT)
            }

            holder.layout.setOnClickListener {
                var intent = Intent(context, CollectionActivity::class.java)
                intent.putExtra(C.TYPE, type)
                intent.putExtra(C.COLLECTION, Gson().toJson(cols[position]))
                context.startActivity(intent)
            }
        }
    }

    /**
     * set load more listener
     */
    fun setOnLoadMoreListener(loadMoreListener: OnLoadMoreListener) {
        this.loadMoreListener = loadMoreListener
    }

    /**
     * is loading
     */
    fun setLoaded() {
        isLoading = false
    }
}