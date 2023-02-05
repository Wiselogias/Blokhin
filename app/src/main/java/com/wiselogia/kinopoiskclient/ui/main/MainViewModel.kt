package com.wiselogia.kinopoiskclient.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.wiselogia.kinopoiskclient.app.App
import com.wiselogia.kinopoiskclient.network.KinopoiskService
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var page = 1
    val listSubject: PublishSubject<List<ShowableModel>> = PublishSubject.create()
    val errorSubject: BehaviorSubject<Throwable> = BehaviorSubject.create()
    val clearSubject: BehaviorSubject<Unit> = BehaviorSubject.create()
    val dbService by lazy {
        (application as App).kinopoiskDBService
    }

    private val disposable = CompositeDisposable()

    private val networkListSubscriber = object : Observer<List<ShowableModel>> {
        override fun onSubscribe(d: Disposable) {
            disposable.add(d)
        }

        override fun onNext(value: List<ShowableModel>) {
            listSubject.onNext(value)
        }

        override fun onError(e: Throwable) {
            e.printStackTrace()
            errorSubject.onNext(e)
        }

        override fun onComplete() {}

    }

    fun viewChanges(text: Observable<String>, onPage: Subject<Unit>) {
        disposable.add(
            text.debounce(200, TimeUnit.MILLISECONDS).subscribe {
                page = 1
                Log.println(Log.INFO, "tag", "query:$it page:$page")
                clearSubject.onNext(Unit)
                getShowableInfo(page, it)
                    .subscribe(networkListSubscriber)
            }
        )
        disposable.add(
            onPage.flatMap { text }.subscribe { query ->
                page++
                Log.println(Log.INFO, "tag", "query:$query page:$page")
                getShowableInfo(page, query)
                    .subscribe(networkListSubscriber)
            }
        )
    }

    private fun getShowableInfo(page: Int, query: String): Observable<List<ShowableModel>> =
        KinopoiskService.getListDataObservable(page, query).map { list ->
            list.map {
                ShowableModel(
                    title = (it.nameRu ?: it.nameOriginal),
                    it.posterUrlPreview ?: "",
                    it.kinopoiskId,
                    it.date
                )
            }
        }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
    fun toggleFavorite(model: ShowableModel) {
        if(!model.fav)
            dbService.addFavMovie(model).subscribe()
        else
            dbService.deleteFavMovie(model.id).subscribe()
    }
}