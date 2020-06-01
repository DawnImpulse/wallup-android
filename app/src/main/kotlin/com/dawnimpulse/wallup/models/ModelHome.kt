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
import com.dawnimpulse.wallup.network.controller.CtrlImage
import com.dawnimpulse.wallup.objects.ObjectIssue
import com.dawnimpulse.wallup.objects.ObjectLoading
import com.dawnimpulse.wallup.utils.reusables.ERROR_HOME_IMAGES
import com.dawnimpulse.wallup.utils.reusables.F
import com.dawnimpulse.wallup.utils.reusables.LIST_COUNT
import kotlinx.coroutines.launch

class ModelHome() : ViewModel() {
    private val homeList = mutableListOf<Any>()
    private val liveList = MutableLiveData<List<Any>>()
    private val errorHandler = MutableLiveData<ObjectIssue>()

    init {
        fetchLatestImages()
    }


    /**
     * get homescreen
     *
     * @return LiveData<List<Any>>
     */
    fun getHomescreen(): LiveData<List<Any>> {
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
     * fetch latest images
     */
    private fun fetchLatestImages() {
        viewModelScope.launch {
            try {
                val contents = CtrlImage.latest(0, LIST_COUNT)
                contents.forEach {
                    it.height = F.getRandomHeight() // add random height
                    homeList.add(it) // add to home list
                }
                homeList.add(ObjectLoading()) // loading obj
                liveList.postValue(homeList) // post list
            } catch (e: Exception) {
                errorHandler.postValue(F.handleException(e, ERROR_HOME_IMAGES, false))
                e.printStackTrace()
            }
        }
    }
}