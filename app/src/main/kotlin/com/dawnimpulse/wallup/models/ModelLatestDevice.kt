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
import com.dawnimpulse.wallup.network.controller.CtrlDevice
import com.dawnimpulse.wallup.objects.ObjectIssue
import com.dawnimpulse.wallup.objects.ObjectLoading
import com.dawnimpulse.wallup.objects.ObjectReload
import com.dawnimpulse.wallup.utils.reusables.ERROR
import com.dawnimpulse.wallup.utils.reusables.F
import com.dawnimpulse.wallup.utils.reusables.LIST_COUNT
import com.dawnimpulse.wallup.utils.reusables.RELOAD
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ModelLatestDevice() : ViewModel() {
    private val deviceList = mutableListOf<Any>()
    private val liveList = MutableLiveData<List<Any>>()
    private val errorHandler = MutableLiveData<ObjectIssue>()
    private var loaded = false

    init {
        fetchLatestDevices()
    }


    /**
     * get devices list
     *
     * @return LiveData<List<Any>>
     */
    fun getList(): LiveData<List<Any>> {
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
     * reload devices list
     */
    fun reload(){
        fetchLatestDevices()
    }

    /**
     * load more devices
     */
    fun loadMore() {
        if (!loaded) fetchMoreDevices()
    }

    /**
     * fetch latest devices
     */
    private fun fetchLatestDevices() {
        viewModelScope.launch {
            if (BuildConfig.DEBUG) delay(1000)
            try {
                val contents = CtrlDevice.latest(0, LIST_COUNT)
                contents.forEach { deviceList.add(it) }
                deviceList.add(ObjectLoading()) // loading obj
                liveList.postValue(deviceList) // post list
            } catch (e: Exception) {
                errorHandler.postValue(F.handleException(e, ERROR.LIST.L_DEVICE, false))
                e.printStackTrace()
            }
        }
    }

    /**
     * fetch more devices
     */
    private fun fetchMoreDevices(){
        viewModelScope.launch {
            try {
                // check if last item is reload
                if (deviceList[deviceList.size - 1] is ObjectReload){
                    deviceList.removeAt(deviceList.size - 1)
                    deviceList.add(ObjectLoading())
                    liveList.postValue(deviceList)
                }

                // fetch content
                val contents = CtrlDevice.latest(deviceList.size - 1, LIST_COUNT)
                deviceList.removeAt(deviceList.size - 1)
                deviceList.addAll(contents)
                // check images count & set loading object
                if (contents.size == LIST_COUNT) deviceList.add(ObjectLoading()) else loaded = true

                // handle exception
            } catch (e: Exception) {
                val issue = F.handleException(e, ERROR.LIST.L_DEVICE, true)
                deviceList.removeAt(deviceList.size - 1)
                deviceList.add(ObjectReload(RELOAD.MORE.L_DEVICES, issue))
                e.printStackTrace()
            }
            // post list
            liveList.postValue(deviceList)
        }
    }
}