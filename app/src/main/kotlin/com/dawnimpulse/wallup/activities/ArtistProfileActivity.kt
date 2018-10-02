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
package com.dawnimpulse.wallup.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.adapters.ArtistPhotosAdapter
import com.dawnimpulse.wallup.adapters.CollectionsHorizontalAdapter
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.CollectionPojo
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.pojo.UserPojo
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.F
import com.dawnimpulse.wallup.utils.L
import com.dawnimpulse.wallup.utils.Toast
import kotlinx.android.synthetic.main.activity_artist_profile.*


/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-31 by Saksham
 *
 * @note Updates :
 *  Saksham - 2018 08 31 - master - click listener
 *  Saksham - 2018 09 14 - master - inflating collections
 */
class ArtistProfileActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var userPojo: UserPojo
    private val NAME = "ArtistProfileActivity"

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_profile)

        var model = UnsplashModel(lifecycle)
        model.userDetails(intent.extras.getString(C.USERNAME)) { error, details ->
            error?.let {
                L.d(NAME, error.toString())
                Toast.short(this, "Error Occurred")
            }
            details?.let {
                userPojo = details as UserPojo
                details()
                artistLayout.visibility = View.VISIBLE
                artistUnsplash.visibility = View.VISIBLE
                artistProgress.visibility = View.GONE
            }
        }
        model.userPhotos(1, 8, intent.extras.getString(C.USERNAME)) { error, details ->
            error?.let {
                L.d(NAME, error.toString())
                Toast.short(this, "Error Occurred In Photos")
                artistPhotosL.visibility = View.GONE
            }
            details?.let {
                if ((details as List<ImagePojo>).size != 0) {
                    var adapter = ArtistPhotosAdapter(this@ArtistProfileActivity, lifecycle, details as List<ImagePojo?>)
                    artistPhotos.layoutManager = LinearLayoutManager(this@ArtistProfileActivity, LinearLayoutManager.HORIZONTAL, false)
                    artistPhotos.adapter = adapter
                    artistPhotos.clipToPadding = false
                } else
                    artistPhotosL.visibility = View.GONE
            }
        }
        model.userCollections(intent.getStringExtra(C.USERNAME), 1, 8) { e, r ->
            e?.let {
                L.d(NAME, e.toString())
                Toast.short(this, "Error Occurred In Collections")
                artistCollectionL.visibility = View.GONE
            }
            r?.let {
                if ((r as List<CollectionPojo>).size != 0) {
                    var adapter = CollectionsHorizontalAdapter(lifecycle, r)
                    artistCollectionRecycler.layoutManager = LinearLayoutManager(this@ArtistProfileActivity, LinearLayoutManager.HORIZONTAL, false)
                    artistCollectionRecycler.adapter = adapter
                    artistCollectionRecycler.clipToPadding = false
                } else
                    artistCollectionL.visibility = View.GONE

            }
        }

        artistPhotosMore.setOnClickListener(this)
        artistUnsplash.setOnClickListener(this)
        artistUrl.setOnClickListener(this)
    }

    // on click
    override fun onClick(v: View) {
        when (v.id) {
            artistPhotosMore.id -> {
                var intent = Intent(this, GeneralImagesActivity::class.java)
                intent.putExtra(C.TYPE, C.ARTIST_IMAGES)
                intent.putExtra(C.USERNAME, userPojo.username)
                startActivity(intent)
            }
            artistUnsplash.id ->
                F.startWeb(this, F.unsplashUser(userPojo.username))
            artistUrl.id ->
                F.startWeb(this, userPojo.portfolio_url!!)

        }
    }

    // setting details
    private fun details() {
        artistUsername.text = "@ ${userPojo.username}"
        artistFirstName.text = userPojo.first_name
        artistLastName.text = userPojo.last_name
        artistPhotoNo.text = userPojo.total_photos.toString()
        artistCollectionNo.text = userPojo.total_collections.toString()
        artistLikesNo.text = userPojo.total_likes.toString()

        if (userPojo.bio != null)
            artistInfo.text = userPojo.bio
        else
            artistInfo.visibility = View.GONE

        if (userPojo.portfolio_url != null) {
            artistUrl.text = userPojo.portfolio_url
            F.underline(artistUrl)
        } else
            artistUrlL.visibility = View.GONE

        if (userPojo.location != null)
            artistLocation.text = userPojo.location
        else
            artistLocationL.visibility = View.GONE

        ImageHandler.setImageInView(lifecycle, artistImage, userPojo.profile_image!!.large)
    }
}
