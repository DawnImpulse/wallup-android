/**
 * ISC License
 *
 * Copyright 2020, Saksham (DawnImpulse)
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
 * WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
 * OR PERFORMANCE OF THIS SOFTWARE.
 **/
package com.dawnimpulse.wallup.utils.reusables

import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.palette.graphics.Palette
import com.dawnimpulse.wallup.BuildConfig
import com.dawnimpulse.wallup.ui.App
import com.dawnimpulse.wallup.utils.handlers.HandlerColor
import com.dawnimpulse.wallup.utils.handlers.HandlerTransform
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import java.io.File


/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-03-06 by Saksham
 * @note Updates :
 */

lateinit var Prefs: SharedPreferences

/**
 * run on main looper
 */
fun onMain(fn: () -> Unit) {
    Handler(Looper.getMainLooper()).post(fn)
}

// add entry in shared preference
fun SharedPreferences.putAny(name: String, any: Any) {
    when (any) {
        is String -> edit().putString(name, any).apply()
        is Int -> edit().putInt(name, any).apply()
        is Boolean -> edit().putBoolean(name, any).apply()

        // also accepts Float, Long & StringSet
    }
}

// remove entry from shared preference
fun SharedPreferences.remove(name: String) {
    edit().remove(name).apply()
}

/**
 * view gone
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * view hide
 */
fun View.hide() {
    visibility = View.INVISIBLE
}

/**
 * view show
 */
fun View.show() {
    visibility = View.VISIBLE
}

/**
 * toast on fragment
 */
fun Fragment.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(activity, message, length).show()
}

/**
 * debug toast on fragment
 */
fun Fragment.toastd(message: String, length: Int = Toast.LENGTH_SHORT) {
    if (BuildConfig.DEBUG)
        Toast.makeText(activity, message, length).show()
}

/**
 * toast
 */
fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

/**
 * debug toast
 */
fun Context.toastd(message: String, length: Int = Toast.LENGTH_SHORT) {
    if (BuildConfig.DEBUG)
        Toast.makeText(this, message, length).show()
}

/**
 * convert dp to px
 *
 * @param value
 */
fun Context.dpToPx(value: Int): Int {
    return F.dpToPx(value, this)
}

/**
 * open activity
 */
fun <T> Context.openActivity(it: Class<T>) {
    startActivity(Intent(this, it))
}

/**
 * start activity with params
 */
fun <T> Context.openActivity(it: Class<T>, bundle: Bundle.() -> Unit = {}) {
    var intent = Intent(this, it)
    intent.putExtras(Bundle().apply(bundle))
    startActivity(intent)
}

/**
 * start web
 */
fun Context.startWeb(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}

/**
 * open bottom sheet
 */
fun BottomSheetDialogFragment.open(supportFragmentManager: FragmentManager) {
    this.show(supportFragmentManager, this.tag)
}

// int color to hexa string
fun Int.toHexa(): String {
    return String.format("#%06X", 0xFFFFFF and this)
}

/**
 * convert dp to px
 */
fun Int.dpToPx(): Int {
    return F.dpToPx(this, App.context)
}

/**
 * transform handler on string for image (fetch)
 */
fun String.imageTransform(view: ImageView): HandlerTransform {
    return HandlerTransform(this, view)
}

/**
 * transform handler on string for image (bitmap)
 */
fun String.imageTransform(context: Context): HandlerTransform {
    return HandlerTransform(this, context)
}

/**
 * convert safely from json to class
 */
fun <T> String.fromSafeJson(classOfT: Class<T>): T? {
    return try {
        Gson().fromJson(this, classOfT)
    } catch (e: Exception) {
        null
    }
}

/**
 * file path string to uri
 */
fun String.toFileUri(): Uri {
    return Uri.fromFile(File(this))
}

/**
 * copy string
 */
fun Context.copy(text: String) {
    val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
    val clip = ClipData.newPlainText(BuildConfig.APPLICATION_ID, text)
    clipboard?.setPrimaryClip(clip)
}

/**
 * set text color
 */
fun TextView.color(color: Int) {
    this.setTextColor(color)
}

/**
 * bitmap get palette
 */
fun Bitmap.getPalette(): Palette {
    return Palette.from(this).generate()
}

/**
 * get non dark color
 */
fun Palette.getNonDarkColor(): Int {
    return HandlerColor.getNonDarkColor(this, App.context)
}

/**
 * get vibrant color from palette
 */
fun Palette.vibrant(): Int {
    return HandlerColor.getVibrant(this)
}

/**
 * convert any object to json
 */
fun Any.toJson(): String {
    return Gson().toJson(this)
}

// log messages
fun logd(message: Any) {
    if (BuildConfig.DEBUG)
        Log.d("wallup", "${Exception().stackTrace[1].className.replace("${BuildConfig.APPLICATION_ID}.", "")} :: $message")
}

fun loge(message: Any) {
    if (BuildConfig.DEBUG)
        Log.e("wallup", "${Exception().stackTrace[1].className.replace("${BuildConfig.APPLICATION_ID}.", "")} :: $message")
}