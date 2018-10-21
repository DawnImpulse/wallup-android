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
import androidx.core.widget.toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.adapters.ImageCollectionAdapter
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.CollectionPojo
import com.dawnimpulse.wallup.pojo.UserPojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.L
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.bottom_sheet_collection.*

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-10-20 by Saksham
 *
 * @note Updates :
 */
class ModalSheetCollection : RoundedBottomSheetDialogFragment() {
    private var NAME = "ModalSheetCollection"
    private var imageCols: List<CollectionPojo>? = null
    private lateinit var cols: MutableList<CollectionPojo>
    private lateinit var model: UnsplashModel
    private lateinit var user: UserPojo
    private lateinit var adapter: ImageCollectionAdapter
    private lateinit var image: String

    // on create
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_collection, container, false)
    }

    // on view created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = UnsplashModel(lifecycle)
        user = Gson().fromJson(Prefs.getString(C.USER, ""), UserPojo::class.java)
        image = arguments!!.getString(C.ID)
        if (arguments!!.containsKey(C.COLLECTIONS)) {
            imageCols = Gson().fromJson(arguments!!.getString(C.COLLECTIONS), Array<CollectionPojo>::class.java).toList()
            L.d(NAME, imageCols!!.size)
        } else
            imageCols = null



        getCollections()
        sheetColRefresh.setOnClickListener {
            sheetColRefresh.visibility = View.GONE
            sheetColProgress.visibility = View.VISIBLE
            getCollections()
        }
    }

    // get user collections
    private fun getCollections() {
        model.userCollections(user.username, 1, 30) { e, r ->
            e?.let {
                L.d(NAME, e)
                context!!.toast("error fetching user collections")
                sheetColRefresh.visibility = View.VISIBLE
                sheetColProgress.visibility = View.GONE
            }
            r?.let { r ->
                cols = (r as List<CollectionPojo>).toMutableList()
                adapter = if (imageCols != null) {
                    ImageCollectionAdapter(lifecycle, cols, imageCols!!.map { it.id }, image)
                } else
                    ImageCollectionAdapter(lifecycle, cols, imageCols, image)

                sheetColRecycler.layoutManager = LinearLayoutManager(sheetColRecycler.context)
                sheetColRecycler.adapter = adapter
                sheetColProgress.visibility = View.GONE
                sheetColRecycler.visibility = View.VISIBLE
            }
        }
    }

}