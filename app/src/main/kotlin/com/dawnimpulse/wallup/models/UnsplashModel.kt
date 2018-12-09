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

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.dawnimpulse.wallup.pojo.BearerBody
import com.dawnimpulse.wallup.pojo.ImagePojo
import com.dawnimpulse.wallup.repositories.UnsplashRepository
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Config
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs

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
 *  2018 10 01 - master - Saksham - generate bearer token
 *  2018 10 04 - master - Saksham - self profile
 *  2018 10 08 - master - Saksham - user liked photos
 *  2018 10 21 - master - Saksham - add image in user's collection
 */
class UnsplashModel() {
    private lateinit var lifecycle: Lifecycle

    // constructor
    constructor(lifecycle: Lifecycle) : this() {
        this.lifecycle = lifecycle
    }

    // Get latest photos
    fun getLatestImages(page: Int, callback: (Any?, Any?) -> Unit) {
        if (Config.CONNECTED)
            UnsplashRepository.getLatestPhotos(page) { e, r ->
                lifecycle.addObserver(object : LifecycleObserver {
                    var once = true
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    fun onStart() {
                        if (once) {
                            callback(e, r)
                            once = false
                        }
                    }
                })
            }
        else {
            if (Prefs.contains(C.LATEST)) {
                callback(null, (Gson().fromJson(Prefs.getString(C.LATEST, null), Array<ImagePojo>::class.java)).toList())
            } else
                callback("internet not available !!", null)
        }
    }

    // Get curated photos
    fun getCuratedImages(page: Int, callback: (Any?, Any?) -> Unit) {
        if (Config.CONNECTED)
            UnsplashRepository.getCuratedPhotos(page) { e, r ->
                lifecycle.addObserver(object : LifecycleObserver {
                    var once = true
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    fun onStart() {
                        if (once) {
                            callback(e, r)
                            once = false
                        }
                    }
                })
            }
        else
            callback("internet not available !!", null)
    }

    // Downloaded a photo
    fun downloadedPhoto(url: String) {
        UnsplashRepository.downloadedPhoto(url)
    }

    // User details
    fun userDetails(username: String, callback: (Any?, Any?) -> Unit) {
        if (Config.CONNECTED)
            UnsplashRepository.userDetails(username) { e, r ->
                lifecycle.addObserver(object : LifecycleObserver {
                    var once = true
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    fun onStart() {
                        if (once) {
                            callback(e, r)
                            once = false
                        }
                    }
                })
            }
        else
            callback("internet not available !!", null)
    }

    // Get user photos
    fun userPhotos(page: Int, count: Int, username: String, callback: (Any?, Any?) -> Unit) {
        if (Config.CONNECTED)
            UnsplashRepository.userPhotos(page, count, username) { e, r ->
                lifecycle.addObserver(object : LifecycleObserver {
                    var once = true
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    fun onStart() {
                        if (once) {
                            callback(e, r)
                            once = false
                        }
                    }
                })
            }
        else
            callback("internet not available !!", null)
    }

    // Get image details
    fun getImage(id: String, callback: (Any?, Any?) -> Unit) {
        if (Config.CONNECTED)
            UnsplashRepository.getImage(id) { e, r ->
                lifecycle.addObserver(object : LifecycleObserver {
                    var once = true
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    fun onStart() {
                        if (once) {
                            callback(e, r)
                            once = false
                        }
                    }
                })
            }
        else
            callback("internet not available !!", null)

    }

    // Get random images
    fun randomImages(callback: (Any?, Any?) -> Unit) {
        if (Config.CONNECTED)
            UnsplashRepository.randomImages() { e, r ->
                lifecycle.addObserver(object : LifecycleObserver {
                    var once = true
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    fun onStart() {
                        if (once) {
                            callback(e, r)
                            once = false
                        }
                    }
                })
            }
        else {
            if (Prefs.contains(C.RANDOM)) {
                callback(null, (Gson().fromJson(Prefs.getString(C.RANDOM, null), Array<ImagePojo>::class.java)).toList())
            } else
                callback("internet not available !!", null)
        }

    }

    // Get random user images
    fun randomUserImages(username: String, callback: (Any?, Any?) -> Unit) {
        if (Config.CONNECTED)
            UnsplashRepository.randomUserImages(username) { e, r ->
                lifecycle.addObserver(object : LifecycleObserver {
                    var once = true
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    fun onStart() {
                        if (once) {
                            callback(e, r)
                            once = false
                        }
                    }
                })
            }
        else
            callback("internet not available !!", null)

    }

    // curated collections
    fun curatedCollections(page: Int, callback: (Any?, Any?) -> Unit) {
        if (Config.CONNECTED)
            UnsplashRepository.curatedCollections(page) { e, r ->
                lifecycle.addObserver(object : LifecycleObserver {
                    var once = true
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    fun onStart() {
                        if (once) {
                            callback(e, r)
                            once = false
                        }
                    }
                })
            }
        else
            callback("internet not available !!", null)
    }

    // featured collections
    fun featuredCollections(page: Int, callback: (Any?, Any?) -> Unit) {
        if (Config.CONNECTED)
            UnsplashRepository.featuredCollections(page) { e, r ->
                lifecycle.addObserver(object : LifecycleObserver {
                    var once = true
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    fun onStart() {
                        if (once) {
                            callback(e, r)
                            once = false
                        }
                    }
                })
            }
        else
            callback("internet not available !!", null)
    }

    // collection photo
    fun collectionPhotos(id: String, page: Int, count: Int, callback: (Any?, Any?) -> Unit) {
        if (Config.CONNECTED)
            UnsplashRepository.collectionPhotos(id, page, count) { e, r ->
                lifecycle.addObserver(object : LifecycleObserver {
                    var once = true
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    fun onStart() {
                        if (once) {
                            callback(e, r)
                            once = false
                        }
                    }
                })
            }
        else
            callback("internet not available !!", null)
    }

    // curated collection photo
    fun curatedCollectionPhotos(id: String, page: Int, count: Int, callback: (Any?, Any?) -> Unit) {
        if (Config.CONNECTED)
            UnsplashRepository.curatedCollectionPhotos(id, page, count) { e, r ->
                lifecycle.addObserver(object : LifecycleObserver {
                    var once = true
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    fun onStart() {
                        if (once) {
                            callback(e, r)
                            once = false
                        }
                    }
                })
            }
        else
            callback("internet not available !!", null)
    }

    // random collection photo
    fun randomCollectionPhotos(id: String, callback: (Any?, Any?) -> Unit) {
        if (Config.CONNECTED)
            UnsplashRepository.randomCollectionPhotos(id) { e, r ->
                lifecycle.addObserver(object : LifecycleObserver {
                    var once = true
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    fun onStart() {
                        if (once) {
                            callback(e, r)
                            once = false
                        }
                    }
                })
            }
        else
            callback("internet not available !!", null)
    }

    // user's collections
    fun userCollections(username: String, page: Int, count: Int, callback: (Any?, Any?) -> Unit) {
        if (Config.CONNECTED)
            UnsplashRepository.userCollections(username, page, count) { e, r ->
                lifecycle.addObserver(object : LifecycleObserver {
                    var once = true
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    fun onStart() {
                        if (once) {
                            callback(e, r)
                            once = false
                        }
                    }
                })
            }
        else
            callback("internet not available !!", null)
    }

    // random images with a keyword
    fun randomImagesTag(keyword: String, callback: (Any?, Any?) -> Unit) {
        if (Config.CONNECTED)
            UnsplashRepository.randomImagesTag(keyword) { e, r ->
                lifecycle.addObserver(object : LifecycleObserver {
                    var once = true
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    fun onStart() {
                        if (once) {
                            callback(e, r)
                            once = false
                        }
                    }
                })
            }
        else
            callback("internet not available !!", null)
    }

    // like a photo
    fun likePhoto(id: String) {
        UnsplashRepository.likePhoto(id)
    }

    // like a photo
    fun unlikePhoto(id: String) {
        UnsplashRepository.unlikePhoto(id)
    }

    // generate bearer token
    fun bearerToken(body: BearerBody, callback: (Any?, Any?) -> Unit) {
        if (Config.CONNECTED)
            UnsplashRepository.bearerToken(body) { e, r ->
                lifecycle.addObserver(object : LifecycleObserver {
                    var once = true
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    fun onStart() {
                        if (once) {
                            callback(e, r)
                            once = false
                        }
                    }
                })
            }
        else
            callback("internet not available !!", null)
    }

    // self profile
    fun selfProfile(callback: (Any?, Any?) -> Unit) {
        if (Config.CONNECTED)
            UnsplashRepository.selfProfile() { e, r ->
                lifecycle.addObserver(object : LifecycleObserver {
                    var once = true
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    fun onStart() {
                        if (once) {
                            callback(e, r)
                            once = false
                        }
                    }
                })
            }
        else
            callback("internet not available !!", null)
    }

    // user liked photos
    fun userLikedPhotos(username: String, page: Int, callback: (Any?, Any?) -> Unit) {
        if (Config.CONNECTED)
            UnsplashRepository.userLikedPhotos(username, page) { e, r ->
                lifecycle.addObserver(object : LifecycleObserver {
                    var once = true
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    fun onStart() {
                        if (once) {
                            callback(e, r)
                            once = false
                        }
                    }
                })
            }
        else
            callback("internet not available !!", null)
    }

    // add image in user's collection
    fun addImageInCollection(photo_id: String, collection_id: String, callback: (Any?, Any?) -> Unit) {
        if (Config.CONNECTED)
            UnsplashRepository.addImageInCollection(photo_id, collection_id) { e, r ->
                lifecycle.addObserver(object : LifecycleObserver {
                    var once = true
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    fun onStart() {
                        if (once) {
                            callback(e, r)
                            once = false
                        }
                    }
                })
            }
        else
            callback("internet not available !!", null)
    }

    // remove image in user's collection
    fun removeImageInCollection(photo_id: String, collection_id: String, callback: (Any?, Any?) -> Unit) {
        if (Config.CONNECTED)
            UnsplashRepository.removeImageInCollection(photo_id, collection_id) { e, r ->
                lifecycle.addObserver(object : LifecycleObserver {
                    var once = true
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    fun onStart() {
                        if (once) {
                            callback(e, r)
                            once = false
                        }
                    }
                })
            }
        else
            callback("internet not available !!", null)
    }

    // image stats
    fun imageStats(id: String, callback: (Any?, Any?) -> Unit) {
        if (Config.CONNECTED)
            UnsplashRepository.imageStats(id) { e, r ->
                lifecycle.addObserver(object : LifecycleObserver {
                    var once = true
                    @OnLifecycleEvent(Lifecycle.Event.ON_START)
                    fun onStart() {
                        if (once) {
                            callback(e, r)
                            once = false
                        }
                    }
                })
            }
        else
            callback("internet not available !!", null)
    }
}