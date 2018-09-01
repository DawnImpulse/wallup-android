/*
ISC License

Copyright 2018, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/package com.dawnimpulse.wallup.models

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.dawnimpulse.wallup.respositories.UnsplashRepository

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-05-20 by Saksham
 *
 * @note Updates :
 *  2018 08 03 - recent - Saksham - downloaded a photo
 *  2018 08 31 - recent - Saksham - user details
 *  2018 09 01 - recent - Saksham - image details
 */
class UnsplashModel() {
    lateinit var lifecycle: Lifecycle

    /**
     * constructor
     */
    constructor(lifecycle: Lifecycle) : this() {
        this.lifecycle = lifecycle
    }

    /**
     * Get latest photos
     * @param page
     * @param callback
     */
    fun getLatestImages(page: Int, callback: (Any?, Any?) -> Unit) {
        UnsplashRepository.getLatestPhotos(page) { error, response ->
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                callback(error, response)
        }
    }

    /**
     * Get popular photos
     * @param page
     * @param callback
     */
    fun getPopularImages(page: Int, callback: (Any?, Any?) -> Unit) {
        UnsplashRepository.getPopularPhotos(page) { error, response ->
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                callback(error, response)
        }
    }

    /**
     * Get curated photos
     * @param page
     * @param callback
     */
    fun getCuratedImages(page: Int, callback: (Any?, Any?) -> Unit) {
        UnsplashRepository.getCuratedPhotos(page) { error, response ->
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                callback(error, response)
        }
    }

    /**
     * Downloaded a photo
     * @param id
     */
    fun downloadedPhoto(id: String) {
        UnsplashRepository.downloadedPhoto(id)
    }

    /**
     * User details
     */
    fun userDetails(username: String, callback: (Any?, Any?) -> Unit) {
        UnsplashRepository.userDetails(username) { e, r ->
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                callback(e, r)
        }
    }

    /**
     * Get user photos
     * @param page
     * @param count - no of images to return
     * @param username
     * @param callback
     */
    fun userPhotos(page: Int, count: Int, username: String, callback: (Any?, Any?) -> Unit) {
        UnsplashRepository.userPhotos(page, count, username) { error, response ->
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                callback(error, response)
        }
    }

    /**
     * Get image details
     * @param id
     * @param callback
     */
    fun getImage(id: String, callback: (Any?, Any?) -> Unit) {
        UnsplashRepository.getImage(id) { e, r ->
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                callback(e, r)
            else
                lifecycle.addObserver(object : LifecycleObserver {
                    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
                    fun onResume() {
                        callback(e, r)
                    }
                })
        }

    }
}