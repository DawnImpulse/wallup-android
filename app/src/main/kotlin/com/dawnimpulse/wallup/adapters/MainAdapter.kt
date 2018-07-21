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
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.activities.ImageActivity
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Config
import com.dawnimpulse.wallup.viewholders.LoadingViewHolder
import com.dawnimpulse.wallup.viewholders.MainViewHolder
import com.google.gson.Gson

/**
 * @author Saksham
 *
 * @note Last Branch Update - recent
 * @note Created on 2018-05-16 by Saksham
 *
 * @note Updates :
 *  Saksham - 2018 05 25 - recent - intent to Image Activity
 */
class MainAdapter(private val lifecycle: Lifecycle, private val images: List<ImagePojo?>, recycler: RecyclerView)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
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
        return images.size
    }

    /**
     * get item view type
     */
    override fun getItemViewType(position: Int): Int {
        return if (images.elementAtOrNull(position) == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    /**
     * create view holder
     */
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

    /**
     * binding view holder
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainViewHolder) {
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