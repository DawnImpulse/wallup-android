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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.fragment_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.sourcei.wallup.deprecated.R
import org.sourcei.wallup.deprecated.utils.F
import org.sourcei.wallup.deprecated.utils.L
import org.sourcei.wallup.deprecated.utils.toast


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

class MainFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, org.sourcei.wallup.deprecated.interfaces.OnLoadMoreListener, View.OnClickListener {
    private val NAME = "MainFragment"
    private lateinit var mainAdapter: org.sourcei.wallup.deprecated.adapters.MainAdapter
    private lateinit var images: MutableList<org.sourcei.wallup.deprecated.pojo.ImagePojo?>
    private var model: org.sourcei.wallup.deprecated.models.UnsplashModel? = null
    private val init: Boolean = true
    private var timestamp = 0
    private var nextPage = 2
    private var type = org.sourcei.wallup.deprecated.utils.C.LATEST
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

        model = org.sourcei.wallup.deprecated.models.UnsplashModel(lifecycle)

        type = arguments!!.getString(org.sourcei.wallup.deprecated.utils.C.TYPE)
        when (type) {
            org.sourcei.wallup.deprecated.utils.C.LATEST ->
                model?.getLatestImages(1, callback)
            org.sourcei.wallup.deprecated.utils.C.CURATED ->
                model?.getCuratedImages(1, callback)
            org.sourcei.wallup.deprecated.utils.C.RANDOM ->
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
            org.sourcei.wallup.deprecated.utils.C.LATEST ->
                model?.getLatestImages(1, callback)
            org.sourcei.wallup.deprecated.utils.C.CURATED ->
                model?.getCuratedImages(1, callback)
            org.sourcei.wallup.deprecated.utils.C.RANDOM ->
                model?.randomImages(callback)
        }
    }


    // On swipe refresh
    override fun onRefresh() {
        when (this.type) {
            org.sourcei.wallup.deprecated.utils.C.LATEST ->
                model?.getLatestImages(1, callback)
            org.sourcei.wallup.deprecated.utils.C.CURATED ->
                model?.getCuratedImages(1, callback)
            org.sourcei.wallup.deprecated.utils.C.RANDOM ->
                model?.randomImages(callback)

        }
    }

    // On load more items
    override fun onLoadMore() {
        images.add(null)
        mainAdapter.notifyItemInserted(images.size)
        when (this.type) {
            org.sourcei.wallup.deprecated.utils.C.LATEST ->
                model?.getLatestImages(nextPage, callbackPaginated)
            org.sourcei.wallup.deprecated.utils.C.CURATED ->
                model?.getCuratedImages(nextPage, callbackPaginated)
            org.sourcei.wallup.deprecated.utils.C.RANDOM ->
                model?.randomImages(callbackPaginated)

        }
    }

    // on message event
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: org.sourcei.wallup.deprecated.utils.Event) {
        if (event.obj.has(org.sourcei.wallup.deprecated.utils.C.TYPE)) {
            if (event.obj.getString(org.sourcei.wallup.deprecated.utils.C.TYPE) == org.sourcei.wallup.deprecated.utils.C.LIKE) {
                // get id of the image
                val id = event.obj.getString(org.sourcei.wallup.deprecated.utils.C.ID)
                // get position array for the image
                var position = images.asSequence().withIndex().filter { it.value!!.id == id }.map { it.index }.toList()
                // if position found
                if (position.isNotEmpty()) {
                    // change like state in images array
                    for (pos in position) {
                        L.d(NAME, pos)
                        images[pos]!!.liked_by_user = event.obj.getBoolean(org.sourcei.wallup.deprecated.utils.C.LIKE)
                        mainAdapter.notifyItemChanged(pos)
                    }
                }
            }
            if (event.obj.getString(org.sourcei.wallup.deprecated.utils.C.TYPE) == org.sourcei.wallup.deprecated.utils.C.IMAGE_TO_COLLECTION) {
                // if image is added to a collection
                if (event.obj.getBoolean(org.sourcei.wallup.deprecated.utils.C.IS_ADDED)) {
                    var col = Gson().fromJson(event.obj.getString(org.sourcei.wallup.deprecated.utils.C.COLLECTION), org.sourcei.wallup.deprecated.pojo.CollectionPojo::class.java)
                    var list = images
                            .asSequence()
                            .withIndex()
                            .filter { it.value!!.id == event.obj.getString(org.sourcei.wallup.deprecated.utils.C.IMAGE) }
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
                            .filter { it.value!!.id == event.obj.getString(org.sourcei.wallup.deprecated.utils.C.IMAGE) }
                            .map { it.index }
                            .toList()

                    if (list.isNotEmpty()) {
                        for (l in list) {
                            var cid = images[l]!!.current_user_collections!!
                                    .asSequence()
                                    .withIndex()
                                    .filter { it.value.id == event.obj.getString(org.sourcei.wallup.deprecated.utils.C.COLLECTION_ID) }
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
                images = (response as List<org.sourcei.wallup.deprecated.pojo.ImagePojo>).toMutableList()
                if (images.isNotEmpty()) {
                    when (type) {
                        org.sourcei.wallup.deprecated.utils.C.LATEST -> Prefs.putString(org.sourcei.wallup.deprecated.utils.C.LATEST, F.toJson(images))
                        org.sourcei.wallup.deprecated.utils.C.RANDOM -> Prefs.putString(org.sourcei.wallup.deprecated.utils.C.RANDOM, F.toJson(images))
                    }
                }
                //timestamp = images[images.size - 1]!!.timestamp

                mainAdapter = if (arguments?.containsKey(org.sourcei.wallup.deprecated.utils.C.LIKE)!!)
                    org.sourcei.wallup.deprecated.adapters.MainAdapter(lifecycle, images, mainRecycler, false)
                else
                    org.sourcei.wallup.deprecated.adapters.MainAdapter(lifecycle, images, mainRecycler)

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
                var newImages = response as List<org.sourcei.wallup.deprecated.pojo.ImagePojo>
                //timestamp = newImages[newImages.size - 1]!!.timestamp
                images.addAll(newImages)
                when (type) {
                    org.sourcei.wallup.deprecated.utils.C.LATEST -> Prefs.putString(org.sourcei.wallup.deprecated.utils.C.LATEST, F.toJson(images))
                    org.sourcei.wallup.deprecated.utils.C.RANDOM -> Prefs.putString(org.sourcei.wallup.deprecated.utils.C.RANDOM, F.toJson(images))
                }
                mainAdapter.notifyDataSetChanged()
                mainAdapter.setLoaded()
            }
        }
    }
}
