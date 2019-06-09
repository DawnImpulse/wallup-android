/*
ISC License

Copyright 2018-2019, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/
package org.sourcei.wallup.deprecated.source

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
 *  Saksham - 2018 12 19 - master - delete collection
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
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Query(org.sourcei.wallup.deprecated.utils.C.PAGE) page: Int
    ): Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>


    // ------------------------------
    //      Popular Photos
    // ------------------------------
    @GET("/photos?order_by=popular&per_page=30")
    fun getPopularPhotos(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Query(org.sourcei.wallup.deprecated.utils.C.PAGE) page: Int
    ): Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>


    // ------------------------------
    //      Curated Photos
    // ------------------------------
    @GET("/photos/curated?per_page=30")
    fun getCuratedPhotos(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Query(org.sourcei.wallup.deprecated.utils.C.PAGE) page: Int
    ): Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>

    // ------------------------------
    //      Search Photos
    // ------------------------------
    @GET("/photos/random?count=30")
    fun randomImagesTag(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Query(org.sourcei.wallup.deprecated.utils.C.QUERY) query: String
    ): Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>

    // ------------------------------
    //      Details Photo
    // ------------------------------
    @GET("/photos/{id}")
    fun getImage(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Path(org.sourcei.wallup.deprecated.utils.C.ID) id: String
    ): Call<org.sourcei.wallup.deprecated.pojo.ImagePojo>

    // ------------------------------
    //      Random Photos
    // ------------------------------
    @GET("/photos/random?count=30")
    fun randomImages(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String
    ): Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>

    // ------------------------------
    //      Download Photo
    // ------------------------------
    @GET
    fun imageDownloaded(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Url url: String
    ): Call<JSONObject>

    // ------------------------------
    //      Like Photo
    // ------------------------------
    @POST("/photos/{id}/like")
    fun likeImage(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Path(org.sourcei.wallup.deprecated.utils.C.ID) id: String
    ): Call<org.sourcei.wallup.deprecated.pojo.ImagePojo>

    // ------------------------------
    //      UnLike Photo
    // ------------------------------
    @DELETE("/photos/{id}/like")
    fun unlikeImage(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Path(org.sourcei.wallup.deprecated.utils.C.ID) id: String
    ): Call<org.sourcei.wallup.deprecated.pojo.ImagePojo>

    // ------------------------------
    //      Photo Stats
    // ------------------------------
    @GET("/photos/{id}/statistics")
    fun imageStats(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Path(org.sourcei.wallup.deprecated.utils.C.ID) id: String
    ): Call<org.sourcei.wallup.deprecated.pojo.ImageStatsPojo>

    //________________________________
    //           USER
    //________________________________


    // ------------------------------
    //      User Details
    // ------------------------------
    @GET("/users/{username}")
    fun userDetails(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Path("username") username: String
    ): Call<org.sourcei.wallup.deprecated.pojo.UserPojo>


    // ------------------------------
    //      User Photos
    // ------------------------------
    @GET("/users/{username}/photos")
    fun userPhotos(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Path(org.sourcei.wallup.deprecated.utils.C.USERNAME) username: String,
            @Query(org.sourcei.wallup.deprecated.utils.C.PAGE) page: Int,
            @Query(org.sourcei.wallup.deprecated.utils.C.PER_PAGE) count: Int
    ): Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>


    // ------------------------------
    //      Random User Photos
    // ------------------------------
    @GET("/photos/random?count=30")
    fun randomUserImages(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Query(org.sourcei.wallup.deprecated.utils.C.USERNAME) username: String
    ): Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>

    // ------------------------------
    //        User Profile
    // ------------------------------
    @GET("/me")
    fun selfProfile(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String
    ): Call<org.sourcei.wallup.deprecated.pojo.UserPojo>

    // ------------------------------
    //      User Liked Photos
    // ------------------------------
    @GET("/users/{username}/likes?per_page=30")
    fun userLikedPhotos(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Path(org.sourcei.wallup.deprecated.utils.C.USERNAME) username: String,
            @Query(org.sourcei.wallup.deprecated.utils.C.PAGE) page: Int
    ): Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>

    // ------------------------------
    //      User Collections
    // ------------------------------
    @GET("/users/{username}/collections?order_by=updated")
    fun getUserCollections(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Path(org.sourcei.wallup.deprecated.utils.C.USERNAME) username: String,
            @Query(org.sourcei.wallup.deprecated.utils.C.PAGE) page: Int,
            @Query(org.sourcei.wallup.deprecated.utils.C.PER_PAGE) count: Int
    ): Call<List<org.sourcei.wallup.deprecated.pojo.CollectionPojo>>

    // ------------------------------
    //    Add Photo to Collection
    // ------------------------------
    @POST("/collections/{id}/add")
    fun addImageInCollection(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Path(org.sourcei.wallup.deprecated.utils.C.ID) collection_id: String,
            @Query(org.sourcei.wallup.deprecated.utils.C.PHOTO_ID) photo_id: String
    ): Call<org.sourcei.wallup.deprecated.pojo.ImagePojo>

    // ------------------------------
    //    Remove Photo to Collection
    // ------------------------------
    @DELETE("/collections/{id}/remove")
    fun removeImageInCollection(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Path(org.sourcei.wallup.deprecated.utils.C.ID) collection_id: String,
            @Query(org.sourcei.wallup.deprecated.utils.C.PHOTO_ID) photo_id: String
    ): Call<org.sourcei.wallup.deprecated.pojo.ImagePojo>


    //________________________________
    //          Collection
    //________________________________


    // ------------------------------
    //      Curated Collection
    // ------------------------------
    @GET("/collections/curated?per_page=30")
    fun curatedCollections(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Query(org.sourcei.wallup.deprecated.utils.C.PAGE) page: Int
    ): Call<List<org.sourcei.wallup.deprecated.pojo.CollectionPojo>>


    // ------------------------------
    //      Featured Collection
    // ------------------------------
    @GET("/collections/featured?per_page=30")
    fun featuredCollections(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Query(org.sourcei.wallup.deprecated.utils.C.PAGE) page: Int
    ): Call<List<org.sourcei.wallup.deprecated.pojo.CollectionPojo>>

    // ------------------------------
    //      Collection Photos
    // ------------------------------
    @GET("/collections/{id}/photos")
    fun collectionPhotos(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Path(org.sourcei.wallup.deprecated.utils.C.ID) id: String,
            @Query(org.sourcei.wallup.deprecated.utils.C.PER_PAGE) per_page: Int,
            @Query(org.sourcei.wallup.deprecated.utils.C.PAGE) page: Int
    ): Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>

    // ------------------------------
    //    Curated Collection Photos
    // ------------------------------
    @GET("/collections/curated/{id}/photos")
    fun curatedCollectionPhotos(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Path(org.sourcei.wallup.deprecated.utils.C.ID) id: String,
            @Query(org.sourcei.wallup.deprecated.utils.C.PER_PAGE) per_page: Int,
            @Query(org.sourcei.wallup.deprecated.utils.C.PAGE) page: Int
    ): Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>

    // ------------------------------
    //    Random Collection Photos
    // ------------------------------
    @GET("/photos/random?count=30")
    fun randomCollectionPhotos(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Query(org.sourcei.wallup.deprecated.utils.C.COLLECTIONS) id: String
    ): Call<List<org.sourcei.wallup.deprecated.pojo.ImagePojo>>

    // ------------------------------
    //        New Collection
    // ------------------------------
    @POST("/collections")
    fun newCollection(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Body body: org.sourcei.wallup.deprecated.pojo.NewCollections
    ): Call<org.sourcei.wallup.deprecated.pojo.CollectionPojo>

    // ------------------------------
    //       Delete Collection
    // ------------------------------
    @DELETE("/collections/{id}")
    fun deleteCollection(
            @Header(org.sourcei.wallup.deprecated.utils.C.AUTHORIZATION) authorization: String,
            @Path(org.sourcei.wallup.deprecated.utils.C.ID) id: String
    ): Call<org.sourcei.wallup.deprecated.pojo.CollectionPojo>

    //________________________________
    //           EXTRAS
    //________________________________


    // ------------------------------
    //      Bearer Token
    // ------------------------------
    @POST
    fun bearerToken(
            @Url url: String,
            @Body body: org.sourcei.wallup.deprecated.pojo.BearerBody
    ): Call<org.sourcei.wallup.deprecated.pojo.BearerToken>

    // ------------------------------
    //         Post Slack
    // ------------------------------
    @POST
    fun postSlack(
            @Url url: String,
            @Body body: org.sourcei.wallup.deprecated.pojo.SlackPojo
    ): Call<Any>

}
