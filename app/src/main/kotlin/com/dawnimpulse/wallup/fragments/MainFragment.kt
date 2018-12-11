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
package com.dawnimpulse.wallup.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.widget.toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.adapters.MainAdapter
import com.dawnimpulse.wallup.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.CollectionPojo
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Event
import com.dawnimpulse.wallup.utils.F
import com.dawnimpulse.wallup.utils.L
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.fragment_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * @author Saksham
 *
 * @note Last Branch Update - recent
 * @note Created on 2018-05-15 by Saksham
 *
 * @note Updates :
 *  Saksham - 2018 05 20 - recent - using model
 *  Saksham - 2018 05 25 - recent - dummy layout
 *  Saksham - 2018 10 28 - master - event change collection
 */

class MainFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, View.OnClickListener {
    private val NAME = "MainFragment"
    private lateinit var mainAdapter: MainAdapter
    private lateinit var images: MutableList<ImagePojo?>
    private lateinit var modelR: DatabaseModel
    private var model: UnsplashModel? = null
    private val init: Boolean = true
    private var timestamp = 0
    private var nextPage = 2
    private var type = C.LATEST
    private var likePos = -1
    private var like = true

    // On create
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    // On view created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = UnsplashModel(lifecycle)
        modelR = DatabaseModel(lifecycle)

        type = arguments!!.getString(C.TYPE)
        when (type) {
            C.LATEST ->
                model?.getLatestImages(1, callback)
            C.CURATED ->
                model?.getCuratedImages(1, callback)
            C.TRENDING ->
                modelR.getTrendingImages(null, callback)
            C.RANDOM ->
                model?.randomImages(callback)
        }
        mainRefresher.setOnRefreshListener(this)
        mainReload.setOnClickListener(this)
        mainProgressI.animation = AnimationUtils.loadAnimation(context, R.anim.rotation_progress)
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

    // on click of reload
    override fun onClick(v: View) {
        mainReload.visibility = View.GONE
        mainProgress.visibility = View.VISIBLE
        when (type) {
            C.LATEST ->
                model?.getLatestImages(1, callback)
            C.CURATED ->
                model?.getCuratedImages(1, callback)
            C.TRENDING ->
                modelR.getTrendingImages(null, callback)
            C.RANDOM ->
                model?.randomImages(callback)
        }
    }


    // On swipe refresh
    override fun onRefresh() {
        when (this.type) {
            C.LATEST ->
                model?.getLatestImages(1, callback)
            C.CURATED ->
                model?.getCuratedImages(1, callback)
            C.TRENDING ->
                modelR.getTrendingImages(null, callback)
            C.RANDOM ->
                model?.randomImages(callback)

        }
    }

    // On load more items
    override fun onLoadMore() {
        images.add(null)
        mainAdapter.notifyItemInserted(images.size)
        when (this.type) {
            C.LATEST ->
                model?.getLatestImages(nextPage, callbackPaginated)
            C.CURATED ->
                model?.getCuratedImages(nextPage, callbackPaginated)
            C.TRENDING ->
                modelR.getTrendingImages(timestamp, callbackPaginated)
            C.RANDOM ->
                model?.randomImages(callbackPaginated)

        }
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
                        mainAdapter.notifyItemChanged(pos)
                    }
                }
            }
            if (event.obj.getString(C.TYPE) == C.IMAGE_TO_COLLECTION) {
                // if image is added to a collection
                if (event.obj.getBoolean(C.IS_ADDED)) {
                    var col = Gson().fromJson(event.obj.getString(C.COLLECTION), CollectionPojo::class.java)
                    var list = images
                            .asSequence()
                            .withIndex()
                            .filter { it.value!!.id == event.obj.getString(C.IMAGE) }
                            .map { it.index }
                            .toList()

                    if (list.isNotEmpty()) {
                        for (l in list) {
                            if (images[l]!!.current_user_collections == null)
                                images[l]!!.current_user_collections = arrayListOf()
                            images[l]!!.current_user_collections!!.add(col)
                            mainAdapter.notifyItemChanged(l)
                        }
                    }
                } else {
                    //if image is removed from collection
                    var list = images
                            .asSequence()
                            .withIndex()
                            .filter { it.value!!.id == event.obj.getString(C.IMAGE) }
                            .map { it.index }
                            .toList()

                    if (list.isNotEmpty()) {
                        for (l in list) {
                            var cid = images[l]!!.current_user_collections!!
                                    .asSequence()
                                    .withIndex()
                                    .filter { it.value.id == event.obj.getString(C.COLLECTION_ID) }
                                    .map { it.index }
                                    .toList()
                            images[l]!!.current_user_collections!!.removeAt(cid[0])
                            mainAdapter.notifyItemChanged(l)
                        }
                    }
                }
            }
        }
    }

    // callback for setting images in adapter
    private var callback = object : (Any?, Any?) -> Unit {
        override fun invoke(error: Any?, response: Any?) {
            error?.let {
                L.d(NAME, error)
                context!!.toast("error loading images")
                mainRefresher.isRefreshing = false
                mainProgress.visibility = View.GONE
                mainReload.visibility = View.VISIBLE
            }
            response?.let {
                images = (response as List<ImagePojo>).toMutableList()
                if (images.isNotEmpty()) {
                    when (type) {
                        C.LATEST -> Prefs.putString(C.LATEST, F.toJson(images))
                        C.RANDOM -> Prefs.putString(C.RANDOM, F.toJson(images))
                    }
                }
                //timestamp = images[images.size - 1]!!.timestamp

                mainAdapter = if (arguments?.containsKey(C.LIKE)!!)
                    MainAdapter(lifecycle, images, mainRecycler, false)
                else
                    MainAdapter(lifecycle, images, mainRecycler)

                mainRecycler.layoutManager = LinearLayoutManager(context)
                mainRecycler.adapter = mainAdapter
                (mainRecycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
                mainRefresher.isRefreshing = false
                mainProgress.visibility = View.GONE
                mainReload.visibility = View.GONE

                mainAdapter.setOnLoadMoreListener(this@MainFragment)
            }
        }
    }

    // callback for setting images in adapter
    private var callbackPaginated = object : (Any?, Any?) -> Unit {
        override fun invoke(error: Any?, response: Any?) {
            error?.let {
                L.d(NAME, error)
            }
            response?.let {
                nextPage++
                images.removeAt(images.size - 1)
                mainAdapter.notifyItemRemoved(images.size - 1)
                var newImages = response as List<ImagePojo>
                //timestamp = newImages[newImages.size - 1]!!.timestamp
                images.addAll(newImages)
                when (type) {
                    C.LATEST -> Prefs.putString(C.LATEST, F.toJson(images))
                    C.RANDOM -> Prefs.putString(C.RANDOM, F.toJson(images))
                }
                mainAdapter.notifyDataSetChanged()
                mainAdapter.setLoaded()
            }
        }
    }
}
