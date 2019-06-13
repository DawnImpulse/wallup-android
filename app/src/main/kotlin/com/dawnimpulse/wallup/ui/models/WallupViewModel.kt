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
package com.dawnimpulse.wallup.ui.models

import androidx.appcompat.app.AppCompatActivity
import com.dawnimpulse.wallup.network.repo.WallupRepo
import com.dawnimpulse.wallup.ui.objects.HomescreenObject
import com.dawnimpulse.wallup.ui.objects.WallupCollectionObject
import com.dawnimpulse.wallup.ui.objects.WallupImageObject
import com.dawnimpulse.wallup.utils.reusables.Lifecycle

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-10 by Saksham
 * @note Updates :
 */
class WallupViewModel(private val activity: AppCompatActivity) {

    // ---------------------------------------
    // fetching sorted collection from wallup
    // ---------------------------------------
    fun getSortedCollections(page: Int, callback: (Any?, List<WallupCollectionObject>?) -> Unit) {
        Lifecycle.onStart(activity) {
            WallupRepo.getSortedCollections(page, callback)
        }
    }


    // ------------------
    //    homescreen
    // ------------------
    fun homescreen(callback: (Any?, HomescreenObject?) -> Unit) {
        Lifecycle.onStart(activity) {
            WallupRepo.homescreen(callback)
        }
    }

    // ------------------------
    //    homescreen random
    // ------------------------
    fun homescreenRandom(callback: (Any?, HomescreenObject?) -> Unit) {
        Lifecycle.onStart(activity) {
            WallupRepo.homescreenRandom(callback)
        }
    }

    // ------------------------
    //    collection images
    // ------------------------
    fun collectionImages(page: Int, cid: String, callback: (Any?, List<WallupImageObject>?) -> Unit) {
        Lifecycle.onStart(activity) {
            WallupRepo.collectionImages(page,cid, callback)
        }
    }
}