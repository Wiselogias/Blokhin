package com.wiselogia.kinopoiskclient.data

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("kinopoiskId", alternate = ["filmId"])
    val kinopoiskId: Int,
    @SerializedName("nameRu")
    val nameRu: String? = null,
    @SerializedName("nameOriginal")
    val nameOriginal: String? = null,
    @SerializedName("posterUrlPreview")
    val posterUrlPreview: String? = null,
    @SerializedName("year")
    val date: Int? = null
)

