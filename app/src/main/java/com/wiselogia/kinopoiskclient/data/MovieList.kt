package com.wiselogia.kinopoiskclient.data

import com.google.gson.annotations.SerializedName


data class MovieList(
    @SerializedName("items", alternate = ["films"])
    val results: List<Movie> = listOf(),
)
