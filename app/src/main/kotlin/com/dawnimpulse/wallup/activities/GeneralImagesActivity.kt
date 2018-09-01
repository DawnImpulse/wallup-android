package com.dawnimpulse.wallup.activities

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import androidx.core.widget.toast
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.adapters.RandomAdapter
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.L
import kotlinx.android.synthetic.main.activity_general_images.*

class GeneralImagesActivity : AppCompatActivity(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private val NAME = "GeneralImagesActivity"
    private lateinit var model: UnsplashModel
    private lateinit var randomAdapter: RandomAdapter

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_images)

        model = UnsplashModel(lifecycle)
        when (intent.extras.getString(C.TYPE)) {
            C.RANDOM -> randomImages()
        }

        generalImagesFab.setOnClickListener(this)
    }

    // on click
    override fun onClick(v: View) {
        when (v.id) {
            generalImagesFab.id -> randomImages()
        }
    }

    // on swipe refresh
    override fun onRefresh() {
        randomImages()
    }

    /**
     * random images
     */
    private fun randomImages() {
        generalImagesProgress.visibility = View.VISIBLE
        generalImagesSwipe.visibility = View.GONE

        model.randomImages { error, images ->
            if (error != null) {
                L.d(NAME, error.toString())
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
    }
}
