package com.wiselogia.kinopoiskclient.network

import com.wiselogia.kinopoiskclient.data.MovieFull
import com.wiselogia.kinopoiskclient.data.MovieList
import com.wiselogia.kinopoiskclient.network.KinopoiskService.APIKEY
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface KinopoiskApi {
    @Headers("X-API-KEY: $APIKEY")
    @GET("/api/v2.2/films/top")
    fun getTopListObservable(
        @Query("page") page: Int = 1,
        @Query("type") type: String = "TOP_100_POPULAR_FILMS"
    ) : Observable<MovieList>

    @Headers("X-API-KEY: $APIKEY")
    @GET("/api/v2.2/films")
    fun getListDataObservable(
        @Query("order") order: String = "RATING",
        @Query("type") type: String = "ALL",
        @Query("ratingFrom") ratingFrom: Int = 0,
        @Query("ratingTo") ratingTo: Int = 10,
        @Query("yearFrom") yearFrom: Int = 1000,
        @Query("yearTo") yearTo: Int = 3000,
        @Query("keyword") keyword: String,
        @Query("page") page: Int = 1
    ) : Observable<MovieList>

    @Headers("X-API-KEY: $APIKEY")
    @GET("/api/v2.2/films/{id}")
    fun getFullDataObservable(
        @Path(value = "id", encoded = false) id: Int,
    ) : Observable<MovieFull>
}