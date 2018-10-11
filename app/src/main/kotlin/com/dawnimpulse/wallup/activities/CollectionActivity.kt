package com.dawnimpulse.wallup.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.toast
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.adapters.ArtistPhotosAdapter
import com.dawnimpulse.wallup.handlers.ColorHandler
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.CollectionPojo
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Config
import com.dawnimpulse.wallup.utils.F
import com.dawnimpulse.wallup.utils.L
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_collection.*

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-08 by Saksham
 *
 * @note Updates :
 */
class CollectionActivity : AppCompatActivity(), View.OnClickListener {
    private var NAME = "CollectionActivity"
    private lateinit var details: CollectionPojo
    private lateinit var model: UnsplashModel
    private lateinit var type: String
    private var color = 0

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)

        type = intent.extras.getString(C.TYPE)
        if (type == C.WALLUP)
            type = C.FEATURED
        model = UnsplashModel(lifecycle)
        details = Gson().fromJson(intent.extras.getString(C.COLLECTION), CollectionPojo::class.java)
        setDetails()

        when (type) {
            C.FEATURED -> model.collectionPhotos(details.id, 1, 8) { e, r ->
                e?.let {
                    L.d(NAME, e.toString())
                    toast("error fetching images")
                }
                r?.let {
                    var adapter = ArtistPhotosAdapter(this, lifecycle, r as List<ImagePojo?>)
                    colRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    colRecycler.adapter = adapter
                    colRecycler.clipToPadding = false
                }
            }
            C.CURATED -> model.curatedCollectionPhotos(details.id, 1, 8) { e, r ->
                e?.let {
                    L.d(NAME, e.toString())
                    toast("error fetching images")
                }
                r?.let {
                    var adapter = ArtistPhotosAdapter(this, lifecycle, r as List<ImagePojo?>)
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
    }

    // on click
    override fun onClick(v: View) {
        when (v.id) {
            colMore.id -> {
                var intent = Intent(this, GeneralImagesActivity::class.java)
                intent.putExtra(C.TYPE, C.COLLECTION)
                intent.putExtra(C.COLLECTION, type)
                intent.putExtra(C.ID, details.id)
                startActivity(intent)
            }
            colUserImage.id, colUserImageL.id -> {
                var intent = Intent(this, ArtistProfileActivity::class.java)
                intent.putExtra(C.USERNAME, details.user!!.username)
                startActivity(intent)
            }
            colBack.id -> finish()
        }
    }

    // setting details in views
    private fun setDetails() {
        var name = F.capWord(details.user!!.name)
        name = "<font color=\"#ffffff\">${F.firstWord(name)}</font> ${name.replace(F.firstWord(name), "")}"
        color = Color.parseColor(details.cover_photo.color!!)
        colUserName.setTextColor(color)
        colTitle.text = F.capWord(details.title)
        colDescription.text = details.description
        colUserName.setText(F.fromHtml(name), TextView.BufferType.SPANNABLE)
        colImageC.setText(F.fromHtml("${details.total_photos} <font color=\"#ffffff\">photos</font>"), TextView.BufferType.SPANNABLE)
        colImageUpdated.setText(F.fromHtml("<font color=\"#ffffff\">updated on</font> ${F.dateConvert(details.updated_at)}"), TextView.BufferType.SPANNABLE)
        colPublished.setText(F.fromHtml("<font color=\"#ffffff\">published on</font> ${F.dateConvert(details.published_at)}"), TextView.BufferType.SPANNABLE)

        ImageHandler.getImageAsBitmap(lifecycle, this, details.cover_photo.urls?.full + Config.IMAGE_HEIGHT) {
            val color = ColorHandler.getNonDarkColor(Palette.from(it).generate(), this)
            colImage.setImageBitmap(it)
            setColor(color)
        }
        ImageHandler.setImageInView(lifecycle, colUserImage, details.user?.profile_image!!.large)

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
