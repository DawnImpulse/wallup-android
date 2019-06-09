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
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import org.sourcei.wallup.deprecated.R
import org.sourcei.wallup.deprecated.utils.F
import org.sourcei.wallup.deprecated.utils.gone
import org.sourcei.wallup.deprecated.viewholders.CollectionsViewHolder
import org.sourcei.wallup.deprecated.viewholders.LoadingViewHolder

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018 09 by Saksham
 *
 * @note Updates :
 */
class CollectionsAdapter(private val lifecycle: Lifecycle,
                         private val cols: List<org.sourcei.wallup.deprecated.pojo.CollectionPojo?>,
                         private val type: String,
                         recycler: RecyclerView)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val NAME = "CollectionsAdapter"
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
        return cols.size
    }

    // get item view type
    override fun getItemViewType(position: Int): Int {
        return if (cols.elementAtOrNull(position) == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    // create view holder
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

    // binding view holder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CollectionsViewHolder) {
            var it = cols[position]!!
            holder.title.text = F.capWord(it.title)
            holder.artist.text = "Curated by ${it.user!!.name}"
            holder.count.text = "${it.total_photos} photos"
            /*holder.title.setTextColor(color)
            holder.artist.setTextColor(color)*/

            it.cover_photo?.let {
                holder.image0.background = ColorDrawable(Color.parseColor(it.color!!))
                org.sourcei.wallup.deprecated.handlers.ImageHandler.setImageInView(lifecycle, holder.image0, it.urls!!.full + org.sourcei.wallup.deprecated.utils.Config.IMAGE_LIST_QUALITY)
            }
            it.preview_photos?.let {
                if (it.size > 1)
                    org.sourcei.wallup.deprecated.handlers.ImageHandler.setImageInView(lifecycle, holder.image1, it[1].urls.full + org.sourcei.wallup.deprecated.utils.Config.IMAGE_LIST_QUALITY)
                else
                    holder.image1.gone()
            }

            holder.layout.setOnClickListener {
                var intent = Intent(context, org.sourcei.wallup.deprecated.activities.CollectionActivity::class.java)
                intent.putExtra(org.sourcei.wallup.deprecated.utils.C.TYPE, type)
                intent.putExtra(org.sourcei.wallup.deprecated.utils.C.COLLECTION, Gson().toJson(cols[position]))
                context.startActivity(intent)
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