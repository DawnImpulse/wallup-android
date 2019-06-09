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
package org.sourcei.wallup.deprecated.sheets

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.bottom_sheet_collection.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.sourcei.wallup.deprecated.R
import org.sourcei.wallup.deprecated.utils.L
import org.sourcei.wallup.deprecated.utils.toast

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-10-20 by Saksham
 *
 * @note Updates :
 * Saksham - 2018 12 04 - master - new reload / progress
 */
class ModalSheetCollection : org.sourcei.wallup.deprecated.sheets.RoundedBottomSheetDialogFragment(), org.sourcei.wallup.deprecated.interfaces.OnLoadMoreListener, View.OnClickListener {
    private var NAME = "ModalSheetCollection"
    private var toView: Boolean = false
    private var imageCols: MutableList<org.sourcei.wallup.deprecated.pojo.CollectionPojo>? = null
    private var imageColString: MutableList<String?>? = null
    private var originalImageColString: MutableList<String?>? = null
    private var image: org.sourcei.wallup.deprecated.pojo.ImagePojo? = null
    private var nextPage = 2
    private lateinit var cols: MutableList<org.sourcei.wallup.deprecated.pojo.CollectionPojo?>
    private lateinit var model: org.sourcei.wallup.deprecated.models.UnsplashModel
    private lateinit var user: org.sourcei.wallup.deprecated.pojo.UserPojo
    private lateinit var adapter: org.sourcei.wallup.deprecated.adapters.ImageCollectionAdapter

    // on create
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_collection, container, false)
    }

    // on view created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = org.sourcei.wallup.deprecated.models.UnsplashModel(lifecycle)
        user = Gson().fromJson(Prefs.getString(org.sourcei.wallup.deprecated.utils.C.USER, ""), org.sourcei.wallup.deprecated.pojo.UserPojo::class.java)
        if (arguments!!.containsKey(org.sourcei.wallup.deprecated.utils.C.COLLECTIONS)) {
            imageCols = Gson().fromJson(arguments!!.getString(org.sourcei.wallup.deprecated.utils.C.COLLECTIONS), Array<org.sourcei.wallup.deprecated.pojo.CollectionPojo>::class.java).toMutableList()
            imageColString = (imageCols!!.map { it.id }).toMutableList()
            originalImageColString = imageColString
        }
        if (arguments!!.containsKey(org.sourcei.wallup.deprecated.utils.C.VIEW)) {
            toView = arguments!!.getBoolean(org.sourcei.wallup.deprecated.utils.C.VIEW)
        } else {
            image = Gson().fromJson(arguments!!.getString(org.sourcei.wallup.deprecated.utils.C.IMAGE_POJO), org.sourcei.wallup.deprecated.pojo.ImagePojo::class.java)
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
                var list = (r as List<org.sourcei.wallup.deprecated.pojo.CollectionPojo?>).toMutableList()
                list = removeCols(0, originalImageColString, list)
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
    fun onEvent(event: org.sourcei.wallup.deprecated.utils.Event) {
        if (event.obj.has(org.sourcei.wallup.deprecated.utils.C.TYPE)) {
            if (event.obj.getString(org.sourcei.wallup.deprecated.utils.C.TYPE) == org.sourcei.wallup.deprecated.utils.C.IMAGE_TO_COLLECTION) {
                // if image is added to a collection
                if (event.obj.getBoolean(org.sourcei.wallup.deprecated.utils.C.IS_ADDED)) {
                    val pos = event.obj.getInt(org.sourcei.wallup.deprecated.utils.C.POSITION)
                    imageColString!!.add(pos, event.obj.getString(org.sourcei.wallup.deprecated.utils.C.COLLECTION_ID))
                    cols[pos]!!.cover_photo = image
                    adapter.notifyItemChanged(event.obj.getInt(org.sourcei.wallup.deprecated.utils.C.POSITION) + 1)
                } else {
                    //if image is removed from collection
                    val pos = event.obj.getInt(org.sourcei.wallup.deprecated.utils.C.POSITION)
                    imageColString!![pos] = null
                    // there can be no preview image for collection which are forcibly moved on top
                    if (cols[pos]!!.preview_photos != null) {
                        if (cols[pos]!!.preview_photos!!.size > 1)
                        // the preview image [0] & cover photo returned by unsplash is same
                            if (cols[pos]!!.cover_photo!!.urls!! == cols[pos]!!.preview_photos!![0].urls)
                                cols[pos]!!.cover_photo = org.sourcei.wallup.deprecated.pojo.ImagePojo(urls = cols[pos]!!.preview_photos!![1].urls)
                            else
                                cols[pos]!!.cover_photo = org.sourcei.wallup.deprecated.pojo.ImagePojo(urls = cols[pos]!!.preview_photos!![0].urls)
                        else
                            cols[pos]!!.cover_photo = image
                    } else
                        cols[pos]!!.cover_photo = image
                    adapter.notifyItemChanged(event.obj.getInt(org.sourcei.wallup.deprecated.utils.C.POSITION) + 1)
                }
            }
            if (event.obj.getString(org.sourcei.wallup.deprecated.utils.C.TYPE) == org.sourcei.wallup.deprecated.utils.C.NEW_COLLECTION) {
                val col = Gson().fromJson(event.obj.getString(org.sourcei.wallup.deprecated.utils.C.COLLECTION), org.sourcei.wallup.deprecated.pojo.CollectionPojo::class.java)
                cols.add(0, col)
                imageColString?.add(null)
                adapter.notifyDataSetChanged()
            }
            if (event.obj.getString(org.sourcei.wallup.deprecated.utils.C.TYPE) == org.sourcei.wallup.deprecated.utils.C.DELETE_COLLECTION) {
                val i = event.obj.getInt(org.sourcei.wallup.deprecated.utils.C.POSITION)
                val id = event.obj.getString(org.sourcei.wallup.deprecated.utils.C.DELETE_COLLECTION)
                cols.removeAt(i)
                imageColString!!.removeAt(i)
                adapter.notifyItemRemoved(i + 1)
                GlobalScope.launch {
                    delay(500)
                    (context as Activity).runOnUiThread {
                        adapter.notifyDataSetChanged()
                        sheetColRecycler.smoothScrollToPosition(i)
                    }
                }
                model.deleteCollection(context!!, id)
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
                cols = (r as List<org.sourcei.wallup.deprecated.pojo.CollectionPojo>).toMutableList()
                image?.let {
                    id = it.id
                }
                var skip = -1
                if (cols != null || cols!!.isNotEmpty()) {
                    imageCols?.let {
                        imageCols = populateCoverPhoto(image!!, imageCols, cols)
                        it.forEachIndexed { i, col ->
                            cols.add(i, col)
                        }
                        skip = it.size - 1
                    }
                }
                cols = removeCols(skip, originalImageColString, cols)
                imageColString = imageCollections(imageColString, cols)
                adapter = if (imageCols != null) {
                    org.sourcei.wallup.deprecated.adapters.ImageCollectionAdapter(lifecycle, cols, imageColString, id, sheetColRecycler)
                } else
                    org.sourcei.wallup.deprecated.adapters.ImageCollectionAdapter(lifecycle, cols, imageColString, id, sheetColRecycler)

                sheetColRecycler.layoutManager = LinearLayoutManager(sheetColRecycler.context)
                sheetColRecycler.adapter = adapter
                (sheetColRecycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
                sheetColProgress.visibility = View.GONE
                sheetColRecycler.visibility = View.VISIBLE
                adapter.setOnLoadMoreListener(this)
            }
        }
    }

    //create image collection <String> of id - array
    private fun imageCollections(imageCols: MutableList<String?>?,
                                 cols: MutableList<org.sourcei.wallup.deprecated.pojo.CollectionPojo?>): MutableList<String?> {
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

    //remove image cols from modified cols
    // skip start from -1 , 0 means 1 item
    private fun removeCols(skip: Int, imageCols: MutableList<String?>?, cols: MutableList<org.sourcei.wallup.deprecated.pojo.CollectionPojo?>): MutableList<org.sourcei.wallup.deprecated.pojo.CollectionPojo?> {
        imageCols?.let {
            val indexes = ArrayList<Int>()
            cols.forEachIndexed { i, pojo ->
                if (i > skip) {
                    if (originalImageColString!!.contains(pojo!!.id))
                        indexes.add(i)
                }
            }
            indexes.forEach { i ->
                if (i < cols.size)
                    cols.removeAt(i)
            }
            return cols
        }
        return cols
    }

    // populate image cols with cover photo
    // doing this since imageCols doesn't contain cover photo (as we are moving them on top)
    // if we find the collection from imageCols in cols then we populate image
    // else we populate it with current image
    private fun populateCoverPhoto(image: org.sourcei.wallup.deprecated.pojo.ImagePojo, imageCols: MutableList<org.sourcei.wallup.deprecated.pojo.CollectionPojo>?, cols: MutableList<org.sourcei.wallup.deprecated.pojo.CollectionPojo?>): MutableList<org.sourcei.wallup.deprecated.pojo.CollectionPojo>? {
        var newImageCols = imageCols
        imageCols!!.forEachIndexed { i, col ->
            val cols2 = cols.filter { it -> it!!.id == col.id }
            if (cols2.isNotEmpty()) {
                newImageCols!![i].cover_photo = cols2[0]!!.cover_photo
                newImageCols!![i].preview_photos = cols2[0]!!.preview_photos
            } else
                newImageCols!![i].cover_photo = image
        }
        return newImageCols
    }

}