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
package org.sourcei.wallup.deprecated.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_collection.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
 * @note Last Branch Update - master
 * @note Created on 2018-09-08 by Saksham
 *
 * @note Updates :
 * Saksham - 2018 11 28 - master - Connection handling
 * Saksham - 2018 12 19 - master - unsplash handling
 */
class CollectionActivity : AppCompatActivity(), View.OnClickListener {
    private var NAME = "CollectionActivity"
    private lateinit var details: org.sourcei.wallup.deprecated.pojo.CollectionPojo
    private lateinit var model: org.sourcei.wallup.deprecated.models.UnsplashModel
    private lateinit var type: String
    private var color = 0

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)

        type = intent.extras!!.getString(org.sourcei.wallup.deprecated.utils.C.TYPE)!!
        if (type == org.sourcei.wallup.deprecated.utils.C.WALLUP)
            type = org.sourcei.wallup.deprecated.utils.C.FEATURED
        model = org.sourcei.wallup.deprecated.models.UnsplashModel(lifecycle)
        details = Gson().fromJson(intent.extras.getString(org.sourcei.wallup.deprecated.utils.C.COLLECTION), org.sourcei.wallup.deprecated.pojo.CollectionPojo::class.java)
        setDetails()

        when (type) {
            org.sourcei.wallup.deprecated.utils.C.FEATURED -> model.collectionPhotos(details.id, 1, 8) { e, r ->
                e?.let {
                    L.d(NAME, e.toString())
                    toast("error fetching images")
                }
                r?.let {
                    var adapter = org.sourcei.wallup.deprecated.adapters.ArtistPhotosAdapter(this, lifecycle, r as List<org.sourcei.wallup.deprecated.pojo.ImagePojo?>)
                    colRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    colRecycler.adapter = adapter
                    colRecycler.clipToPadding = false
                }
            }
            org.sourcei.wallup.deprecated.utils.C.CURATED -> model.curatedCollectionPhotos(details.id, 1, 8) { e, r ->
                e?.let {
                    L.d(NAME, e.toString())
                    toast("error fetching images")
                }
                r?.let {
                    var adapter = org.sourcei.wallup.deprecated.adapters.ArtistPhotosAdapter(this, lifecycle, r as List<org.sourcei.wallup.deprecated.pojo.ImagePojo?>)
                    colRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    colRecycler.adapter = adapter
                    colRecycler.clipToPadding = false
                }
            }
        }

        colMore.setOnClickListener(this)
        colUserImage.setOnClickListener(this)
        colUserImageL.setOnClickListener(this)
        colBack.setOnClickListener(this)
        colUnsplash.setOnClickListener(this)
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
            colMore.id -> {
                var intent = Intent(this, org.sourcei.wallup.deprecated.activities.GeneralImagesActivity::class.java)
                intent.putExtra(org.sourcei.wallup.deprecated.utils.C.TYPE, org.sourcei.wallup.deprecated.utils.C.COLLECTION)
                intent.putExtra(org.sourcei.wallup.deprecated.utils.C.COLLECTION, type)
                intent.putExtra(org.sourcei.wallup.deprecated.utils.C.ID, details.id)
                startActivity(intent)
            }
            colUserImage.id, colUserImageL.id -> {
                var intent = Intent(this, org.sourcei.wallup.deprecated.activities.ArtistProfileActivity::class.java)
                intent.putExtra(org.sourcei.wallup.deprecated.utils.C.USERNAME, details.user!!.username)
                startActivity(intent)
            }
            colBack.id -> finish()
            colUnsplash.id -> F.startWeb(this,details!!.links!!.html + org.sourcei.wallup.deprecated.utils.C.UTM)
        }
    }

    // on message event
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: org.sourcei.wallup.deprecated.utils.Event) {
        if (event.obj.has(org.sourcei.wallup.deprecated.utils.C.TYPE)) {
            if (event.obj.getString(org.sourcei.wallup.deprecated.utils.C.TYPE) == org.sourcei.wallup.deprecated.utils.C.NETWORK) {
                runOnUiThread {
                    if (event.obj.getBoolean(org.sourcei.wallup.deprecated.utils.C.NETWORK)) {
                        colConnLayout.setBackgroundColor(org.sourcei.wallup.deprecated.utils.Colors(this).GREEN)
                        colConnText.text = "Back Online"
                        GlobalScope.launch {
                            delay(1500)
                            runOnUiThread {
                                colConnLayout.visibility = View.GONE
                            }
                        }
                    } else {
                        colConnLayout.visibility = View.VISIBLE
                        colConnLayout.setBackgroundColor(org.sourcei.wallup.deprecated.utils.Colors(this).LIKE)
                        colConnText.text = "No Internet"
                    }
                }
            }
        }
    }

    // setting details in views
    private fun setDetails() {
        var name = F.capWord(details.user!!.name)
        color = org.sourcei.wallup.deprecated.utils.Colors(this).GREY_500
        details.cover_photo?.let {
            color = Color.parseColor(details.cover_photo!!.color!!)
        }
        name = "<font color=\"#ffffff\">${F.firstWord(name)}</font> ${name.replace(F.firstWord(name), "")}"
        colUserName.setTextColor(color)
        colTitle.text = F.capWord(details.title)
        colDescription.text = details.description
        colUserName.setText(F.fromHtml(name), TextView.BufferType.SPANNABLE)
        colImageC.setText(F.fromHtml("${details.total_photos} <font color=\"#ffffff\">photos</font>"), TextView.BufferType.SPANNABLE)
        colImageUpdated.setText(F.fromHtml("<font color=\"#ffffff\">updated on</font> ${F.dateConvert(details.updated_at)}"), TextView.BufferType.SPANNABLE)
        colPublished.setText(F.fromHtml("<font color=\"#ffffff\">published on</font> ${F.dateConvert(details.published_at)}"), TextView.BufferType.SPANNABLE)

        details.cover_photo?.let {
            org.sourcei.wallup.deprecated.handlers.ImageHandler.getImageAsBitmap(lifecycle, this, details.cover_photo!!.urls?.full + org.sourcei.wallup.deprecated.utils.Config.IMAGE_LIST_QUALITY) {
                val color = org.sourcei.wallup.deprecated.handlers.ColorHandler.getNonDarkColor(Palette.from(it).generate(), this)
                colImage.setImageBitmap(it)
                setColor(color)
            }
        }
        org.sourcei.wallup.deprecated.handlers.ImageHandler.setImageInView(lifecycle, colUserImage, details.user?.profile_image!!.large)

        setColor(color)
    }

    // set color on views
    private fun setColor(color: Int) {
        colTitle.setTextColor(color)
        colImageC.setTextColor(color)
        //colDescription.setTextColor(color)
        colUserName.setTextColor(color)
        colImageUpdated.setTextColor(color)
        colPublished.setTextColor(color)

        (colBack.background.current as GradientDrawable).setColor(color)
    }
}
