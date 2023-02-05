package com.wiselogia.kinopoiskclient.data

import com.google.gson.annotations.SerializedName

data class MovieFull(
    @SerializedName("nameRu")
    val nameRu: String? = null,
    @SerializedName("nameOriginal")
    val nameOriginal: String? = null,
    @SerializedName("posterUrl")
    val posterUrl: String? = null,
    @SerializedName("year")
    val date: Int? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("countries")
    val countries: List<Country> = listOf(),
    @SerializedName("genres")
    val genres: List<Genre> = listOf()
)
