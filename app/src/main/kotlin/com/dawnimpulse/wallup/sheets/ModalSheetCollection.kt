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
package com.dawnimpulse.wallup.sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.core.widget.toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.adapters.ImageCollectionAdapter
import com.dawnimpulse.wallup.interfaces.OnLoadMoreListener
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.CollectionPojo
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.pojo.UserPojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Event
import com.dawnimpulse.wallup.utils.L
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.bottom_sheet_collection.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-10-20 by Saksham
 *
 * @note Updates :
 * Saksham - 2018 12 04 - master - new reload / progress
 */
class ModalSheetCollection : RoundedBottomSheetDialogFragment(), OnLoadMoreListener, View.OnClickListener {
    private var NAME = "ModalSheetCollection"
    private var toView: Boolean = false
    private var imageCols: MutableList<CollectionPojo>? = null
    private var imageColString: MutableList<String?>? = null
    private var originalImageColString: MutableList<String?>? = null
    private var image: ImagePojo? = null
    private var nextPage = 2
    private lateinit var cols: MutableList<CollectionPojo?>
    private lateinit var model: UnsplashModel
    private lateinit var user: UserPojo
    private lateinit var adapter: ImageCollectionAdapter

    // on create
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_collection, container, false)
    }

    // on view created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = UnsplashModel(lifecycle)
        user = Gson().fromJson(Prefs.getString(C.USER, ""), UserPojo::class.java)
        if (arguments!!.containsKey(C.COLLECTIONS)) {
            imageCols = Gson().fromJson(arguments!!.getString(C.COLLECTIONS), Array<CollectionPojo>::class.java).toMutableList()
            imageColString = (imageCols!!.map { it.id }).toMutableList()
            originalImageColString = imageColString
        }
        if (arguments!!.containsKey(C.VIEW)) {
            toView = arguments!!.getBoolean(C.VIEW)
        } else {
            image = Gson().fromJson(arguments!!.getString(C.IMAGE_POJO), ImagePojo::class.java)
        }

        getCollections()
        sheetColReload.setOnClickListener(this)

        sheetColProgressI.animation = AnimationUtils.loadAnimation(context, R.anim.rotation_progress)
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
        sheetColReload.isVisible = false
        sheetColProgress.isVisible = true
        getCollections()
    }

    // on load more
    override fun onLoadMore() {
        cols.add(null)
        adapter.notifyItemInserted(cols.size)
        model.userCollections(user.username, nextPage, 30) { e, r ->
            e?.let {
                L.d(NAME, e)
                context!!.toast("error fetching user collections")
            }
            r?.let {
                nextPage++
                cols.removeAt(cols.size - 1)
                adapter.notifyItemRemoved(cols.size - 1)
                var list = (r as List<CollectionPojo?>).toMutableList()
                var temp = imageCollections(originalImageColString, list)
                cols.addAll(list)
                imageColString!!.addAll(temp)
                adapter.notifyDataSetChanged()
                adapter.setLoaded()
            }
        }
    }

    // on message event
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: Event) {
        if (event.obj.has(C.TYPE)) {
            if (event.obj.getString(C.TYPE) == C.IMAGE_TO_COLLECTION) {
                // if image is added to a collection
                if (event.obj.getBoolean(C.IS_ADDED)) {
                    val pos = event.obj.getInt(C.POSITION)
                    imageColString!!.add(pos, event.obj.getString(C.COLLECTION_ID))
                    cols[pos]!!.cover_photo = image
                    adapter.notifyItemChanged(event.obj.getInt(C.POSITION) + 1)
                } else {
                    //if image is removed from collection
                    val pos = event.obj.getInt(C.POSITION)
                    imageColString!![event.obj.getInt(C.POSITION)] = null
                    if (cols[pos]!!.preview_photos != null)
                        cols[pos]!!.cover_photo = ImagePojo(urls = cols[pos]!!.preview_photos!![0].urls)
                    else
                        cols[pos]!!.cover_photo = null
                    adapter.notifyItemChanged(event.obj.getInt(C.POSITION) + 1)
                }
            }
            if (event.obj.getString(C.TYPE) == C.NEW_COLLECTION) {
                val col = Gson().fromJson(event.obj.getString(C.COLLECTION), CollectionPojo::class.java)
                cols.add(0, col)
                adapter.notifyDataSetChanged()
            }
        }
    }

    // get user collections
    private fun getCollections() {
        model.userCollections(user.username, 1, 30) { e, r ->
            e?.let {
                L.d(NAME, e)
                context!!.toast("error fetching user collections")
                sheetColProgress.isVisible = false
                sheetColReload.isVisible = true
            }
            r?.let { r ->
                var id: String? = null
                cols = (r as List<CollectionPojo>).toMutableList()
                image?.let {
                    id = it.id
                }
                if (cols != null || cols!!.isNotEmpty())
                    imageColString = imageCollections(imageColString, cols)
                adapter = if (imageCols != null) {
                    ImageCollectionAdapter(lifecycle, cols, imageColString, id, sheetColRecycler)
                } else
                    ImageCollectionAdapter(lifecycle, cols, imageColString, id, sheetColRecycler)

                sheetColRecycler.layoutManager = LinearLayoutManager(sheetColRecycler.context)
                sheetColRecycler.adapter = adapter
                (sheetColRecycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
                sheetColProgress.visibility = View.GONE
                sheetColRecycler.visibility = View.VISIBLE
                adapter.setOnLoadMoreListener(this)
            }
        }
    }

    //create image collection array
    private fun imageCollections(imageCols: MutableList<String?>?,
                                 cols: MutableList<CollectionPojo?>): MutableList<String?> {
        var i = 0
        var list: MutableList<String?> = ArrayList()
        while (i < cols.size) {
            var done = false
            imageCols?.let {
                for (image in it) {
                    if (image == cols[i]!!.id) {
                        list.add(image)
                        done = true
                        break
                    }
                }
            }
            if (!done)
                list.add(null)
            i++
        }
        return list
    }

}