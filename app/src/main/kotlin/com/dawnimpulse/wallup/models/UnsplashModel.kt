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
import com.dawnimpulse.wallup.repositories.UnsplashRepository

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-05-20 by Saksham
 *
 * @note Updates :
 *  2018 08 03 - recent - Saksham - downloaded a photo
 *  2018 08 31 - master - Saksham - user details
 *  2018 09 01 - master - Saksham - image details
 *  2018 09 02 - master - Saksham - random user images
 *  2018 09 08 - master - Saksham - featured curatedCollections
 *  2018 09 22 - master - Saksham - random images tag
 */
class UnsplashModel() {
    lateinit var lifecycle: Lifecycle

    // constructor
    constructor(lifecycle: Lifecycle) : this() {
        this.lifecycle = lifecycle
    }

    // Get latest photos
    fun getLatestImages(page: Int, callback: (Any?, Any?) -> Unit) {
        UnsplashRepository.getLatestPhotos(page) { error, response ->
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                callback(error, response)
        }
    }

    // Get popular photos
    fun getPopularImages(page: Int, callback: (Any?, Any?) -> Unit) {
        UnsplashRepository.getPopularPhotos(page) { error, response ->
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                callback(error, response)
        }
    }

    // Get curated photos
    fun getCuratedImages(page: Int, callback: (Any?, Any?) -> Unit) {
        UnsplashRepository.getCuratedPhotos(page) { error, response ->
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                callback(error, response)
        }
    }

    // Downloaded a photo
    fun downloadedPhoto(url: String) {
        UnsplashRepository.downloadedPhoto(url)
    }

    // User details
    fun userDetails(username: String, callback: (Any?, Any?) -> Unit) {
        UnsplashRepository.userDetails(username) { e, r ->
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                callback(e, r)
        }
    }

    // Get user photos
    fun userPhotos(page: Int, count: Int, username: String, callback: (Any?, Any?) -> Unit) {
        UnsplashRepository.userPhotos(page, count, username) { error, response ->
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
                callback(error, response)
        }
    }

    // Get image details
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

    // Get random images
    fun randomImages(callback: (Any?, Any?) -> Unit) {
        UnsplashRepository.randomImages() { e, r ->
            lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
                fun onResume() {
                    callback(e, r)
                }
            })
        }

    }

    // Get random user images
    fun randomUserImages(username: String, callback: (Any?, Any?) -> Unit) {
        UnsplashRepository.randomUserImages(username) { e, r ->
            lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
                fun onResume() {
                    callback(e, r)
                }
            })
        }

    }

    // curated collections
    fun curatedCollections(page: Int, callback: (Any?, Any?) -> Unit) {
        UnsplashRepository.curatedCollections(page) { e, r ->
            lifecycle.addObserver(object : LifecycleObserver {
                var once = true
                @OnLifecycleEvent(Lifecycle.Event.ON_START)
                fun onResume() {
                    if (once) {
                        callback(e, r)
                        once = false
                    }
                }
            })
        }
    }

    // featured collections
    fun featuredCollections(page: Int, callback: (Any?, Any?) -> Unit) {
        UnsplashRepository.featuredCollections(page) { e, r ->
            lifecycle.addObserver(object : LifecycleObserver {
                var once = true
                @OnLifecycleEvent(Lifecycle.Event.ON_START)
                fun onResume() {
                    if (once) {
                        callback(e, r)
                        once = false
                    }
                }
            })
        }
    }

    // collection photo
    fun collectionPhotos(id: String, page: Int, count: Int, callback: (Any?, Any?) -> Unit) {
        UnsplashRepository.collectionPhotos(id, page, count) { e, r ->
            lifecycle.addObserver(object : LifecycleObserver {
                var once = true
                @OnLifecycleEvent(Lifecycle.Event.ON_START)
                fun onResume() {
                    if (once) {
                        callback(e, r)
                        once = false
                    }
                }
            })
        }
    }

    // curated collection photo
    fun curatedCollectionPhotos(id: String, page: Int, count: Int, callback: (Any?, Any?) -> Unit) {
        UnsplashRepository.curatedCollectionPhotos(id, page, count) { e, r ->
            lifecycle.addObserver(object : LifecycleObserver {
                var once = true
                @OnLifecycleEvent(Lifecycle.Event.ON_START)
                fun onResume() {
                    if (once) {
                        callback(e, r)
                        once = false
                    }
                }
            })
        }
    }

    // random collection photo
    fun randomCollectionPhotos(id: String, callback: (Any?, Any?) -> Unit) {
        UnsplashRepository.randomCollectionPhotos(id) { e, r ->
            lifecycle.addObserver(object : LifecycleObserver {
                var once = true
                @OnLifecycleEvent(Lifecycle.Event.ON_START)
                fun onResume() {
                    if (once) {
                        callback(e, r)
                        once = false
                    }
                }
            })
        }
    }

    // user's collections
    fun userCollections(username: String, page: Int, count: Int, callback: (Any?, Any?) -> Unit) {
        UnsplashRepository.userCollections(username,page,count) { e, r ->
            lifecycle.addObserver(object : LifecycleObserver {
                var once = true
                @OnLifecycleEvent(Lifecycle.Event.ON_START)
                fun onResume() {
                    if (once) {
                        callback(e, r)
                        once = false
                    }
                }
            })
        }
    }

    // random images with a keyword
    fun randomImagesTag(keyword: String, callback: (Any?, Any?) -> Unit) {
        UnsplashRepository.randomImagesTag(keyword){ e,r ->
            lifecycle.addObserver(object : LifecycleObserver {
                var once = true
                @OnLifecycleEvent(Lifecycle.Event.ON_START)
                fun onResume() {
                    if (once) {
                        callback(e, r)
                        once = false
                    }
                }
            })
        }
    }
}