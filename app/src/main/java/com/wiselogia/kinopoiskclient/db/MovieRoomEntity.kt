package com.wiselogia.kinopoiskclient.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class MovieRoomEntity (
    @PrimaryKey
    val kinopoiskId: Int,
    @ColumnInfo
    val title: String? = null,
    @ColumnInfo
    val posterUrlPreview: String? = null,
    @ColumnInfo
    val date: Int? = null,
    @ColumnInfo
    val createDate: Long = Date().time
)