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
package com.dawnimpulse.wallup.utils.functions

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Point
import android.net.Uri
import android.os.Environment
import android.view.WindowManager
import com.crashlytics.android.Crashlytics
import com.dawnimpulse.wallup.utils.reusables.CACHED
import com.dawnimpulse.wallup.utils.reusables.Config
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.commons.io.comparator.LastModifiedFileComparator
import java.io.File
import java.security.MessageDigest
import java.util.*
import kotlin.random.Random


/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2019-06-10 by Saksham
 *
 * @note Updates :
 * Saksham - 2019 08 18 - master - random color
 * Saksham - 2019 08 20 - master - generate shortid
 * Saksham - 2019 09 02 - develop - delete cached + dynamic height
 * Saksham - 2019 09 04 - develop - compare 2 bitmaps
 * Saksham - 2019 09 25 - master - start web
 */
object F {

    // start intent
    fun startWeb(context: Context, string: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(string)))
    }

    // convert dp - px
    fun dpToPx(dp: Int, context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    // get display height
    fun displayDimensions(context: Context): Point {
        val point = Point()
        val mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = mWindowManager.defaultDisplay
        display.getSize(point) //The point now has display dimens
        return point
    }

    //make dir
    fun mkdir() {
        if (Environment.getExternalStorageDirectory().exists()) {
            if (!Config.DEFAULT_DOWNLOAD_PATH.toFile().exists())
                Config.DEFAULT_DOWNLOAD_PATH.toFile().mkdir()
        }
    }

    // get height based on screen width
    fun getDynamicHeight(context: Context, screenWidth: Int, screenHeight: Int, width: Int, height: Int): Int {
        val h = ((screenWidth - dpToPx(16, context)) * height) / width

        return if (h > (screenHeight - dpToPx(48, context)))
            screenHeight - dpToPx(48, context)
        else
            h
    }

    // Generating random color
    fun randomColor(): String {
        val chars = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")
        var color = "#"
        for (i in 1..6) {
            color += chars[Math.floor(Math.random() * chars.size).toInt()]
        }
        return color
    }

    // generate shortid
    fun shortid(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..10)
                .map { Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")
    }

    // delete extra cached images
    fun deleteCached(context: Context, amount: Int) {

        GlobalScope.launch {
            // files dir
            val file = File(context.filesDir, CACHED)

            // check count of cached images
            if (file.listFiles().size > amount) {
                val files = file.listFiles().filter { it.name.contains(".jpg") }.toTypedArray()
                Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR)

                // number of images to delete
                val deleteCount = files.size - amount

                // delete files
                files.forEachIndexed { index, file ->

                    if (index < deleteCount) {

                        // deleting file
                        try {
                            file.delete()
                        } catch (e: Exception) {
                            Crashlytics.logException(e)
                            e.printStackTrace()
                        }
                    }
                }

                logd("deleted $deleteCount cached images")
            }
        }
    }

    // delete all cached images
    fun deleteAllCached(context: Context) {

        GlobalScope.launch {
            // files dir
            try {
                File(context.filesDir, CACHED).listFiles().forEach { it.delete() }
                context.filesDir.listFiles().forEach {
                    // only delete files (images)
                    if (!it.isDirectory)
                        it.delete()
                }
            } catch (e: Exception) {
                Crashlytics.logException(e)
                e.printStackTrace()
            } finally {
                logd("all cached images removed")
            }
        }
    }

    // verify two bitmaps
    fun compareBitmaps(b1: Bitmap?, b2: Bitmap?, callback: (Boolean) -> Unit) {

        if (b1 == null || b2 == null) {
            callback(false)
        } else
            GlobalScope.launch {
                try {
                    callback(b1.sameAs(b2)) // callback with compare
                } catch (e: Exception) {
                    Crashlytics.logException(e)
                    e.printStackTrace()
                    callback(false)
                }
            }
    }

    // verify file size for wallpaper
    fun verifyFileWallpaper(file: File, files: List<File>, callback: (Boolean) -> Unit) {

        var deleted = false

        GlobalScope.launch {
            for (f in files) {

                // check cache directory as well
                if (f.isDirectory) {
                    for (ff in f.listFiles()) {
                        if (file.exists() && ff.exists()) {
                            val fic = calculateMD5(file)
                            val ffc = calculateMD5(ff)

                            if (fic != null && ffc != null && fic == ffc && file.name != ff.name) {
                                file.delete()
                                deleted = true
                                break
                            }
                        }
                    }
                } else {

                    if (file.exists() && f.exists()) {
                        val fic = calculateMD5(file)
                        val fc = calculateMD5(f)

                        if (fic != null && fc != null && fic == fc && file.name != f.name) {
                            file.delete()
                            deleted = true
                            break
                        }
                    }
                }
            }
            callback(deleted)
        }


    }

    // remove duplicates
    fun removeDuplicates(files: List<File>) {
        GlobalScope.launch {
            try {
                for (f in files) {
                    if (f.isDirectory) {
                        for (ff in f.listFiles())
                            verifyFileWallpaper(ff, f.listFiles().toList()) {}
                    } else
                        verifyFileWallpaper(f, files) {}
                }
            } catch (e: Exception) {
                Crashlytics.logException(e)
                e.printStackTrace()
            }
        }
    }

    // md5 of a file
    @SuppressLint("DefaultLocale")
    fun calculateMD5(file: File): String? {
        try {
            val md = MessageDigest.getInstance("MD5")
            md.update(file.readBytes())
            val digest = md.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest in digest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2)
                    h = "0$h"
                hexString.append(h)
            }
            return hexString.toString().toUpperCase()
        } catch (e: Exception) {
            return null
        }
    }
}