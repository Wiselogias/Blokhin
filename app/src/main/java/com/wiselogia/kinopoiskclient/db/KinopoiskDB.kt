package com.wiselogia.kinopoiskclient.db


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
     version = 1, entities = [MovieRoomEntity::class]
)
abstract class KinopoiskDB : RoomDatabase() {
    abstract fun movieDao() : MovieDao
}