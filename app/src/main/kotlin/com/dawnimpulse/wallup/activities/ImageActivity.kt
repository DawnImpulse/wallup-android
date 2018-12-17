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
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.toast
import androidx.palette.graphics.Palette
import com.dawnimpulse.permissions.android.Permissions
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.handlers.ColorHandler
import com.dawnimpulse.wallup.handlers.DateHandler
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.handlers.WallpaperHandler
import com.dawnimpulse.wallup.models.UnsplashModel
import com.dawnimpulse.wallup.pojo.CollectionPojo
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.sheets.ModalSheetCollection
import com.dawnimpulse.wallup.sheets.ModalSheetExif
import com.dawnimpulse.wallup.sheets.ModalSheetStats
import com.dawnimpulse.wallup.sheets.ModalSheetUnsplash
import com.dawnimpulse.wallup.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_image.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject

/**
 * @author Saksham
 *
 * @note Last Branch Update - recent
 * @note Created on 2018-05-24 by Saksham
 *
 * @note Updates :
 *  Saksham - 2018 05 25 - recent - working with single image display
 *  Saksham - 2018 07 20 - recent - adding listeners
 *  Saksham - 2018 07 26 - recent - downloading
 *  Saksham - 2018 09 01 - master - exif bottom sheet
 *  Saksham - 2018 09 06 - master - unsplash & image share handling
 *  Saksham - 2018 09 15 - master - handling direct unsplash links
 *  Saksham - 2018 09 20 - master - ML for tags
 *  Saksham - 2018 10 20 - master - Add to collection
 *  Saksham - 2018 11 28 - master - Connection handling
 *  Saksham - 2018 12 04 - master - Long click icons
 *  Saksham - 2018 12 09 - master - Image stats
 */
class ImageActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener {
    private val NAME = "ImageActivity"
    private var setBitmap = false
    private var bitmap: Bitmap? = null
    //private var fullDetails: ImagePojo? = null
    private var color: Int = 0
    private var position = -1
    private var like = false //state of like button
    private var likeStateChange = false //since we make multiple calls we set like once
    private var details: ImagePojo? = null
    private var dialogOpen = false
    private lateinit var model: UnsplashModel
    private lateinit var exifSheet: ModalSheetExif
    private lateinit var loginSheet: ModalSheetUnsplash
    private lateinit var colSheet: ModalSheetCollection
    private lateinit var statsSheet: ModalSheetStats

    // On create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        color = Colors(this).WHITE
        val point = F.displayDimensions(this)
        imageL.layoutParams = RelativeLayout.LayoutParams(point.x, (point.y * 0.8).toInt())

        model = UnsplashModel(lifecycle)
        exifSheet = ModalSheetExif()
        loginSheet = ModalSheetUnsplash()
        colSheet = ModalSheetCollection()
        statsSheet = ModalSheetStats()

        // checking to handle the app links
        if (intent.hasExtra(C.IMAGE_POJO)) {
            var params = intent.extras
            details = Gson().fromJson(params.getString(C.IMAGE_POJO), ImagePojo::class.java)

            getImageDetails(details!!.id)
            setImageDetails(details!!)
        } else {
            val appLinkData = intent.data
            getImageDetails(appLinkData.lastPathSegment)
        }

        position = intent.getIntExtra(C.POSITION, -1)

        imagePreviewWallpaper.setOnClickListener(this)
        imagePreviewDownload.setOnClickListener(this)
        imagePreviewAuthorL.setOnClickListener(this)
        imagePreviewExif.setOnClickListener(this)
        imagePreviewShare.setOnClickListener(this)
        imagePreviewStats.setOnClickListener(this)
        imagePreviewUnsplash.setOnClickListener(this)
        imagePreviewLikeL.setOnClickListener(this)
        imagePreviewCollect.setOnClickListener(this)
        imageBack.setOnClickListener(this)

        // long press
        imagePreviewAuthorL.setOnLongClickListener(this)
        imagePreviewWallpaper.setOnLongClickListener(this)
        imagePreviewShare.setOnLongClickListener(this)
        imagePreviewExif.setOnLongClickListener(this)
        imagePreviewCollect.setOnLongClickListener(this)
        imagePreviewUnsplash.setOnLongClickListener(this)
        imagePreviewStats.setOnLongClickListener(this)

        //Ripple.add(Colors(this).GREY_400, imagePreviewStats)
        imageHover.animation = AnimationUtils.loadAnimation(this, R.anim.hover)
        imageScroll.viewTreeObserver.addOnScrollChangedListener {
            imageHover.clearAnimation()
            imageHover.gone()
        }
    }

    // on start
    override fun onStart() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
        super.onStart()
    }

    // on resume for dialog only
    override fun onResume() {
        super.onResume()

        if (dialogOpen) {
            Dialog.download(this, details!!.id, details!!.urls!!.full)
        }
    }

    // on destroy
    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    // On click for various buttons
    override fun onClick(v: View) {
        when (v.id) {
            imagePreviewWallpaper.id -> {
                imagePreviewProgress.visibility = View.VISIBLE
                Permissions.askWriteExternalStoragePermission(this) { no, _ ->
                    if (no != null) {
                        Toast.short(this@ImageActivity, "Kindly provide external storage permission in Settings")
                        imagePreviewProgress.visibility = View.GONE
                    } else {
                        if (bitmap != null) {
                            WallpaperHandler.setHomescreenWallpaper(this@ImageActivity, bitmap!!)
                            imagePreviewProgress.visibility = View.GONE
                        } else {
                            toast("Waiting for High Quality Image ...")
                            ImageHandler.getImageAsBitmap(lifecycle, this, details!!.urls!!.full) {
                                WallpaperHandler.setHomescreenWallpaper(this@ImageActivity, it)
                                imagePreviewProgress.visibility = View.GONE
                            }
                        }
                        model.downloadedPhoto(details!!.links!!.download_location)
                    }
                }
            }
            imagePreviewDownload.id -> {
                /*Dialog.download(this)*/

                Permissions.askWriteExternalStoragePermission(this) { no, _ ->
                    if (no != null)
                        Toast.short(this@ImageActivity, "Kindly provide external storage permission in Settings")
                    else {
                        Dialog.download(this, details!!.id, details!!.urls!!.raw)
                        //DownloadHandler.downloadData(this, details!!.links!!.download, details!!.id)
                        //Toast.short(this, "Downloading Image in /Downloads/Wallup/${details!!.id}.jpg .Check notification for progress.")
                        model.downloadedPhoto(details!!.links!!.download_location)
                    }
                }
            }
            imagePreviewAuthorL.id -> {
                var intent = Intent(this, ArtistProfileActivity::class.java)
                intent.putExtra(C.USERNAME, details!!.user!!.username)
                startActivity(intent)
            }
            imagePreviewExif.id -> {
                if (details != null) {
                    var bundle = bundleOf(Pair(C.IMAGE_POJO, Gson().toJson(details)))
                    if (color != 0) bundle.putInt(C.COLOR, color)

                    exifSheet.arguments = bundle
                    exifSheet.show(supportFragmentManager, exifSheet.tag)
                } else
                    toast("kindly wait for image details to be loaded")
            }
            imagePreviewShare.id -> {
                imagePreviewProgress.visibility = View.VISIBLE
                Permissions.askWriteExternalStoragePermission(this) { no, _ ->
                    if (no != null) {
                        Toast.short(this@ImageActivity, "Kindly provide external storage permission in Settings")
                        imagePreviewProgress.visibility = View.GONE
                    } else {
                        if (bitmap != null) {
                            ImageHandler.shareImage(this, bitmap!!, details!!.id, F.unsplashImage(details!!.id))
                            imagePreviewProgress.visibility = View.GONE
                        } else {
                            toast("Waiting for High Quality Image ...")
                            ImageHandler.getImageAsBitmap(lifecycle, this, details!!.urls!!.full) {
                                bitmap = it
                                ImageHandler.shareImage(this, it, details!!.id, F.unsplashImage(details!!.id))
                                imagePreviewProgress.visibility = View.GONE
                            }
                        }
                        model.downloadedPhoto(details!!.links!!.download_location)
                    }
                }
            }
            imagePreviewStats.id -> {
                var bundle = bundleOf(Pair(C.ID, details!!.id), Pair(C.COLOR, color))
                statsSheet.arguments = bundle
                statsSheet.show(supportFragmentManager, statsSheet.tag)
            }
            imagePreviewUnsplash.id -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(F.unsplashImage(details!!.id))))
            imagePreviewLikeL.id -> {
                if (Config.USER_API_KEY.isNotEmpty()) {
                    details?.let {
                        like = !like //changing like status
                        F.like(this, imagePreviewLikeI, it.id, like, color) // change like status

                        // set to details & view
                        if (like) {
                            imagePreviewLikesCount.text = F.fromHtml("<b><font color=\"${color.toHexa()}\">${F.withSuffix(details!!.likes + 1)}+</font></b> Likes")
                            details!!.likes = details!!.likes + 1
                        } else {
                            imagePreviewLikesCount.text = F.fromHtml("<b><font color=\"${color.toHexa()}\">${F.withSuffix(details!!.likes - 1)}+</font></b> Likes")
                            details!!.likes = details!!.likes - 1
                        }

                        // send a event bus to notify all views
                        var obj = JSONObject()
                        obj.put(C.TYPE, C.LIKE)
                        obj.put(C.LIKE, like)
                        obj.put(C.ID, details!!.id)
                        EventBus.getDefault().postSticky(Event(obj))
                    }
                } else
                    loginSheet.show(supportFragmentManager, loginSheet.tag)
            }
            imagePreviewCollect.id -> {
                if (Config.USER_API_KEY.isNotEmpty()) {
                    var bundle = Bundle()
                    details?.let {
                        bundle.putString(C.IMAGE_POJO, Gson().toJson(details!!))
                        it.current_user_collections?.let { cols ->
                            if (cols.isNotEmpty())
                                bundle.putString(C.COLLECTIONS, Gson().toJson(cols))
                        }
                    }
                    colSheet.arguments = bundle
                    colSheet.show(supportFragmentManager, colSheet.tag)
                } else
                    loginSheet.show(supportFragmentManager, loginSheet.tag)
            }
            imageBack.id -> finish()
        }
    }

    // on long click
    override fun onLongClick(v: View?): Boolean {
        when (v!!.id) {
            imagePreviewAuthorL.id -> toast("open photographer's profile")
            imagePreviewWallpaper.id -> toast("set image as wallpaper")
            imagePreviewShare.id -> toast("share image")
            imagePreviewExif.id -> toast("image EXIF info")
            imagePreviewCollect.id -> toast("add image to collection")
            imagePreviewUnsplash.id -> toast("open image on Unsplash website")
            imagePreviewStats.id -> toast("image statistics on unsplash")
        }
        return false
    }

    // On back pressed
    override fun onBackPressed() {
        finish()
    }

    // on message event
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: Event) {
        if (event.obj.has(C.TYPE)) {
            if (event.obj.getString(C.TYPE) == C.IMAGE_TO_COLLECTION) {
                // if image is added to a collection
                if (event.obj.getBoolean(C.IS_ADDED)) {
                    var col = Gson().fromJson(event.obj.getString(C.COLLECTION), CollectionPojo::class.java)
                    details!!.current_user_collections!!.add(0, col)
                    imagePreviewCollectI.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vd_plus_circle))
                } else {
                    //if image is removed from collection
                    var cid = details!!.current_user_collections!!
                            .asSequence()
                            .withIndex()
                            .filter { it.value.id == event.obj.getString(C.COLLECTION_ID) }
                            .map { it.index }
                            .toList()

                    details!!.current_user_collections!!.removeAt(cid[0])
                    if (details!!.current_user_collections == null || details!!.current_user_collections!!.isEmpty())
                        imagePreviewCollectI.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vd_plus))
                }

                if (color != 0)
                    imagePreviewCollectI.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            }
            if (event.obj.getString(C.TYPE) == C.NETWORK) {
                runOnUiThread {
                    if (event.obj.getBoolean(C.NETWORK)) {
                        imageConnLayout.setBackgroundColor(Colors(this).GREEN)
                        imageConnText.text = "Back Online"
                        launch {
                            delay(1500)
                            runOnUiThread {
                                imageConnLayout.visibility = View.GONE
                            }
                        }
                    } else {
                        imageConnLayout.visibility = View.VISIBLE
                        imageConnLayout.setBackgroundColor(Colors(this).LIKE)
                        imageConnText.text = "No Internet"
                    }
                }
            }
            if (event.obj.getString(C.TYPE) == C.DOWNLOAD_PATH) {
                dialogOpen = true
            }
        }
    }

    // get image details
    private fun getImageDetails(id: String) {
        model.getImage(id) { _, details ->
            details?.let {
                this.details = it as ImagePojo
                //fullDetails = it
                setImageDetails(this.details!!)
            }

        }
    }

    // set image details on views
    private fun setImageDetails(details: ImagePojo) {
        imagePreviewLikesCount.text = F.fromHtml("<b><font color=\"#FFFFFF\">${F.withSuffix(details.likes)}+</font></b> Likes")
        imagePreviewAuthorName.text = F.capWord(details.user!!.name)
        imagePreviewAuthorImages.text = F.fromHtml("<b><font color=${color.toHexa()}>${F.withSuffix(details.user!!.total_photos)}</font></b> Photos")
        imagePreviewAuthorCollections.text = F.fromHtml("<b><font color=${color.toHexa()}>${F.withSuffix(details.user!!.total_collections)}</font></b> Collections")
        imagePreviewViewsCount.text = F.withSuffix(details.views)
        imagePreviewDownloadCount.text = F.withSuffix(details.downloads)
        imagePreviewPublishedOn.text = DateHandler.convertForImagePreview(details.created_at)

        // change add icon if user has image in any collection
        details.current_user_collections?.let {
            if (it.isNotEmpty())
                imagePreviewCollectI.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vd_plus_circle))
            else
                imagePreviewCollectI.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vd_plus))
        }

        ImageHandler.setImageInView(lifecycle, imagePreviewAuthorImage, details.user!!.profile_image!!.large)
        ImageHandler.getImageAsBitmap(lifecycle, this, details.urls!!.full + Config.IMAGE_PREVIEW_QUALITY) {
            color = ColorHandler.getNonDarkColor(Palette.from(it).generate(), this)
            color()
            imageMain.setImageBitmap(it)
            imageMain.scaleType = ImageView.ScaleType.CENTER_CROP
            imagePreviewProgress.visibility = View.GONE
            /*ML.labels(it) {
                setTags(it)
            }*/
        }
        //F.underline(imagePreviewStatistics)

        if (details.downloads == 0) {
            imagePreviewDownloadCount.visibility = View.GONE
        } else
            imagePreviewDownloadCount.visibility = View.VISIBLE

        //if (details.liked_by_user && !likeStateChange)
        F.like(this, imagePreviewLikeI, true, details.liked_by_user)

        if (!likeStateChange)
            like = details.liked_by_user
        //likeStateChange = true
    }

    // set color on a resources
    private fun color() {
        var down = imagePreviewDownload.background.current as GradientDrawable
        var wall = imagePreviewWallpaper.background.current as GradientDrawable
        var back = imageBack.background.current as GradientDrawable

        //imagePreviewAuthorImagesL.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        //imagePreviewAuthorCollectionsL.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        imagePreviewShareI.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        imagePreviewExifI.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        imagePreviewStatsI.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        imagePreviewUnsplashI.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        imagePreviewCollectI.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        imagePreviewLikeI.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)

        imagePreviewViewsCount.setTextColor(color)
        imagePreviewPublishedOn.setTextColor(color)
        imagePreviewAuthorImages.text = F.fromHtml("<b><font color=${color.toHexa()}>${F.withSuffix(details!!.user!!.total_photos)}</font></b> Photos")
        imagePreviewAuthorCollections.text = F.fromHtml("<b><font color=${color.toHexa()}>${F.withSuffix(details!!.user!!.total_collections)}</font></b> Collections")
        imagePreviewLikesCount.text = F.fromHtml("<b><font color=\"${color.toHexa()}\">${F.withSuffix(details!!.likes)}+</font></b> Likes")

        //imagePreviewLikeI.drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)

        down.setColor(color)
        back.setColor(color)
        wall.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        //imagePreviewWallpaperT.setTextColor(color)
    }

    /* // set tags
     private fun setTags(tags: List<FirebaseVisionLabel>) {
         imagePreviewTags.visibility = View.VISIBLE
         val sortedTags = F.sortLabels(tags)
         imagePreviewTags.clipToPadding = false
         imagePreviewTags.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
         imagePreviewTags.adapter = TagsAdapter(lifecycle, sortedTags)
     }*/
}
