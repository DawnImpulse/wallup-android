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
package org.sourcei.wallup.deprecated.handlers

import android.app.DownloadManager
import android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.sourcei.wallup.deprecated.utils.toFileUri
import org.sourcei.wallup.deprecated.utils.toast


/**
 * @author Saksham
 *
 * @note Last Branch Update - hotfixes
 * @note Created on 2018-07-22 by Saksham
 *
 * @note Updates :
 *  2018 08 03 - recent - Saksham - using android default download manager
 *  2018 12 16 - master - Saksham - using dynamic path
 *  2019 01 05 - hotfixes - Saksham - handling of null cursor in download manager
 * Saksham - 2019 02 06 - master - exception handling
 */
object DownloadHandler {

    // download image using system download manager
    fun downloadData(context: Context, url: String, id: String, path: String, isWallpaper: Boolean = false) {
        val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(url.toUri())
        val uri = ("$path/$id.jpg").toFileUri()

        GlobalScope.launch {
            request
                    .setTitle("$id.jpg")
                    .setDescription("Downloading image from WallUp.")
                    .setDestinationUri(uri)
                    .setVisibleInDownloadsUi(true)
                    .setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .allowScanningByMediaScanner()

            try {
                val did = downloadManager.enqueue(request)
                org.sourcei.wallup.deprecated.utils.Arrays.downloadIds.add(did) // use to notify when image is downloaded
            } catch (e: Exception) {
                context.toast("Security issue while downloading to SD Card!! Kindly switch to Internal Storage.")
            }
        }
    }

    /*//external download
    fun externalDownload(url: String, path: String, name: String, progress: (Progress) -> Unit, callback: (Boolean) -> Unit): Int {
        return PRDownloader.download(url, path, name)
                .build()
                .setOnProgressListener {
                    progress(it)
                }
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        callback(true)
                    }

                    override fun onError(error: Error?) {
                        callback(false)
                    }

                })
    }*/

    //download manager progress
    fun downloadManager(context: Context, url: String, path: String, id: String,
                        progress: (Pair<Int, Int>) -> Unit, downloadId: (Long) -> Unit, callback: (Boolean) -> Unit) {
        val downloadManager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(url.toUri())
        val uri = ("$path/$id").toFileUri()
        var did: Long = 0

        GlobalScope.launch {
            request
                    .setTitle("$id.jpg")
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

                                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) === DownloadManager.STATUS_SUCCESSFUL) {
                                    downloading = false
                                    callback(true)
                                }
                                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) === DownloadManager.STATUS_FAILED) {
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
            }catch (e:Exception){
                e.printStackTrace()
                context.toast("Security issue while downloading image!! Kindly retry & if you have chosen SD Card switch to Internal Storage.")
            }
        }
    }
}