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

import android.app.DownloadManager
import android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.dawnimpulse.wallup.utils.functions.toFileUri
import com.dawnimpulse.wallup.utils.functions.toast
import com.dawnimpulse.wallup.utils.reusables.Arrays
import com.dawnimpulse.wallup.utils.reusables.Config
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-28 by Saksham
 * @note Updates :
 */
object DownloadHandler {

    // download image using system download manager
    fun downloadData(context: Context, url: String, name: String) {
        val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(url.toUri())
        val uri = ("${Config.DEFAULT_DOWNLOAD_PATH}/$name").toFileUri()

        GlobalScope.launch {
            request
                    .setTitle(name)
                    .setDescription("Downloading image from WallUp.")
                    .setDestinationUri(uri)
                    .setVisibleInDownloadsUi(true)
                    .setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .allowScanningByMediaScanner()

            try {
                val did = downloadManager.enqueue(request)
                Arrays.downloadIds.add(did) // use to notify when image is downloaded
            } catch (e: Exception) {
                context.toast("Issue downloading image")
            }
        }
    }

    //download manager progress
    fun downloadManager(context: Context, url: String, name: String,
                        progress: (Pair<Int, Int>) -> Unit, downloadId: (Long) -> Unit, callback: (Boolean) -> Unit) {

        val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(url.toUri())
        val uri = ("${Config.DEFAULT_DOWNLOAD_PATH}/$name").toFileUri()
        var did: Long = 0

        GlobalScope.launch {
            request
                    .setTitle(name)
                    .setDescription("Downloading image from WallUp.")
                    .setDestinationUri(uri)
                    .allowScanningByMediaScanner()

            try {
                did = downloadManager.enqueue(request)
                downloadId(did)
                var downloading = true

                while (downloading) {

                    val q = DownloadManager.Query()
                    q.setFilterById(did)

                    val cursor = downloadManager.query(q)
                    if (cursor != null) {
                        cursor.let {
                            if (cursor.count > 0) {
                                cursor.moveToFirst()

                                val bytes_downloaded = cursor.getInt(cursor
                                        .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                                val bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                                    downloading = false
                                    callback(true)
                                }
                                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_FAILED) {
                                    downloading = false
                                    callback(false)
                                }

                                context as AppCompatActivity
                                context.runOnUiThread {
                                    if (bytes_total > 0)
                                        progress(Pair(bytes_downloaded, bytes_total))
                                }

                                cursor.close()
                            } else {
                                cursor.close()
                                downloading = false
                            }
                        }
                    } else {
                        downloading = false
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                (context as AppCompatActivity).runOnUiThread {
                    context.toast("Issue downloading image")
                }
            }
        }
    }
}