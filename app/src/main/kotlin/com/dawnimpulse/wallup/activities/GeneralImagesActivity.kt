package com.dawnimpulse.wallup.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.adapters.MainAdapter
import com.dawnimpulse.wallup.adapters.RandomAdapter
import com.dawnimpulse.wallup.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Event
import com.dawnimpulse.wallup.utils.L
import kotlinx.android.synthetic.main.activity_general_images.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09 by Saksham
 *
 * @note Updates :
 * Saksham - 2018 09 09 - master - collection images
 * Saksham - 2018 09 22 - master - random images tag
 */
class GeneralImagesActivity : AppCompatActivity(), View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {

    private val NAME = "GeneralImagesActivity"
    private var current: Int = 0
    private var nextPage = 2
    private var randomImages = false //if random images are set
    private lateinit var model: UnsplashModel
    private lateinit var randomAdapter: RandomAdapter
    private lateinit var mainAdapter: MainAdapter
    private lateinit var username: String
    private lateinit var images: MutableList<ImagePojo?>
    private lateinit var colId: String
    private lateinit var colType: String
    private lateinit var tag: String

    // on create
    @SuppressLint("RestrictedApi")
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
            C.COLLECTION -> {
                current = 2
                colId = intent.extras.getString(C.ID)
                colType = intent.extras.getString(C.COLLECTION)
                paginatedImages()
            }
            C.TAG -> {
                current = 3
                tag = intent.extras.getString(C.TAG)
                randomImages()
            }
            C.LIKE -> {
                current = 4
                username = intent.extras.getString(C.USERNAME)
                L.d(NAME,username)
                paginatedImages()
                generalImagesFab.visibility = View.GONE
            }
        }

        generalImagesFab.setOnClickListener(this)
        generalImagesSwipe.setOnRefreshListener(this)
        generalBack.setOnClickListener(this)
    }

    // on start
    override fun onStart() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
        super.onStart()
    }

    // on destroy
    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    // on click
    override fun onClick(v: View) {
        when (v.id) {
            generalImagesFab.id -> randomImages()
            generalBack.id -> finish()
        }
    }

    // on swipe refresh
    override fun onRefresh() {
        when (current) {
            0 -> randomImages()
            1 -> paginatedImages()
            2 -> paginatedImages()
            3 -> randomImages()
            4 -> paginatedImages()
        }
    }

    // on load more
    override fun onLoadMore() {
        images.add(null)
        mainAdapter.notifyItemInserted(images.size)
        when (current) {
            1 -> model.userPhotos(nextPage, 30, username, callbackPaginated)
            2 -> {
                if (colType == C.FEATURED)
                    model.collectionPhotos(colId, nextPage, 30, callbackPaginated)
                else
                    model.curatedCollectionPhotos(colId, nextPage, 30, callbackPaginated)
            }
            4 -> model.userLikedPhotos(username,nextPage, callbackPaginated)
        }
    }

    // back press
    override fun onBackPressed() {
        finish()
    }

    // on message event
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onEvent(event: Event) {
        if (event.obj.has(C.TYPE)) {
            if (event.obj.getString(C.TYPE) == C.LIKE) {
                // get id of the image
                val id = event.obj.getString(C.ID)
                // get position array for the image
                var position = images.asSequence().withIndex().filter { it.value!!.id == id }.map { it.index }.toList()
                // if position found
                if (position.isNotEmpty()) {
                    // change like state in images array
                    for (pos in position) {
                        L.d(NAME, pos)
                        images[pos]!!.liked_by_user = event.obj.getBoolean(C.LIKE)
                        if (randomImages)
                            randomAdapter.notifyItemChanged(pos)
                        else
                            mainAdapter.notifyItemChanged(pos)
                    }
                }
            }
        }
    }

    // random images
    private fun randomImages() {
        randomImages = true
        generalImagesProgress.visibility = View.VISIBLE
        generalImagesSwipe.visibility = View.GONE

        when (current) {
            0 -> {
                model.randomImages(callbackR)
            }
            1 -> {
                toast("shuffling images")
                model.randomUserImages(username, callbackR)
            }
            2 -> {
                toast("shuffling images")
                if (colType == C.FEATURED)
                    model.randomCollectionPhotos(colId, callbackR)
                else // we cant get curated random images hence shuffling normal ones
                    model.randomImages(callbackR)
            }
            3 -> {
                model.randomImagesTag(tag, callbackR)
            }
        }
    }

    // paginated images
    private fun paginatedImages() {
        when (current) {
            1 -> {
                model.userPhotos(1, 30, username, callback)
            }
            2 -> {
                if (colType == C.FEATURED)
                    model.collectionPhotos(colId, 1, 30, callback)
                else
                    model.curatedCollectionPhotos(colId, 1, 30, callback)
            }
            4 -> {
                model.userLikedPhotos(username, 1,callback)
            }
        }
    }

    // set random images in adapter
    private var callbackR = object : (Any?, Any?) -> Unit {
        override fun invoke(error: Any?, r: Any?) {
            L.d(NAME, "called")
            error?.let {
                L.d(NAME, error)
                generalImagesSwipe.isRefreshing = false
                generalImagesProgress.visibility = View.GONE
                toast("Error while fetching random images")
            }
            r?.let {
                images = (r as List<ImagePojo?>).toMutableList()
                generalImagesProgress.visibility = View.GONE
                generalImagesSwipe.visibility = View.VISIBLE

                randomAdapter = RandomAdapter(lifecycle, images)
                generalImagesSwipe.isRefreshing = false
                generalImagesRecycler.layoutManager = LinearLayoutManager(this@GeneralImagesActivity)
                generalImagesRecycler.adapter = randomAdapter
                (generalImagesRecycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            }
        }
    }

    // callback for setting images in adapter
    private var callback = object : (Any?, Any?) -> Unit {
        override fun invoke(error: Any?, response: Any?) {
            randomImages = false

            error?.let {
                L.d(NAME, error)
                generalImagesProgress.visibility = View.GONE
                generalImagesSwipe.isRefreshing = false
                toast("error fetching images")
            }
            response?.let {
                if ((response as List<ImagePojo>).size < 30) {
                    randomImages = true
                    callbackR(error, response)
                } else {
                    images = response.toMutableList()
                    mainAdapter = MainAdapter(lifecycle, images, generalImagesRecycler)
                    generalImagesRecycler.layoutManager = LinearLayoutManager(this@GeneralImagesActivity)
                    generalImagesRecycler.adapter = mainAdapter

                    generalImagesSwipe.isRefreshing = false
                    generalImagesSwipe.visibility = View.VISIBLE
                    generalImagesProgress.visibility = View.GONE
                    (generalImagesRecycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

                    mainAdapter.setOnLoadMoreListener(this@GeneralImagesActivity)
                }
            }
        }
    }

    // callback for setting images in adapter
    private var callbackPaginated = object : (Any?, Any?) -> Unit {
        override fun invoke(error: Any?, response: Any?) {
            error?.let {
                L.d(NAME, error)
                toast("Error loading images")
            }
            response?.let {
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
