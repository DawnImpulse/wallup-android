package com.dawnimpulse.wallup.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.adapter.CollectionHorizontalImagesAdapter
import com.dawnimpulse.wallup.ui.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.ui.models.WallupViewModel
import com.dawnimpulse.wallup.ui.objects.BannerCollectionObject
import com.dawnimpulse.wallup.ui.objects.WallupImageObject
import com.dawnimpulse.wallup.utils.functions.loge
import com.dawnimpulse.wallup.utils.functions.toastd
import kotlinx.android.synthetic.main.activity_general.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-13 by Saksham
 * @note Updates :
 */
class CollectionHorizontalActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    private lateinit var model: WallupViewModel
    private lateinit var items: MutableList<Any?>
    private lateinit var adapter: CollectionHorizontalImagesAdapter
    private lateinit var cid: String
    private lateinit var name: String
    private lateinit var image: String
    private var page = 2
    private var finished = false

    //--------------------
    //      create
    //--------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general)

        model = WallupViewModel(this)
        items = mutableListOf(BannerCollectionObject(image, name))
        adapter = CollectionHorizontalImagesAdapter(items, generalRecycler)
        generalRecycler.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        generalRecycler.adapter = adapter

        generalSwipe.setOnRefreshListener(this)
        fetch(1, callback)
    }

    //--------------------
    //      refresh
    //--------------------
    override fun onRefresh() {
        finished = false
        fetch(1, callback)
    }

    //--------------------
    //     load more
    //--------------------
    override fun onLoadMore() {
        if (finished)
            fetch(page, callbackPaginate)
    }

    //--------------------
    //    fetch data
    //--------------------
    private fun fetch(page: Int, callback: (Any?, List<WallupImageObject>?) -> Unit) {
        model.collectionImages(page, cid, callback)
    }

    //--------------------
    //    callback
    //--------------------
    private var callback = object : (Any?, List<WallupImageObject>?) -> Unit {
        override fun invoke(e: Any?, r: List<WallupImageObject>?) {
            e?.let {
                loge(it)
                toastd(it.toString())
            }
            r?.let {

                if (it.isNotEmpty()) {
                    items.addAll(it)
                    items.add(null)
                    adapter.notifyDataSetChanged()
                    adapter.setOnLoadMoreListener(this@CollectionHorizontalActivity)

                    // list is empty
                } else {
                    finished = true
                    adapter.onLoaded()
                }

            }
        }

    }

    //------------------------
    //    callback paginate
    //------------------------
    private var callbackPaginate = object : (Any?, List<WallupImageObject>?) -> Unit {
        override fun invoke(e: Any?, r: List<WallupImageObject>?) {
            e?.let {
                loge(it)
                toastd(it.toString())
            }
            r?.let {
                if (it.isNotEmpty()) {
                    items.addAll(it)
                    items.add(null)
                    adapter.notifyDataSetChanged()

                    // list is empty
                } else {
                    finished = true
                    adapter.onLoaded()
                }
            }
        }

    }

}
