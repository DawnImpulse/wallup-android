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

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.dawnimpulse.wallup.BuildConfig
import com.dawnimpulse.wallup.objects.ObjectError
import com.dawnimpulse.wallup.objects.ObjectIssue
import com.dawnimpulse.wallup.ui.App
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.apache.commons.io.FileUtils


object F {

    /**
     * check exception type and convert to ObjectIssue
     *
     * @param e
     * @param code
     * @param more - if exception is in load more images
     * @return ObjectIssue
     */
    fun handleException(e: Exception, code: Int, more: Boolean): ObjectIssue {
        // handling error
        return if (e.message != null) {
            // checking for errors from server
            val error = e.message!!.fromSafeJson(ObjectError::class.java)
            if (error != null)
                ObjectIssue(more, code, error.error, error.message, error)
            else
                ObjectIssue(more, code, TYPE_ERROR_LOADING, e.message!!, e)
        } else
            ObjectIssue(more, code, TYPE_ERROR_LOADING, "some internal error", e)
    }

    // get display height
    fun displayDimensions(): Point {
        val point = Point()
        val mWindowManager = App.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = mWindowManager.defaultDisplay
        display.getSize(point) //The point now has display dimens
        return point
    }

    // convert dp - px
    fun dpToPx(dp: Int, context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    /**
     * get width & height for random image
     */
    fun getWidthHeightRandom(): Pair<Int, Int> {
        val point = displayDimensions()
        val width = point.x / 2 - dpToPx(8, App.context)
        val height = dpToPx((180..260).random(), App.context)

        return Pair(width, height)
    }

    /**
     * get random height
     */
    fun getRandomHeight(): Int {
        val point = displayDimensions()
        val min = (0.50 * point.y).toInt()
        val max = (0.80 * point.y).toInt()
        return (min..max).random()
    }

    /**
     * night mode settings
     */
    fun nightMode(): Int {
        if (Prefs.contains(NIGHT_MODE_SYSTEM))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        else {
            if (Prefs.getBoolean(NIGHT_MODE, false))
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        return AppCompatDelegate.getDefaultNightMode()
    }

    /**
     * send mail
     */
    fun sendMail(context: Context) {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
        val text = "SDK INT : ${android.os.Build.VERSION.SDK_INT} \n" +
                "MODEL : ${android.os.Build.MODEL} \n"

        emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("dawnimpulse@gmail.com"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "HowComma Feedback  v${BuildConfig.VERSION_NAME}")
        emailIntent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(emailIntent, "Send mail using..."))
    }

    /**
     * application cache
     */
    fun appCache(scope: CoroutineScope, context: Context, callback: (String) -> Unit) {
        scope.launch {
            val size = FileUtils.sizeOfDirectory(context.cacheDir)
            (context as AppCompatActivity).runOnUiThread {
                callback(FileUtils.byteCountToDisplaySize(size))
            }
        }
    }

    /**
     * delete app cache
     */
    fun deleteCache(scope: CoroutineScope, context: Context) {
        scope.launch {
            FileUtils.deleteQuietly(context.cacheDir)
        }
    }

    /**
     * return firebase token
     */
    fun getFirebaseToken(callback: (String?) -> Unit) {
        FirebaseAuth.getInstance().currentUser!!
                .getIdToken(true)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val idToken: String? = task.result?.token
                        callback(idToken)
                    } else {
                        task.exception?.printStackTrace()
                        callback(null)
                    }
                }
    }
}