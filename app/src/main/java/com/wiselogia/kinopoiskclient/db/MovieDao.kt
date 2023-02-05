package com.wiselogia.kinopoiskclient.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable

@Dao
interface MovieDao {
    @Insert
    fun insert(movieRoomEntity: MovieRoomEntity) : Long
    @Query("SELECT * FROM MovieRoomEntity ORDER BY createDate")
    fun getFavMovies(): Observable<List<MovieRoomEntity>>
    @Delete
    fun delete(movieRoomEntity: MovieRoomEntity)
    @Query("SELECT * FROM MovieRoomEntity WHERE kinopoiskId = :id")
    fun getFavMovie(id: Int): MovieRoomEntity
    @Query("SELECT * FROM MovieRoomEntity ORDER BY createDate")
    fun getAllFavMovies(): List<MovieRoomEntity>
}