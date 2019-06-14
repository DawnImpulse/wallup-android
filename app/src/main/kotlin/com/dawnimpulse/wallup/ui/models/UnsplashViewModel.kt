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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dawnimpulse.wallup.network.repo.UnsplashRepo
import com.dawnimpulse.wallup.ui.objects.UnsplashImageObject
import com.dawnimpulse.wallup.utils.functions.RxBus
import com.dawnimpulse.wallup.utils.functions.RxErrorBus
import com.dawnimpulse.wallup.utils.functions.RxErrorBusObject
import com.dawnimpulse.wallup.utils.reusables.ERROR_UNSPLASH_RANDOM
import com.dawnimpulse.wallup.utils.reusables.REFRESHED_UNSPLASH_RANDOM
import com.dawnimpulse.wallup.utils.reusables.REFRESH_UNSPLASH_RANDOM
import io.reactivex.disposables.CompositeDisposable

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2019-06-10 by Saksham
 * @note Updates :
 */
class UnsplashViewModel : ViewModel() {
    private var compositeDisposable = CompositeDisposable()
    private var refreshRandom = false

    init {
        // event handling
        compositeDisposable.add(
                RxBus.subscribe {
                    if (it == REFRESH_UNSPLASH_RANDOM) {
                        refreshRandom = true
                        getRandomImages()
                    }
                }
        )
    }

    // random unsplash images
    val randomImages: MutableLiveData<MutableList<UnsplashImageObject>> by lazy {
        MutableLiveData<MutableList<UnsplashImageObject>>().also {
            getRandomImages()
        }
    }

    // fetching queried images from unsplash
    fun getRandomImages() {

        UnsplashRepo.randomImages { e, r ->
            e?.let {
                RxErrorBus.accept(RxErrorBusObject(ERROR_UNSPLASH_RANDOM, it))
            }
            r?.let {

                // get list for live object
                val images = randomImages.value

                if (images != null && !refreshRandom) {
                    images.addAll(it)
                    randomImages.value = images
                } else
                    randomImages.value = it.toMutableList()

                // publish event that refresh is handled
                if (refreshRandom)
                    RxBus.accept(REFRESHED_UNSPLASH_RANDOM)
                refreshRandom = false
            }
        }
    }

    // remove disposables
    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}