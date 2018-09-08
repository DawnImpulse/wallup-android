package com.dawnimpulse.wallup.activities

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import androidx.core.widget.toast
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.adapters.CollectionsAdapter
import com.dawnimpulse.wallup.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.CollectionPojo
import com.dawnimpulse.wallup.utils.L
import kotlinx.android.synthetic.main.activity_collection_layout.*

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-08 by Saksham
 *
 * @note Updates :
 */
class CollectionLayoutActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    private val NAME = "CollectionLayoutActivity"
    private lateinit var model: UnsplashModel
    private lateinit var cols: MutableList<CollectionPojo?>
    private lateinit var adapter: CollectionsAdapter
    private var nextPage = 2

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_layout)

        model = UnsplashModel(lifecycle)
        model.featuredCollections(1,callback)
        colLSwipe.setOnRefreshListener(this)
    }

    // on refresh
    override fun onRefresh() {
        model.featuredCollections(1,callback)
    }

    // on load more
    override fun onLoadMore() {
        model.featuredCollections(nextPage,callbackPaginated)
    }

    /**
     * callback for setting images in adapter
     */
    private var callback = object : (Any?, Any?) -> Unit {
        override fun invoke(error: Any?, response: Any?) {
            error?.let{
                L.d(NAME, error)
                colLSwipe.isRefreshing = false
                toast("error fetching collections")
            }
            response?.let {
                cols = (response as List<CollectionPojo>).toMutableList()
                adapter = CollectionsAdapter(lifecycle, cols, colLRecycler)
                colLRecycler.layoutManager = LinearLayoutManager(this@CollectionLayoutActivity)
                colLRecycler.adapter = adapter
                colLSwipe.isRefreshing = false
                colLProgress.visibility = View.GONE
                colLRecycler.visibility = View.VISIBLE

                adapter.setOnLoadMoreListener(this@CollectionLayoutActivity)
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
                toast("unable to fetch collections")
            }
            response?.let{
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
