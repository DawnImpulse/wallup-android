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
package com.dawnimpulse.wallup.utils.handlers

import android.app.DownloadManager
import android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
import android.content.ContentValues
import android.content.Context.DOWNLOAD_SERVICE
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.dawnimpulse.wallup.ui.App
import com.dawnimpulse.wallup.utils.reusables.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object HandlerDownload {
    // download image using system download manager
    fun downloadManager(url: String, name: String) {
        val link = HandlerTransform(url)
                .height(1440)
                .quality(95)
                .format(JPG)
                .url()
        val downloadManager = App.context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(link.toUri())
        val uri = ("${DOWNLOAD_PATH}/$name").toFileUri()

        GlobalScope.launch {
            request
                    .setTitle(name)
                    .setDescription("Downloading image from WallUp")
                    .setDestinationUri(uri)
                    .setVisibleInDownloadsUi(true)
                    .setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .allowScanningByMediaScanner()

            try {
                val did = downloadManager.enqueue(request)
                DOWNLOADS.add(did) // use to notify when image is downloaded
                onMain { StyleToast.info("downloading image") }
            } catch (e: Exception) {
                e.printStackTrace()
                onMain { StyleToast.error("issue downloading image") }
            }
        }
    }

    /**
     * download in android Q
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun downloadQ(url:String, name: String) {
        GlobalScope.launch {
            HandlerTransform(url, App.context)
                    .height(1440)
                    .format(JPG)
                    .quality(95)
                    .bitmap {
                        if (it!=null){
                            try {
                                val values = ContentValues().apply {
                                    put(MediaStore.Images.Media.DISPLAY_NAME, name)
                                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Wallup/")
                                    put(MediaStore.Images.Media.IS_PENDING, 1)
                                }

                                val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
                                val imageUri = App.context.contentResolver.insert(collection, values)
                                App.context.contentResolver.openOutputStream(imageUri!!).use { out ->
                                    it.compress(Bitmap.CompressFormat.JPEG, 90, out)
                                }

                                values.clear()
                                values.put(MediaStore.Images.Media.IS_PENDING, 0)
                                App.context.contentResolver.update(imageUri, values, null, null)

                                onMain { StyleToast.success("image downloaded", Toast.LENGTH_LONG) }
                                // exception
                            }catch (e:Exception){
                                e.printStackTrace()
                                onMain { StyleToast.error("error saving image") }
                            }
                        }else
                            onMain { StyleToast.error("error downloading image") }

                    }
        }
    }

}