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
package com.dawnimpulse.wallup.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dawnimpulse.wallup.BuildConfig
import com.dawnimpulse.wallup.network.controller.CtrlBookmark
import com.dawnimpulse.wallup.objects.ObjectImage
import com.dawnimpulse.wallup.objects.ObjectIssue
import com.dawnimpulse.wallup.objects.ObjectLoading
import com.dawnimpulse.wallup.objects.ObjectReload
import com.dawnimpulse.wallup.utils.reusables.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ModelBookmarks() : ViewModel() {
    private val imageList = mutableListOf<Any>()
    private val liveList = MutableLiveData<List<Any>>()
    private val errorHandler = MutableLiveData<ObjectIssue>()
    private var loaded = false

    init {
        fetchBookmarks()
    }

    /**
     * get images
     *
     * @return LiveData<List<Any>>
     */
    fun getImages(): LiveData<List<Any>> {
        return liveList
    }

    /**
     * handling errors
     *
     */
    fun errors(): LiveData<ObjectIssue> {
        return errorHandler
    }

    /**
     * reload images
     */
    fun reload() {
        fetchBookmarks()
    }

    /**
     * load more images
     */
    fun loadMore() {
        if (!loaded) loadMoreBookmarks()
    }

    /**
     * remove image
     */
    fun remove(id: String) {
        imageList.removeAll { if (it is ObjectImage) it.iid == id else false }
        if (imageList.isEmpty())
            RxBusType.accept(RxType(EVENT.NOT.BOOKMARKS, true))
        liveList.postValue(imageList)
    }

    /**
     * add image
     */
    fun add(image: ObjectImage) {
        imageList.add(0, image)
        liveList.postValue(imageList)
    }

    /**
     * fetch bookmarks
     */
    private fun fetchBookmarks() {
        viewModelScope.launch {
            if (BuildConfig.DEBUG) delay(1000)
            try {
                val contents = CtrlBookmark.latest(0, LIST_COUNT)
                val images = contents.map { it.image }
                images.forEach { it.height = F.getRandomHeight() }
                imageList.addAll(images)
                if (imageList.size == LIST_COUNT)
                    imageList.add(ObjectLoading()) // loading obj
                if (imageList.isEmpty()) // list empty
                    RxBusType.accept(RxType(EVENT.NOT.BOOKMARKS, true))
                liveList.postValue(imageList) // post list
            } catch (e: Exception) {
                errorHandler.postValue(F.handleException(e, ERROR.LIST.BOOKMARKS, false))
                e.printStackTrace()
            }
        }
    }

    /**
     * load more bookmarks
     */
    private fun loadMoreBookmarks() {
        viewModelScope.launch {
            try {
                // check if last item is reload
                if (imageList[imageList.size - 1] is ObjectReload) {
                    imageList.removeAt(imageList.size - 1)
                    imageList.add(ObjectLoading())
                    liveList.postValue(imageList)
                }

                // fetch content
                val contents = CtrlBookmark.latest(imageList.size - 1, LIST_COUNT)
                val images = contents.map { it.image }
                imageList.removeAt(imageList.size - 1)
                images.forEach { it.height = F.getRandomHeight() }
                imageList.addAll(images)
                // check images count & set loading object
                if (contents.size == LIST_COUNT) imageList.add(ObjectLoading()) else loaded = true
                // handle exception
            } catch (e: Exception) {
                val issue = F.handleException(e, ERROR.LIST.MORE.BOOKMARKS, true)
                imageList.removeAt(imageList.size - 1)
                imageList.add(ObjectReload(RELOAD.MORE.BOOKMARKS, issue))
                e.printStackTrace()
            }
            // post list
            liveList.postValue(imageList)
        }
    }
}