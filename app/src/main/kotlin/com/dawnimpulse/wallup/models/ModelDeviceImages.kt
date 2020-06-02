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
import com.dawnimpulse.wallup.network.controller.CtrlImage
import com.dawnimpulse.wallup.objects.ObjectIssue
import com.dawnimpulse.wallup.objects.ObjectLoading
import com.dawnimpulse.wallup.objects.ObjectReload
import com.dawnimpulse.wallup.utils.reusables.ERROR
import com.dawnimpulse.wallup.utils.reusables.F
import com.dawnimpulse.wallup.utils.reusables.LIST_COUNT
import com.dawnimpulse.wallup.utils.reusables.RELOAD
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ModelDeviceImages() : ViewModel() {
    private val imageList = mutableListOf<Any>()
    private val liveList = MutableLiveData<List<Any>>()
    private val errorHandler = MutableLiveData<ObjectIssue>()
    private var loaded = false
    private lateinit var device: String

    init {
        fetchImages()
    }

    /**
     * get list of images
     *
     * @param device
     * @return LiveData<List<Any>>
     */
    fun getList(device: String): LiveData<List<Any>> {
        this.device = device
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
        fetchImages()
    }

    /**
     * load more images
     */
    fun loadMore() {
        if (!loaded) loadMoreImages()
    }

    /**
     * fetch latest images
     */
    private fun fetchImages() {
        viewModelScope.launch {
            if (BuildConfig.DEBUG) delay(1000)
            try {
                val contents = CtrlImage.device(device, 0, LIST_COUNT)
                contents.forEach { it.height = F.getRandomHeight() }
                imageList.addAll(contents) // add all content to list
                imageList.add(ObjectLoading()) // loading obj
                liveList.postValue(imageList) // post list
            } catch (e: Exception) {
                errorHandler.postValue(F.handleException(e, ERROR.LIST.D_IMAGES, false))
                e.printStackTrace()
            }
        }
    }

    /**
     * load more images
     */
    private fun loadMoreImages() {
        viewModelScope.launch {
            if (BuildConfig.DEBUG) delay(1000)
            try {
                // check if last item is reload
                if (imageList[imageList.size - 1] is ObjectReload) {
                    imageList.removeAt(imageList.size - 1)
                    imageList.add(ObjectLoading())
                    liveList.postValue(imageList)
                }

                // fetch content
                val contents = CtrlImage.device(device, imageList.size - 1, LIST_COUNT)
                imageList.removeAt(imageList.size - 1)
                contents.forEach { it.height = F.getRandomHeight() }
                imageList.addAll(contents) // add to list
                if (contents.size >= LIST_COUNT) imageList.add(ObjectLoading()) else loaded = true
                // handle exception
            } catch (e: Exception) {
                imageList.removeAt(imageList.size - 1)
                imageList.add(ObjectReload(RELOAD.MORE.D_IMAGES,
                        F.handleException(e, ERROR.LIST.MORE.D_IMAGES, true)))
                e.printStackTrace()
            }
            // post list
            liveList.postValue(imageList)
        }
    }
}