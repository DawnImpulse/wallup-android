package com.dawnimpulse.wallup.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.toast
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.adapters.CollectionsAdapter
import com.dawnimpulse.wallup.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.CollectionPojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.L
import kotlinx.android.synthetic.main.fragment_collection.*

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-09 by Saksham
 *
 * @note Updates :
 * Saksham - 2018 09 15 - master - adding wallup content
 */
class CollectionFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    private val NAME = "CollectionFragment"
    private lateinit var model: UnsplashModel
    private lateinit var cols: MutableList<CollectionPojo?>
    private lateinit var adapter: CollectionsAdapter
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

        type = arguments!!.getString(C.TYPE)
        model = UnsplashModel(lifecycle)

        when (type) {
            C.FEATURED -> model.featuredCollections(1, callback)
            C.CURATED -> model.curatedCollections(1, callback)
            C.WALLUP -> model.userCollections(C.DAWN_IMPULSE, 1, 30, callback)
        }

        colLSwipe.setOnRefreshListener(this)
    }

    // on refresh
    override fun onRefresh() {
        when (type) {
            C.FEATURED -> model.featuredCollections(1, callback)
            C.CURATED -> model.curatedCollections(1, callback)
            C.WALLUP -> model.userCollections(C.DAWN_IMPULSE, 1, 30, callback)
        }
    }

    // on load more
    override fun onLoadMore() {
        cols.add(null)
        adapter.notifyItemInserted(cols.size)
        when (type) {
            C.FEATURED -> model.featuredCollections(nextPage, callbackPaginated)
            C.CURATED -> model.curatedCollections(nextPage, callbackPaginated)
            C.WALLUP -> model.userCollections(C.DAWN_IMPULSE, nextPage, 30, callbackPaginated)
        }
    }

    /**
     * callback for setting images in adapter
     */
    private var callback = object : (Any?, Any?) -> Unit {
        override fun invoke(error: Any?, response: Any?) {
            error?.let {
                L.d(NAME, error)
                colLSwipe.isRefreshing = false
                context!!.toast("error fetching $type collections")
            }
            response?.let {
                cols = (response as List<CollectionPojo>).toMutableList()
                adapter = CollectionsAdapter(lifecycle, cols, type, colLRecycler)
                colLRecycler.layoutManager = LinearLayoutManager(context)
                colLRecycler.adapter = adapter
                colLSwipe.isRefreshing = false
                colLProgress.visibility = View.GONE
                colLRecycler.visibility = View.VISIBLE

                adapter.setOnLoadMoreListener(this@CollectionFragment)
            }
        }
    }

    /**
     * callback for setting images in adapter
     */
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
                cols.addAll(response as List<CollectionPojo>)
                adapter.notifyDataSetChanged()
                adapter.setLoaded()
            }
        }
    }

}
