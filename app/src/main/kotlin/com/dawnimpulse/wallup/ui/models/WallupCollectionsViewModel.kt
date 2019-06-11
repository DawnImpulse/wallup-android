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
import com.dawnimpulse.wallup.network.repo.WallupRepo
import com.dawnimpulse.wallup.ui.objects.WallupCollectionObject
import com.dawnimpulse.wallup.utils.*
import com.dawnimpulse.wallup.utils.functions.logd
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
class WallupCollectionsViewModel : ViewModel() {
    private var compositeDisposable = CompositeDisposable()
    private var refreshSortedCollections = false
    private var finishedSortedCollections = false
    private var pageSortedCollections = 2

    init {
        // event handling
        compositeDisposable.add(
                RxBus
                        .subscribe {
                            when (it) {
                                // refresh
                                REFRESH_WALLUP_COLLECTIONS_SORTED -> {
                                    pageSortedCollections = 1
                                    refreshSortedCollections = true
                                    finishedSortedCollections = false
                                    getSortedCollections(pageSortedCollections)
                                }

                                // load more
                                LOAD_MORE_WALLUP_COLLECTIONS_SORTED -> {
                                    if (!finishedSortedCollections)
                                        getSortedCollections(pageSortedCollections)
                                    else
                                        RxBus.accept(LOADED_WALLUP_COLLECTIONS_SORTED)
                                }
                            }
                        }
        )
    }

    // random unsplash images
    val sortedCollections: MutableLiveData<MutableList<WallupCollectionObject?>> by lazy {
        MutableLiveData<MutableList<WallupCollectionObject?>>().also {
            getSortedCollections(1)
        }
    }

    // fetching queried images from unsplash
    private fun getSortedCollections(page: Int) {

        WallupRepo.getSortedCollections(page) { e, r ->
            e?.let {
                RxErrorBus.accept(RxErrorBusObject(ERROR_WALLUP_COLLECTIONS_SORTED, it))
            }
            r?.let {

                // get list for live object
                val cols = sortedCollections.value

                logd("Size : ${it.size}")

                // check if result list is empty or not
                if (it.isNotEmpty()) {
                    if (cols != null && !refreshSortedCollections) {
                        this.pageSortedCollections++
                        cols.addAll(it)
                        sortedCollections.value = cols
                        RxBus.accept(LOADED_WALLUP_COLLECTIONS_SORTED)

                    } else {
                        refreshSortedCollections = false
                        sortedCollections.value = it.toMutableList()
                        RxBus.accept(REFRESHED_WALLUP_COLLECTIONS_SORTED)
                    }

                    // list is finished
                } else {
                    finishedSortedCollections = true
                    RxBus.accept(LOADED_WALLUP_COLLECTIONS_SORTED)
                }
            }
        }
    }

    // remove disposables
    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}