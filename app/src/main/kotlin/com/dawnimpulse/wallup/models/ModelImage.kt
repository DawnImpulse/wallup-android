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
import com.dawnimpulse.wallup.objects.ObjectImage
import com.dawnimpulse.wallup.utils.reusables.Resource
import kotlinx.coroutines.launch

/**
 * @info - bridge between ui & controller
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-03-02 by Saksham
 * @note Updates :
 */
class ModelImage(limit: Number = 30) : ViewModel() {
    private val randomImages = mutableListOf<ObjectImage>()
    private val mutableRandomImages = MutableLiveData<Resource<List<ObjectImage>>>()

    init {
        fetchRandomImages(limit)
    }


    /**
     * get random images
     *
     * @return callback
     */
    fun getRandomImages(): LiveData<Resource<List<ObjectImage>>> {
        return mutableRandomImages
    }

    /**
     * get random images
     *
     * @param limit
     */
    private fun fetchRandomImages(limit: Number) {
        var resource = Resource.success(randomImages)
        viewModelScope.launch {
            resource = try {
                randomImages.addAll(CtrlImage.random(limit))
                Resource.success(randomImages)
            } catch (e: Exception) {
                Resource.error(e.toString(), randomImages)
            } finally {
                mutableRandomImages.postValue(resource)
            }
        }
    }
}