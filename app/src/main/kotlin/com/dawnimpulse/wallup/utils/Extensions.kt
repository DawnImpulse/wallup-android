package com.dawnimpulse.wallup.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import java.io.File

/**
 * @info - custom kotlin extension functions
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2018-12-04 by Saksham
 * @note Updates :
 */

// int color to hexa string
fun Int.toHexa(): String {
    return String.format("#%06X", 0xFFFFFF and this)
}

// gone view
fun View.gone() {
    visibility = View.GONE
}

// hide view
fun View.hide() {
    visibility = View.INVISIBLE
}

// gone view
fun View.show() {
    visibility = View.VISIBLE
}

// open activity
fun <T> Context.openActivity(it: Class<T>) {
    startActivity(Intent(this, it))
}

// file path string to uri
fun String.toFileUri(): Uri {
    return Uri.fromFile(File(this))
}