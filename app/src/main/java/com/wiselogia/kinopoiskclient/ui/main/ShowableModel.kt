package com.wiselogia.kinopoiskclient.ui.main

data class ShowableModel(
    val title: String? = "",
    val image: String = "",
    val id: Int = 0,
    val year: Int? = null,
    var fav: Boolean = false
)

