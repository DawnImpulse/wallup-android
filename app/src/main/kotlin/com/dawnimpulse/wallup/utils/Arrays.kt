package com.dawnimpulse.wallup.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.pojo.IconsPojo
import com.dawnimpulse.wallup.pojo.LibraryPojo

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2018-12-15 by Saksham
 * @note Updates :
 */
object Arrays{
    val LIBRARIES = listOf(
            LibraryPojo("Glide", "An image loading and caching library for Android focused on smooth scrolling.", "https://github.com/bumptech/glide"),
            LibraryPojo("Retrofit", "Type-safe HTTP client for Android and Java by Square, Inc.", "https://github.com/square/retrofit"),
            LibraryPojo("RoundedImageView", "A fast ImageView that supports rounded corners, ovals, and circles.", "https://github.com/vinc3m1/RoundedImageView"),
            LibraryPojo("Calligraphy", "Custom fonts in Android the easy way...", "https://github.com/chrisjenx/Calligraphy"),
            LibraryPojo("AndroidImageCropper", "Image Cropping Library for Android, optimized for Camera / Gallery.", "https://github.com/ArthurHub/Android-Image-Cropper"),
            LibraryPojo("PhotoView", "Implementation of ImageView for Android that supports zooming, by various touch gestures.", "https://github.com/chrisbanes/PhotoView"),
            LibraryPojo("Permissions", "Simple permission handling in Android.", "https://github.com/DawnImpulse/permissions-android"),
            LibraryPojo("EasyPrefs", "A small library containing a wrapper/helper for the Shared Preferences of android.", "https://github.com/Pixplicity/EasyPrefs"),
            LibraryPojo("AutoFitTextView", "A TextView that automatically resizes text to fit perfectly within its bounds.", "https://github.com/grantland/android-autofittextview"),
            LibraryPojo("EventBus", "Event bus for Android and Java that simplifies communication between Activities, Fragments, Threads, Services, etc. Less code, better quality.", "https://github.com/greenrobot/EventBus"),
            LibraryPojo("MPAndroidChart", "A powerful \uD83D\uDE80 Android chart view / graph view library, supporting line- bar- pie- radar- bubble- and candlestick charts as well as scaling, dragging and animations.", "https://github.com/PhilJay/MPAndroidChart"),
            LibraryPojo("Apache CommonsIO", "Commons IO is a library of utilities to assist with developing IO functionality.", "https://commons.apache.org/proper/commons-io/")
    )

    fun icons(context: Context) : List<IconsPojo>{
        val library = ContextCompat.getDrawable(context, R.drawable.vd_update)

        return listOf(
                IconsPojo(library!!,"dmitri13","flaticon.com","https://www.flaticon.com/free-icon/reload_813310")
        )
    }
}