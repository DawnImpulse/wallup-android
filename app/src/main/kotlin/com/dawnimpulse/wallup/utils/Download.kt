/*
ISC License

Copyright 2018, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/
package com.dawnimpulse.wallup.utils

import android.content.Context
import android.media.MediaScannerConnection
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.OnProgressListener
import com.downloader.PRDownloader.download
import com.downloader.Progress


/**
 * @author Saksham
 *
 * @note Last Branch Update -
 * @note Created on 2018-07-22 by Saksham
 *
 * @note Updates :
 */
object Download {
    private var progress = HashMap<String, Progress>()
    private var progressing = HashMap<String, OnProgressListener>()

    fun start(context: Context, id: String, url: String, path: String, name: String, listener: (error: String?) -> Unit) {
        progressing[id] = OnProgressListener { }
        download(url, path, name)
                .build()
                .setOnProgressListener(progressing[id])
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        listener(null);
                        MediaScannerConnection.scanFile(context, arrayOf(path),
                                arrayOf("image/jpg", "image/jpeg", "image/png"), null)
                    }

                    override fun onError(error: Error?) {
                        listener(error.toString())
                    }
                })
    }

    fun progress(id: String): Progress? {
        return if (progress.containsKey(id)) {
            progress[id]
        } else
            null
    }

    fun progress1(id: String, listener: (b: Boolean, progress: Progress?) -> Unit) {
        return if (progress.containsKey(id)) {
            var ab = progressing[id]
            //ab!!.onProgress()
        } else
            listener(false, null)
    }
}