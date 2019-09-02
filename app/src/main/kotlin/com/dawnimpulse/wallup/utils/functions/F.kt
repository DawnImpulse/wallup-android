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

import android.content.Context
import android.graphics.Point
import android.os.Environment
import android.view.WindowManager
import com.crashlytics.android.Crashlytics
import com.dawnimpulse.wallup.utils.reusables.CACHED
import com.dawnimpulse.wallup.utils.reusables.Config
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.commons.io.comparator.LastModifiedFileComparator
import java.io.File
import java.util.*
import kotlin.random.Random


/**
 * @author Saksham
 *
 * @note Last Branch Update - develop
 * @note Created on 2019-06-10 by Saksham
 *
 * @note Updates :
 * Saksham - 2019 08 18 - master - random color
 * Saksham - 2019 08 20 - master - generate shortid
 * Saksham - 2019 09 02 - develop - delete cached + dynamic height
 */
object F {

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
                    if(!it.isDirectory)
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
}