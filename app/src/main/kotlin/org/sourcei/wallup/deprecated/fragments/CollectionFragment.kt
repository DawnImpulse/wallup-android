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
package org.sourcei.wallup.deprecated.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_collection.*
import org.sourcei.wallup.deprecated.R
import org.sourcei.wallup.deprecated.utils.L
import org.sourcei.wallup.deprecated.utils.toast

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-09 by Saksham
 *
 * @note Updates :
 * Saksham - 2018 09 15 - master - adding wallup content
 * Saksham - 2018 12 04 - master - new reload / progress
 */
class CollectionFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, org.sourcei.wallup.deprecated.interfaces.OnLoadMoreListener, View.OnClickListener {
    private val NAME = "CollectionFragment"
    private lateinit var model: org.sourcei.wallup.deprecated.models.UnsplashModel
    private lateinit var cols: MutableList<org.sourcei.wallup.deprecated.pojo.CollectionPojo?>
    private lateinit var adapter: org.sourcei.wallup.deprecated.adapters.CollectionsAdapter
    private lateinit var type: String
    private var nextPage = 2

    // on create view
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collection, container, false)
    }

    // on view created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        type = arguments!!.getString(org.sourcei.wallup.deprecated.utils.C.TYPE)
        model = org.sourcei.wallup.deprecated.models.UnsplashModel(lifecycle)

        when (type) {
            org.sourcei.wallup.deprecated.utils.C.FEATURED -> model.featuredCollections(1, callback)
            org.sourcei.wallup.deprecated.utils.C.CURATED -> model.curatedCollections(1, callback)
            org.sourcei.wallup.deprecated.utils.C.WALLUP -> model.userCollections(org.sourcei.wallup.deprecated.utils.C.DAWN_IMPULSE, 1, 30, callback)
        }

        colLSwipe.setOnRefreshListener(this)
        colLReload.setOnClickListener(this)
        colLProgressI.animation = AnimationUtils.loadAnimation(context, R.anim.rotation_progress)
    }

    // on click
    override fun onClick(v: View) {
        colLReload.isVisible = false
        colLProgress.isVisible = true
        when (type) {
            org.sourcei.wallup.deprecated.utils.C.FEATURED -> model.featuredCollections(1, callback)
            org.sourcei.wallup.deprecated.utils.C.CURATED -> model.curatedCollections(1, callback)
            org.sourcei.wallup.deprecated.utils.C.WALLUP -> model.userCollections(org.sourcei.wallup.deprecated.utils.C.DAWN_IMPULSE, 1, 30, callback)
        }
    }

    // on refresh
    override fun onRefresh() {
        when (type) {
            org.sourcei.wallup.deprecated.utils.C.FEATURED -> model.featuredCollections(1, callback)
            org.sourcei.wallup.deprecated.utils.C.CURATED -> model.curatedCollections(1, callback)
            org.sourcei.wallup.deprecated.utils.C.WALLUP -> model.userCollections(org.sourcei.wallup.deprecated.utils.C.DAWN_IMPULSE, 1, 30, callback)
        }
    }

    // on load more
    override fun onLoadMore() {
        cols.add(null)
        adapter.notifyItemInserted(cols.size)
        when (type) {
            org.sourcei.wallup.deprecated.utils.C.FEATURED -> model.featuredCollections(nextPage, callbackPaginated)
            org.sourcei.wallup.deprecated.utils.C.CURATED -> model.curatedCollections(nextPage, callbackPaginated)
            org.sourcei.wallup.deprecated.utils.C.WALLUP -> model.userCollections(org.sourcei.wallup.deprecated.utils.C.DAWN_IMPULSE, nextPage, 30, callbackPaginated)
        }
    }

    // callback for setting images in adapter
    private var callback = object : (Any?, Any?) -> Unit {
        override fun invoke(error: Any?, response: Any?) {
            error?.let {
                L.d(NAME, error)
                colLSwipe.isRefreshing = false
                colLProgress.isVisible = false
                colLReload.isVisible = true
                context!!.toast("error fetching $type collections")
            }
            response?.let {
                cols = (response as List<org.sourcei.wallup.deprecated.pojo.CollectionPojo>).toMutableList()
                adapter = org.sourcei.wallup.deprecated.adapters.CollectionsAdapter(lifecycle, cols, type, colLRecycler)
                colLRecycler.layoutManager = LinearLayoutManager(context)
                colLRecycler.adapter = adapter
                colLSwipe.isVisible = true
                colLRecycler.isVisible = true
                colLSwipe.isRefreshing = false
                colLProgress.isVisible = false
                colLReload.isVisible = false

                adapter.setOnLoadMoreListener(this@CollectionFragment)
            }
        }
    }

    // callback for setting images in adapter
    private var callbackPaginated = object : (Any?, Any?) -> Unit {
        override fun invoke(error: Any?, response: Any?) {
            error?.let {
                L.d(NAME, error)
                context!!.toast("unable to fetch curatedCollections")
            }
            response?.let {
                nextPage++
                cols.removeAt(cols.size - 1)
                adapter.notifyItemRemoved(cols.size - 1)
                cols.addAll(response as List<org.sourcei.wallup.deprecated.pojo.CollectionPojo>)
                adapter.notifyDataSetChanged()
                adapter.setLoaded()
            }
        }
    }

}
