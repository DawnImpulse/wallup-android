package com.dawnimpulse.wallup.pojo

import com.google.gson.annotations.SerializedName

/**
 * @info -
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2018-12-09 by Saksham
 * @note Updates :
 */
data class ImageStatsPojo(
        @SerializedName("id") val id: String,
        @SerializedName("views") val views: ImageStatsInfo,
        @SerializedName("downloads") val downloads: ImageStatsInfo,
        @SerializedName("likes") val likes: ImageStatsInfo
)

data class ImageStatsInfo(
        @SerializedName("total") val total: Int,
        @SerializedName("historical") val historical: ImageStatsHistorical
)

data class ImageStatsHistorical(
        @SerializedName("change") val change: Int,
        @SerializedName("quantity") val quantity: Int,
        @SerializedName("values") val values: List<ImageStatsData>
)

data class ImageStatsData(
        @SerializedName("data") val date: String,
        @SerializedName("value") val value: Int
)