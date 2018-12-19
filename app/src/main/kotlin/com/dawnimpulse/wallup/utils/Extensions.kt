package com.dawnimpulse.wallup.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import org.json.JSONObject
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

// open activity
fun <T> Context.openActivity(it: Class<T>, bundle: Bundle.() -> Unit = {}) {
    var intent = Intent(this, it)
    intent.putExtras(Bundle().apply(bundle))
    startActivity(intent)
}

// json put params
fun jsonOf(vararg pairs: Pair<String, Any>) = JSONObject().apply {
    pairs.forEach {
        put(it.first, it.second)
    }
}

// file path string to uri
fun String.toFileUri(): Uri {
    return Uri.fromFile(File(this))
}

// tree uri path to file uri path
fun String.toFileString(): String {
    if (this.contains(":")){
        var substring = split(":")
        var tree = substring[0]

        return if (tree.contains("primary"))
            Environment.getExternalStorageDirectory().path + "/${substring[1]}"
        else
            "/storage/${tree.replace("/tree/", "")}/${substring[1]}"
    }else
        return this
}

//convert to content uri
fun Uri.toContentUri(context: Context): Uri {
    val cr = context.contentResolver
    val file = File(this.path)
    val imagePath = file.absolutePath
    val imageName: String? = null
    val imageDescription: String? = null
    val uriString = MediaStore.Images.Media.insertImage(cr, imagePath, imageName, imageDescription)
    return Uri.parse(uriString)
}

//get display ratio a/b
fun Context.displayRatio(): Pair<Int, Int> {
    fun gcd(p: Int, q: Int): Int {
        return if (q == 0) p;
        else gcd(q, p % q);
    }

    val point = F.displayDimensions(this)
    val x = point.x
    val y = point.y
    val gcd = gcd(x, y)

    val a = x / gcd
    val b = y / gcd

    return if (x > y)
        Pair(a, b)
    else
        Pair(b, a)
}

//covert to file type
fun String.toFile():File{
    return File(this)
}