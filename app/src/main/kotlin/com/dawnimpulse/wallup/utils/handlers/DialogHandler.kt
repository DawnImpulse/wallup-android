/**
 * ISC License
 *
 * Copyright 2018-2019, Saksham (DawnImpulse)
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
package com.dawnimpulse.wallup.utils.handlers

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.utils.functions.toast
import kotlinx.android.synthetic.main.dialog_progress.view.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-28 by Saksham
 * @note Updates :
 */
object DialogHandler {

    lateinit var alertDialog: AlertDialog

    /**
     * Progress dialog handling
     */
    @SuppressLint("SetTextI18n")
    fun downloadProgress(context: Context, url: String, name: String, callback: (Boolean) -> Unit) {
        var did: Long? = null
        val factory = LayoutInflater.from(context)
        val view = factory.inflate(R.layout.dialog_progress, null)
        alertDialog = AlertDialog.Builder(context, R.style.MyDialogTheme).create()
        alertDialog.setView(view)

        // get views
        val progress = view.progressSize
        val percentage = view.progressPercentage
        val bar = view.progressBar as ProgressBar

        // if cancelled
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "CANCEL") { _, _ ->
            did?.let {

                // remove task from downloading
                val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                manager.remove(it)

                // show user toast
                (context as AppCompatActivity).runOnUiThread {
                    context.toast("download cancelled")
                }
            }

            // dismiss dialog
            alertDialog.dismiss()
        }

        bar.isIndeterminate = true
        DownloadHandler.downloadManager(context, url, name, {
            bar.isIndeterminate = false

            val per = (it.first.toDouble().div(it.second) * 100).toInt()
            val done = "${it.first.toDouble().div(1024).toInt()} KB"
            val total = "${it.second.toDouble().div(1024).toInt()} KB"

            bar.progress = per
            percentage.text = "$per %"
            progress.text = "$done/$total"

        }, {
            did = it
        }) {
            callback(it)
            alertDialog.dismiss()
        }

        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}