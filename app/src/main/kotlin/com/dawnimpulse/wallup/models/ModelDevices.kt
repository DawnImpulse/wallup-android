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
import com.dawnimpulse.wallup.objects.ObjectDevice
import com.dawnimpulse.wallup.objects.ObjectIssue
import com.dawnimpulse.wallup.objects.ObjectLoading
import com.dawnimpulse.wallup.objects.ObjectReload
import com.dawnimpulse.wallup.utils.reusables.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ModelDevices() : ViewModel() {
    private val deviceList = mutableListOf<Any>()
    private val liveList = MutableLiveData<List<Any>>()
    private val errorHandler = MutableLiveData<ObjectIssue>()
    private var loaded = false
    private var position = 0

    init {
        fetchAllDevices()
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
    fun reload() {
        fetchAllDevices()
    }

    /**
     * load more devices
     */
    fun loadMore() {
        if (!loaded) loadMoreDevices()
    }

    /**
     * fetch latest devices
     */
    private fun fetchAllDevices() {
        viewModelScope.launch {
            if (BuildConfig.DEBUG) delay(1000)
            try {
                val contents = CtrlDevice.all(0, LIST_COUNT)
                position = contents.size
                val devices = mutableListOf<Any>()
                contents.forEachIndexed { i, it ->
                    // first index
                    when {
                        i == 0 -> {
                            with(devices) {
                                add(it.brand)
                                add(it)
                            }
                        }
                        it.brand.name == contents[i - 1].brand.name -> devices.add(it)
                        else -> {
                            with(devices) {
                                add(it.brand)
                                add(it)
                            }
                        }
                    }
                }
                deviceList.addAll(devices)
                deviceList.add(ObjectLoading()) // loading obj
                logd(deviceList.size)
                liveList.postValue(deviceList) // post list
            } catch (e: Exception) {
                errorHandler.postValue(F.handleException(e, ERROR.LIST.A_DEVICES, false))
                e.printStackTrace()
            }
        }
    }

    /**
     * load more devices
     */
    private fun loadMoreDevices() {
        if (!loaded)
            viewModelScope.launch {
                try {
                    // check if last item is reload
                    if (deviceList[deviceList.size - 1] is ObjectReload) {
                        deviceList.removeAt(deviceList.size - 1)
                        deviceList.add(ObjectLoading())
                        liveList.postValue(deviceList)
                    }

                    // fetch content
                    val contents = CtrlDevice.all(position, LIST_COUNT)
                    position += contents.size
                    val devices = mutableListOf<Any>()

                    deviceList.removeAt(deviceList.size - 1)
                    contents.forEachIndexed { i, it ->
                        // first index
                        when (it.brand.name) {
                            // previous list brand is same
                            (deviceList[deviceList.size - 1] as ObjectDevice).brand.name -> devices.add(it)

                            // previous list brand is not same
                            (deviceList[deviceList.size - 1] as ObjectDevice).brand.name -> {
                                with(devices) {
                                    add(it.brand)
                                    add(it)
                                }
                            }

                            // list previous brand same
                            contents[i - 1].brand.name -> devices.add(it)
                            else -> {
                                with(devices) {
                                    add(it.brand)
                                    add(it)
                                }
                            }
                        }
                    }

                    deviceList.addAll(devices)
                    // check devices count & set loading object
                    if (contents.size == LIST_COUNT) deviceList.add(ObjectLoading()) else loaded = true

                    // handle exception
                } catch (e: Exception) {
                    val issue = F.handleException(e, ERROR.LIST.MORE.A_DEVICES, true)
                    deviceList.removeAt(deviceList.size - 1)
                    deviceList.add(ObjectReload(RELOAD.MORE.A_DEVICES, issue))
                    e.printStackTrace()
                }
                // post list
                liveList.postValue(deviceList)
            }
    }
}