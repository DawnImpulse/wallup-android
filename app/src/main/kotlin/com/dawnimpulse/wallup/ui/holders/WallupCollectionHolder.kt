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
package com.dawnimpulse.wallup.ui.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.ui.objects.WallupCollectionObject
import com.dawnimpulse.wallup.utils.reusables.Config
import com.dawnimpulse.wallup.utils.functions.RxBusTime
import com.dawnimpulse.wallup.utils.functions.gone
import com.dawnimpulse.wallup.utils.functions.show
import com.dawnimpulse.wallup.utils.handlers.ImageHandler
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.inflator_collections.view.*

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-11 by Saksham
 * @note Updates :
 */
class WallupCollectionHolder(view: View) : RecyclerView.ViewHolder(view) {

    val primary = view.collectionMainImage
    val secondary = view.collectionsSecImage
    val progress = view.collectionProgress
    val context = view.context

    // bind view
    fun bind(item: WallupCollectionObject) {

        Config.disposableWallupCollectionsViewHolder[adapterPosition]?.dispose()

        lateinit var change: Disposable
        val size = item.images.size
        var current = 0
        var progressed = 0
        progress.max = 500
        progress.show()

        // set image in views
        if (item.images.isNotEmpty()) {
            ImageHandler.setImageImgixFadein(primary, item.images[current].urls[0])

            if (size > current + 1)
                ImageHandler.setImageImgixFadein(secondary, item.images[current + 1].urls[0])
        }

        // caching all images
        item.images.forEach {
            ImageHandler.cacheImage(context, it.urls[0], 720)
        }

        // disposable of auto change
        change = RxBusTime.subscribe {

            if (size > current + 2) {
                progress.progress = ++progressed

                if (progressed % 500 == 0) {
                    if (size > current + 2) {
                        ImageHandler.setImageImgixSlide(primary, item.images[current + 1].urls[0])
                        ImageHandler.setImageImgixSlide(secondary, item.images[current + 2].urls[0])
                        current++
                        progress.progress = 0
                        progressed = 0
                    } else {
                        change.dispose()
                        progress.gone()
                    }
                }

                if (progressed == 2500) {
                    change.dispose()
                    progress.gone()
                }
            }else
                progress.gone()
        }

        Config.disposableWallupCollectionsViewHolder[adapterPosition] = change
    }

}
