package com.dawnimpulse.wallup.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.adapter.UnsplashAdapter
import com.dawnimpulse.wallup.ui.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.ui.models.UnsplashViewModel
import com.dawnimpulse.wallup.ui.objects.UnsplashImageObject
import com.dawnimpulse.wallup.utils.*
import com.dawnimpulse.wallup.utils.functions.loge
import com.dawnimpulse.wallup.utils.functions.toast
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_random.*

class RandomActivity : AppCompatActivity(),
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, OnLoadMoreListener {
    private lateinit var unsplashModel: UnsplashViewModel
    private lateinit var adapter: UnsplashAdapter
    private var compositeDisposable = CompositeDisposable()

    // error events on bus
    private fun errorEvents(error: RxErrorBusObject) {
        when (error.type) {
            ERROR_UNSPLASH_RANDOM -> {
                loge(error.error ?: "error fetching images")
                toast("error fetching images")
                generalSwipe.isRefreshing = false
            }
        }
    }

    // general event on bus
    private fun events(event: String) {
        when (event) {
            REFRESH_UNSPLASH_RANDOM_DONE -> {
                generalSwipe.isRefreshing = false
            }
        }
    }

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random)

        unsplashModel = ViewModelProviders.of(this).get(UnsplashViewModel::class.java)
        unsplashModel.randomImages.observe(this, randomImagesObserver())

        compositeDisposable.add(RxErrorBus.subscribe { errorEvents(it) })
        compositeDisposable.add(RxBus.subscribe { events(it) })

        generalSwipe.setOnRefreshListener(this)
    }

    // destroying disposables
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    // on refresh
    override fun onRefresh() {
        RxBus.accept(REFRESH_UNSPLASH_RANDOM)
    }

    // on click
    override fun onClick(v: View?) {

    }

    // on load more items
    override fun onLoadMore() {
        unsplashModel.getRandomImages()
    }

    // random images observer
    private fun randomImagesObserver(): Observer<MutableList<UnsplashImageObject>> {
        return Observer {

            if (::adapter.isInitialized) {
                adapter.notifyDataSetChanged()
                adapter.setLoaded()

            } else {

                adapter = UnsplashAdapter(it, generalRecycler)
                generalRecycler.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                generalRecycler.adapter = adapter
                adapter.setOnLoadMoreListener(this)
            }
        }
    }
}
