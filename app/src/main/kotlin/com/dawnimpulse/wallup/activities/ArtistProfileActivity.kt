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
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.adapters.ArtistPhotosAdapter
import com.dawnimpulse.wallup.adapters.CollectionsHorizontalAdapter
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.CollectionPojo
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.pojo.UserPojo
import com.dawnimpulse.wallup.utils.*
import kotlinx.android.synthetic.main.activity_artist_profile.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-31 by Saksham
 *
 * @note Updates :
 *  Saksham - 2018 08 31 - master - click listener
 *  Saksham - 2018 09 14 - master - inflating collections
 *  Saksham - 2018 11 28 - master - connection handling
 *  Saksham - 2018 12 04 - master - new reload / progress
 */
class ArtistProfileActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var userPojo: UserPojo
    lateinit var model: UnsplashModel
    private val NAME = "ArtistProfileActivity"

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_profile)

        model = UnsplashModel(lifecycle)
        model.userDetails(intent.extras.getString(C.USERNAME)) { error, details ->
            error?.let {
                L.d(NAME, error.toString())
                artistProgress.visibility = View.GONE
                artistReload.isVisible = true
                Toast.short(this, "Error Occurred")
            }
            details?.let {
                userPojo = details as UserPojo
                details()
                artistLayout.visibility = View.VISIBLE
                artistUnsplash.visibility = View.VISIBLE
                artistProgress.visibility = View.GONE
                artistReload.isVisible = false
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
        artistCollectionMore.setOnClickListener(this)
        artistUnsplash.setOnClickListener(this)
        artistUrl.setOnClickListener(this)
        artistBack.setOnClickListener(this)
        artistReload.setOnClickListener(this)

        artistProgressI.animation = AnimationUtils.loadAnimation(this, R.anim.rotation_progress)
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
            artistPhotosMore.id -> {
                var intent = Intent(this, GeneralImagesActivity::class.java)
                intent.putExtra(C.TYPE, C.ARTIST_IMAGES)
                intent.putExtra(C.USERNAME, userPojo.username)
                startActivity(intent)
            }
            artistCollectionMore.id -> {
                openActivity(GeneralCollectionsActivity::class.java) {
                    putString(C.USERNAME, userPojo.username)
                }
            }
            artistUnsplash.id ->
                F.startWeb(this, F.unsplashUser(userPojo.username))
            artistUrl.id ->
                F.startWeb(this, userPojo.portfolio_url!!)
            artistBack.id -> finish()
            artistReload.id -> {
                artistReload.isVisible = false
                artistProgress.isVisible = true
                model.userDetails(intent.extras.getString(C.USERNAME)) { error, details ->
                    error?.let {
                        L.d(NAME, error.toString())
                        artistProgress.visibility = View.GONE
                        artistReload.isVisible = true
                        Toast.short(this, "Error Occurred")
                    }
                    details?.let {
                        userPojo = details as UserPojo
                        details()
                        artistLayout.visibility = View.VISIBLE
                        artistUnsplash.visibility = View.VISIBLE
                        artistProgress.visibility = View.GONE
                        artistReload.isVisible = false
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
            }

        }
    }

    // on message event
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: Event) {
        if (event.obj.has(C.TYPE)) {
            if (event.obj.getString(C.TYPE) == C.NETWORK) {
                runOnUiThread {
                    if (event.obj.getBoolean(C.NETWORK)) {
                        artistConnLayout.setBackgroundColor(Colors(this).GREEN)
                        artistConnText.text = "Back Online"
                        launch {
                            delay(1500)
                            runOnUiThread {
                                artistConnLayout.visibility = View.GONE
                            }
                        }
                    } else {
                        artistConnLayout.visibility = View.VISIBLE
                        artistConnLayout.setBackgroundColor(Colors(this).LIKE)
                        artistConnText.text = "No Internet"
                    }
                }
            }
        }
    }

    // setting details
    private fun details() {
        artistUsername.text = "@${userPojo.username}"
        artistFirstName.text = F.capWord(userPojo.first_name)
        artistLastName.text = userPojo.last_name?.let { F.capWord(it) }
        artistPhotoNo.text = userPojo.total_photos.toString()
        artistCollectionNo.text = userPojo.total_collections.toString()
        artistLikesNo.text = userPojo.total_likes.toString()
        F.underline(artistUsername)

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
