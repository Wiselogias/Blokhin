package com.wiselogia.kinopoiskclient.ui.movie

import androidx.lifecycle.ViewModel
import com.wiselogia.kinopoiskclient.data.MovieFull
import com.wiselogia.kinopoiskclient.network.KinopoiskService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class MovieViewModel(val id: Int) : ViewModel() {
    val movieData: Observable<MovieFull> = KinopoiskService
        .getDataObservable(id)
        .observeOn(AndroidSchedulers.mainThread())
}