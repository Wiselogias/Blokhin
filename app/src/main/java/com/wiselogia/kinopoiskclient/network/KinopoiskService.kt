package com.wiselogia.kinopoiskclient.network

import com.wiselogia.kinopoiskclient.data.Movie
import com.wiselogia.kinopoiskclient.data.MovieFull
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

object KinopoiskService {
    const val APIKEY = "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b"
    val scheduler = Schedulers.from(Executors.newFixedThreadPool(10))
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://kinopoiskapiunofficial.tech")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val kinopoiskApi = retrofit.create(KinopoiskApi::class.java)



    fun getDataObservable(id: Int) : Observable<MovieFull> {
        return kinopoiskApi
            .getFullDataObservable(id)
            .subscribeOn(scheduler)
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getListDataObservable(
        page: Int,
        query: String
    ): Observable<List<Movie>> {
        return if(query.isEmpty())
            kinopoiskApi.getTopListObservable(page).map {
                it.results
            }.subscribeOn(scheduler)
                .observeOn(AndroidSchedulers.mainThread())
        else
            kinopoiskApi.getListDataObservable(keyword = query, page = page).map {
                it.results
            }.subscribeOn(scheduler)
                .observeOn(AndroidSchedulers.mainThread())
    }

}