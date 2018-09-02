package com.dawnimpulse.wallup.activities

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import androidx.core.widget.toast
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.adapters.MainAdapter
import com.dawnimpulse.wallup.adapters.RandomAdapter
import com.dawnimpulse.wallup.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.L
import kotlinx.android.synthetic.main.activity_general_images.*

class GeneralImagesActivity : AppCompatActivity(), View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {

    private val NAME = "GeneralImagesActivity"
    private var current = 0
    private var nextPage = 2
    private lateinit var model: UnsplashModel
    private lateinit var randomAdapter: RandomAdapter
    private lateinit var mainAdapter: MainAdapter
    private lateinit var username: String
    private lateinit var images: MutableList<ImagePojo?>

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_images)

        model = UnsplashModel(lifecycle)
        when (intent.extras.getString(C.TYPE)) {
            C.RANDOM -> randomImages()
            C.ARTIST_IMAGES -> {
                current = 1
                username = intent.extras.getString(C.USERNAME)
                paginatedImages()
            }
        }

        generalImagesFab.setOnClickListener(this)
        generalImagesSwipe.setOnRefreshListener(this)
    }

    // on click
    override fun onClick(v: View) {
        when (v.id) {
            generalImagesFab.id -> randomImages()
        }
    }

    // on swipe refresh
    override fun onRefresh() {
        when (current) {
            0 -> randomImages()
            1 -> paginatedImages()
        }
    }

    // on load more
    override fun onLoadMore() {
        images.add(null)
        mainAdapter.notifyItemInserted(images.size)
        when (current) {
            1 -> model.userPhotos(nextPage, 30, username, callbackPaginated)
        }
    }

    /**
     * random images
     */
    private fun randomImages() {
        generalImagesProgress.visibility = View.VISIBLE
        generalImagesSwipe.visibility = View.GONE

        when (current) {
            0 -> {
                model.randomImages { e, r ->
                    setRandomImages(e, r as List<ImagePojo>)
                }
            }
            1 -> {
                toast("shuffling images")
                model.randomUserImages(username) { e, r ->
                    setRandomImages(e, r as List<ImagePojo>)
                }
            }
        }
    }

    /**
     * set random images in adapter
     */
    private fun setRandomImages(error: Any?, images: List<ImagePojo>?) {
        if (error != null) {
            L.d(NAME, error.toString())
            generalImagesSwipe.isRefreshing = false
            generalImagesProgress.visibility = View.GONE
            toast("Error while fetching random images")
        } else {
            generalImagesProgress.visibility = View.GONE
            generalImagesSwipe.visibility = View.VISIBLE
            randomAdapter = RandomAdapter(lifecycle, images as List<ImagePojo?>)
            generalImagesSwipe.isRefreshing = false
            generalImagesRecycler.layoutManager = LinearLayoutManager(this)
            generalImagesRecycler.adapter = randomAdapter
        }
    }

    /**
     * paginated images
     */
    private fun paginatedImages() {
        when (current) {
            1 -> {
                model.userPhotos(1, 30, username, callback)
            }
        }
    }

    /**
     * callback for setting images in adapter
     */
    private var callback = object : (Any?, Any?) -> Unit {
        override fun invoke(error: Any?, response: Any?) {
            if ((response as List<ImagePojo>).size < 30) {
                setRandomImages(error, response)
            } else {
                if (error != null) {
                    L.d(NAME, error)
                    toast("Error in fetching images")
                    generalImagesProgress.visibility = View.GONE
                    generalImagesSwipe.isRefreshing = false
                } else {
                    images = response.toMutableList()
                    mainAdapter = MainAdapter(lifecycle, images, generalImagesRecycler)
                    generalImagesSwipe.visibility = View.VISIBLE
                    generalImagesRecycler.layoutManager = LinearLayoutManager(this@GeneralImagesActivity)
                    generalImagesRecycler.adapter = mainAdapter
                    generalImagesSwipe.isRefreshing = false
                    generalImagesProgress.visibility = View.GONE

                    mainAdapter.setOnLoadMoreListener(this@GeneralImagesActivity)
                }
            }
        }
    }

    /**
     * callback for setting images in adapter
     */
    private var callbackPaginated = object : (Any?, Any?) -> Unit {
        override fun invoke(error: Any?, response: Any?) {
            if (error != null) {
                L.d(NAME, error)
                toast("Error loading images")
            } else {
                nextPage++
                images.removeAt(images.size - 1)
                mainAdapter.notifyItemRemoved(images.size - 1)
                images.addAll(response as List<ImagePojo>)
                mainAdapter.notifyDataSetChanged()
                mainAdapter.setLoaded()
            }
        }
    }
}
