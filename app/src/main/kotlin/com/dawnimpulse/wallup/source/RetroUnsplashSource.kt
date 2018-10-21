package com.dawnimpulse.wallup.source

import com.dawnimpulse.wallup.pojo.*
import com.dawnimpulse.wallup.utils.C
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

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

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-05-13 by Saksham
 *
 * @note Updates :
 *  Saksham - 2018 05 20 - recent - curated photos
 *  Saksham - 2018 09 01 - recent - image details
 *  Saksham - 2018 09 02 - recent - random user images
 *  Saksham - 2018 09 08 - recent - featured collections
 *  Saksham - 2018 09 14 - recent - users's collections
 *  Saksham - 2018 09 22 - recent - random photos with a tag
 *  Saksham - 2018 10 21 - recent - add image in collection
 */
interface RetroUnsplashSource {


    //________________________________
    //           PHOTOS
    //________________________________


    // ------------------------------
    //         Latest Photos
    // ------------------------------
    @GET("/photos?per_page=30")
    fun getLatestPhotos(
            @Header(C.AUTHORIZATION) authorization: String,
            @Query(C.PAGE) page: Int
    ): Call<List<ImagePojo>>


    // ------------------------------
    //      Popular Photos
    // ------------------------------
    @GET("/photos?order_by=popular&per_page=30")
    fun getPopularPhotos(
            @Header(C.AUTHORIZATION) authorization: String,
            @Query(C.PAGE) page: Int
    ): Call<List<ImagePojo>>


    // ------------------------------
    //      Curated Photos
    // ------------------------------
    @GET("/photos/curated?per_page=30")
    fun getCuratedPhotos(
            @Header(C.AUTHORIZATION) authorization: String,
            @Query(C.PAGE) page: Int
    ): Call<List<ImagePojo>>

    // ------------------------------
    //      Search Photos
    // ------------------------------
    @GET("/photos/random?count=30")
    fun randomImagesTag(
            @Header(C.AUTHORIZATION) authorization: String,
            @Query(C.QUERY) query: String
    ): Call<List<ImagePojo>>

    // ------------------------------
    //      Details Photo
    // ------------------------------
    @GET("/photos/{id}")
    fun getImage(
            @Header(C.AUTHORIZATION) authorization: String,
            @Path(C.ID) id: String
    ): Call<ImagePojo>

    // ------------------------------
    //      Random Photos
    // ------------------------------
    @GET("/photos/random?count=30")
    fun randomImages(
            @Header(C.AUTHORIZATION) authorization: String
    ): Call<List<ImagePojo>>

    // ------------------------------
    //      Download Photo
    // ------------------------------
    @GET
    fun imageDownloaded(
            @Header(C.AUTHORIZATION) authorization: String,
            @Url url: String
    ): Call<JSONObject>

    // ------------------------------
    //      Like Photo
    // ------------------------------
    @POST("/photos/{id}/like")
    fun likeImage(
            @Header(C.AUTHORIZATION) authorization: String,
            @Path(C.ID) id: String
    ): Call<ImagePojo>

    // ------------------------------
    //      UnLike Photo
    // ------------------------------
    @DELETE("/photos/{id}/like")
    fun unlikeImage(
            @Header(C.AUTHORIZATION) authorization: String,
            @Path(C.ID) id: String
    ): Call<ImagePojo>

    //________________________________
    //           USER
    //________________________________


    // ------------------------------
    //      User Details
    // ------------------------------
    @GET("/users/{username}")
    fun userDetails(
            @Header(C.AUTHORIZATION) authorization: String,
            @Path("username") username: String
    ): Call<UserPojo>


    // ------------------------------
    //      User Photos
    // ------------------------------
    @GET("/users/{username}/photos")
    fun userPhotos(
            @Header(C.AUTHORIZATION) authorization: String,
            @Path(C.USERNAME) username: String,
            @Query(C.PAGE) page: Int,
            @Query(C.PER_PAGE) count: Int
    ): Call<List<ImagePojo>>


    // ------------------------------
    //      Random User Photos
    // ------------------------------
    @GET("/photos/random?count=30")
    fun randomUserImages(
            @Header(C.AUTHORIZATION) authorization: String,
            @Query(C.USERNAME) username: String
    ): Call<List<ImagePojo>>

    // ------------------------------
    //        User Profile
    // ------------------------------
    @GET("/me")
    fun selfProfile(
            @Header(C.AUTHORIZATION) authorization: String
    ): Call<UserPojo>

    // ------------------------------
    //      User Liked Photos
    // ------------------------------
    @GET("/users/{username}/likes?per_page=30")
    fun userLikedPhotos(
            @Header(C.AUTHORIZATION) authorization: String,
            @Path(C.USERNAME) username: String,
            @Query(C.PAGE) page: Int
    ): Call<List<ImagePojo>>

    // ------------------------------
    //      User Collections
    // ------------------------------
    @GET("/users/{username}/collections?order_by=updated")
    fun getUserCollections(
            @Header(C.AUTHORIZATION) authorization: String,
            @Path(C.USERNAME) username: String,
            @Query(C.PAGE) page: Int,
            @Query(C.PER_PAGE) count: Int
    ): Call<List<CollectionPojo>>

    // ------------------------------
    //    Add Photo to Collection
    // ------------------------------
    @POST("/collections/{id}/add")
    fun addImageInCollection(
            @Header(C.AUTHORIZATION) authorization: String,
            @Path(C.ID) collection_id: String,
            @Query(C.PHOTO_ID) photo_id: String
    ): Call<ImagePojo>

    // ------------------------------
    //    Remove Photo to Collection
    // ------------------------------
    @DELETE("/collections/{id}/remove")
    fun removeImageInCollection(
            @Header(C.AUTHORIZATION) authorization: String,
            @Path(C.ID) collection_id: String,
            @Query(C.PHOTO_ID) photo_id: String
    ): Call<ImagePojo>


    //________________________________
    //          Collection
    //________________________________


    // ------------------------------
    //      Curated Collection
    // ------------------------------
    @GET("/collections/curated?per_page=30")
    fun curatedCollections(
            @Header(C.AUTHORIZATION) authorization: String,
            @Query(C.PAGE) page: Int
    ): Call<List<CollectionPojo>>


    // ------------------------------
    //      Featured Collection
    // ------------------------------
    @GET("/collections/featured?per_page=30")
    fun featuredCollections(
            @Header(C.AUTHORIZATION) authorization: String,
            @Query(C.PAGE) page: Int
    ): Call<List<CollectionPojo>>

    // ------------------------------
    //      Collection Photos
    // ------------------------------
    @GET("/collections/{id}/photos")
    fun collectionPhotos(
            @Header(C.AUTHORIZATION) authorization: String,
            @Path(C.ID) id: String,
            @Query(C.PER_PAGE) per_page: Int,
            @Query(C.PAGE) page: Int
    ): Call<List<ImagePojo>>

    // ------------------------------
    //    Curated Collection Photos
    // ------------------------------
    @GET("/collections/curated/{id}/photos")
    fun curatedCollectionPhotos(
            @Header(C.AUTHORIZATION) authorization: String,
            @Path(C.ID) id: String,
            @Query(C.PER_PAGE) per_page: Int,
            @Query(C.PAGE) page: Int
    ): Call<List<ImagePojo>>

    // ------------------------------
    //    Random Collection Photos
    // ------------------------------
    @GET("/photos/random?count=30")
    fun randomCollectionPhotos(
            @Header(C.AUTHORIZATION) authorization: String,
            @Query(C.COLLECTIONS) id: String
    ): Call<List<ImagePojo>>


    //________________________________
    //           EXTRAS
    //________________________________


    // ------------------------------
    //      Bearer Token
    // ------------------------------
    @POST
    fun bearerToken(
            @Url url: String,
            @Body body: BearerBody
    ): Call<BearerToken>

    // ------------------------------
    //         Post Slack
    // ------------------------------
    @POST
    fun postSlack(
            @Url url: String,
            @Body body: SlackPojo
    ): Call<Any>

}
