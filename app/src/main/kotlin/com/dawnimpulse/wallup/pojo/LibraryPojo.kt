package com.dawnimpulse.wallup.pojo

import com.google.gson.annotations.SerializedName

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2018-12-15 by Saksham
 * @note Updates :
 */
data class LibraryPojo(
        @SerializedName("name") val name: String,
        @SerializedName("info") val info: String,
        @SerializedName("link") val link: String
)