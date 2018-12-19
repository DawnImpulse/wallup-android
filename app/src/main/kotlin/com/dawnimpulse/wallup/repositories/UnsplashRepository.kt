/*
ISC License

Copyright 2018, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/
package com.dawnimpulse.wallup.repositories

import com.dawnimpulse.wallup.network.RetroApiClient
import com.dawnimpulse.wallup.pojo.*
import com.dawnimpulse.wallup.source.RetroUnsplashSource
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Config
import com.dawnimpulse.wallup.utils.ErrorUtils
import com.dawnimpulse.wallup.utils.L
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
 *  2018 09 08 - master - Saksham - featured collections
 *  2018 09 14 - master - Saksham - user's collections
 *  2018 09 22 - master - Saksham - random images tag
 *  2018 10 01 - master - Saksham - generate bearer token
 *  2018 10 04 - master - Saksham - user profile
 *  2018 10 08 - master - Saksham - user likes
 *  2018 10 21 - master - Saksham - add image in user collection
 *  2018 12 09 - master - Saksham - image statistics
 *  2018 12 11 - master - Saksham - new collection
 *  2018 12 19 - master - Saksham - delete collection
 */
object UnsplashRepository {
    private val NAME = "UnsplashRepository"

    // Get latest photos
    fun getLatestPhotos(page: Int, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.getLatestPhotos(
                Config.apiKey(),
                page)

        call.enqueue(object : Callback<List<ImagePojo>> {

            override fun onResponse(call: Call<List<ImagePojo>>?, response: Response<List<ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // Get popular photos
    fun getPopularPhotos(page: Int, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.getPopularPhotos(
                Config.apiKey(),
                page)

        call.enqueue(object : Callback<List<ImagePojo>> {

            override fun onResponse(call: Call<List<ImagePojo>>?, response: Response<List<ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // Get curated photos
    fun getCuratedPhotos(page: Int, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.getCuratedPhotos(
                Config.apiKey(),
                page)

        call.enqueue(object : Callback<List<ImagePojo>> {

            override fun onResponse(call: Call<List<ImagePojo>>?, response: Response<List<ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // Downloaded a photo
    fun downloadedPhoto(url: String) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.imageDownloaded(
                Config.apiKey(),
                url)

        call.enqueue(object : Callback<JSONObject> {

            override fun onResponse(call: Call<JSONObject>?, response: Response<JSONObject>) {
                L.d(NAME, "File downloaded linked")
            }

            override fun onFailure(call: Call<JSONObject>?, t: Throwable?) {
                L.e(NAME, t.toString())
            }
        })
    }

    // User Details
    fun userDetails(username: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.userDetails(
                Config.apiKey(),
                username)

        call.enqueue(object : Callback<UserPojo> {

            override fun onResponse(call: Call<UserPojo>?, response: Response<UserPojo>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<UserPojo>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // Get user photos
    fun userPhotos(page: Int, count: Int, username: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.userPhotos(
                Config.apiKey(),
                username,
                page,
                count)

        call.enqueue(object : Callback<List<ImagePojo>> {

            override fun onResponse(call: Call<List<ImagePojo>>?, response: Response<List<ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // Get image details
    fun getImage(id: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.getImage(
                Config.apiKey(),
                id
        )

        call.enqueue(object : Callback<ImagePojo> {

            override fun onResponse(call: Call<ImagePojo>?, response: Response<ImagePojo>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<ImagePojo>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // Get random images
    fun randomImages(callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.randomImages(
                Config.apiKey()
        )

        call.enqueue(object : Callback<List<ImagePojo>> {

            override fun onResponse(call: Call<List<ImagePojo>>?, response: Response<List<ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // Get random user images
    fun randomUserImages(username: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.randomUserImages(
                Config.apiKey(),
                username
        )

        call.enqueue(object : Callback<List<ImagePojo>> {

            override fun onResponse(call: Call<List<ImagePojo>>?, response: Response<List<ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // curated curatedCollections
    fun curatedCollections(page: Int, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.curatedCollections(
                Config.apiKey(),
                page
        )

        call.enqueue(object : Callback<List<CollectionPojo>> {

            override fun onResponse(call: Call<List<CollectionPojo>>?, response: Response<List<CollectionPojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<CollectionPojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // featured curatedCollections
    fun featuredCollections(page: Int, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.featuredCollections(
                Config.apiKey(),
                page
        )

        call.enqueue(object : Callback<List<CollectionPojo>> {

            override fun onResponse(call: Call<List<CollectionPojo>>?, response: Response<List<CollectionPojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<CollectionPojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // collection photos
    fun collectionPhotos(id: String, page: Int, count: Int, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.collectionPhotos(
                Config.apiKey(),
                id,
                count,
                page
        )

        call.enqueue(object : Callback<List<ImagePojo>> {

            override fun onResponse(call: Call<List<ImagePojo>>?, response: Response<List<ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // curated collection photos
    fun curatedCollectionPhotos(id: String, page: Int, count: Int, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.curatedCollectionPhotos(
                Config.apiKey(),
                id,
                count,
                page
        )

        call.enqueue(object : Callback<List<ImagePojo>> {

            override fun onResponse(call: Call<List<ImagePojo>>?, response: Response<List<ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // random collection photos
    fun randomCollectionPhotos(id: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.randomCollectionPhotos(
                Config.apiKey(),
                id
        )

        call.enqueue(object : Callback<List<ImagePojo>> {

            override fun onResponse(call: Call<List<ImagePojo>>?, response: Response<List<ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // user's collections
    fun userCollections(username: String, page: Int, count: Int, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.getUserCollections(
                Config.apiKey(),
                username,
                page,
                count
        )

        call.enqueue(object : Callback<List<CollectionPojo>> {

            override fun onResponse(call: Call<List<CollectionPojo>>?, response: Response<List<CollectionPojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<CollectionPojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // random images with a tag
    fun randomImagesTag(keyword: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.randomImagesTag(
                Config.apiKey(),
                keyword
        )

        call.enqueue(object : Callback<List<ImagePojo>> {

            override fun onResponse(call: Call<List<ImagePojo>>?, response: Response<List<ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // like a photo
    fun likePhoto(id: String) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.likeImage(
                Config.apiKey(),
                id
        )

        call.enqueue(object : Callback<ImagePojo> {

            override fun onResponse(call: Call<ImagePojo>?, response: Response<ImagePojo>) {
                if (!response.isSuccessful)
                    L.dO(NAME, ErrorUtils.parseError(response))
                else
                    L.d(NAME, "Liked Photo")
            }

            override fun onFailure(call: Call<ImagePojo>?, t: Throwable?) {
                t?.toString()?.let { L.d(NAME, it) }
            }
        })
    }

    // unlike a photo
    fun unlikePhoto(id: String) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.unlikeImage(
                Config.apiKey(),
                id
        )

        call.enqueue(object : Callback<ImagePojo> {

            override fun onResponse(call: Call<ImagePojo>?, response: Response<ImagePojo>) {
                if (!response.isSuccessful)
                    L.dO(NAME, ErrorUtils.parseError(response))
                else
                    L.d(NAME, "Unliked Photo")
            }

            override fun onFailure(call: Call<ImagePojo>?, t: Throwable?) {
                t?.toString()?.let { L.d(NAME, it) }
            }
        })
    }

    // generate bearer token
    fun bearerToken(body: BearerBody, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.bearerToken(
                C.UNSPLASH_TOKEN,
                body
        )

        call.enqueue(object : Callback<BearerToken> {

            override fun onResponse(call: Call<BearerToken>?, response: Response<BearerToken>) {
                if (response.isSuccessful) {
                    callback(null, response.body())
                } else {
                    callback(ErrorUtils.parseErrorAuth(response), null)
                }
            }

            override fun onFailure(call: Call<BearerToken>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // user profile
    fun selfProfile(callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.selfProfile(
                Config.apiKey()
        )

        call.enqueue(object : Callback<UserPojo> {

            override fun onResponse(call: Call<UserPojo>?, response: Response<UserPojo>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<UserPojo>?, t: Throwable?) {
                t?.toString()?.let { L.d(NAME, it) }
            }
        })
    }

    // user liked photos
    fun userLikedPhotos(username: String, page: Int, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.userLikedPhotos(
                Config.apiKey(),
                username,
                page)

        call.enqueue(object : Callback<List<ImagePojo>> {

            override fun onResponse(call: Call<List<ImagePojo>>?, response: Response<List<ImagePojo>>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<List<ImagePojo>>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // add image in collection
    fun addImageInCollection(photo_id: String, collection_id: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.addImageInCollection(
                Config.apiKey(),
                collection_id,
                photo_id
        )

        call.enqueue(object : Callback<ImagePojo> {

            override fun onResponse(call: Call<ImagePojo>?, response: Response<ImagePojo>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<ImagePojo>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // remove image in collection
    fun removeImageInCollection(photo_id: String, collection_id: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.removeImageInCollection(
                Config.apiKey(),
                collection_id,
                photo_id
        )

        call.enqueue(object : Callback<ImagePojo> {

            override fun onResponse(call: Call<ImagePojo>?, response: Response<ImagePojo>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<ImagePojo>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // image stats
    fun imageStats(id: String, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.imageStats(
                Config.apiKey(),
                id
        )

        call.enqueue(object : Callback<ImageStatsPojo> {

            override fun onResponse(call: Call<ImageStatsPojo>?, response: Response<ImageStatsPojo>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<ImageStatsPojo>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // new collection
    fun newCollection(body: NewCollections, callback: (Any?, Any?) -> Unit) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.newCollection(
                Config.apiKey(),
                body
        )

        call.enqueue(object : Callback<CollectionPojo> {

            override fun onResponse(call: Call<CollectionPojo>?, response: Response<CollectionPojo>) {
                if (response.isSuccessful)
                    callback(null, response.body())
                else
                    callback(ErrorUtils.parseError(response), null)
            }

            override fun onFailure(call: Call<CollectionPojo>?, t: Throwable?) {
                t?.toString()?.let { callback(t.toString(), null) }
            }
        })
    }

    // delete collection
    fun deleteCollection(id: String) {
        val apiClient = RetroApiClient.getClient()!!.create(RetroUnsplashSource::class.java)
        val call = apiClient.deleteCollection(
                Config.apiKey(),
                id
        )

        call.enqueue(object : Callback<CollectionPojo> {

            override fun onResponse(call: Call<CollectionPojo>?, response: Response<CollectionPojo>) {
            }

            override fun onFailure(call: Call<CollectionPojo>?, t: Throwable?) {

            }
        })
    }
}

