package com.wiselogia.kinopoiskclient.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.wiselogia.kinopoiskclient.app.App
import com.wiselogia.kinopoiskclient.ui.main.ShowableModel
import io.reactivex.android.schedulers.AndroidSchedulers


class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val dbService by lazy {
        (application as App).kinopoiskDBService
    }
    val items = dbService.getFavMovies().map { list ->
        list.map {
            ShowableModel(title = it.title,
                image = it.posterUrlPreview.toString(),
                id = it.kinopoiskId,
                year = it.date,
                fav = true)
        }
    }.observeOn(AndroidSchedulers.mainThread())

    fun dislike(showableModel: ShowableModel) {
        dbService.deleteFavMovie(showableModel.id).subscribe()
    }
}