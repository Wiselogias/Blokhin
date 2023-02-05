package com.wiselogia.kinopoiskclient.db

import android.content.Context
import android.os.Looper
import androidx.room.Room
import com.wiselogia.kinopoiskclient.data.Movie
import com.wiselogia.kinopoiskclient.ui.main.ShowableModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.logging.Handler

typealias Listener = (Int, Boolean) -> Unit
class KinopoiskDBService(context: Context) {
    private val db = Room.databaseBuilder(context, KinopoiskDB::class.java, "DIO").build()
    private val dao = db.movieDao()
    private val listeners = mutableSetOf<Listener>()
    private val handler = android.os.Handler(Looper.getMainLooper())
    fun getFavMovies() = dao.getFavMovies().subscribeOn(Schedulers.io())
    fun getAllFavMovies() = dao.getAllFavMovies()
    fun addFavMovie(movie: ShowableModel) = Completable.create {
        val movieRoomEntity = MovieRoomEntity(
            kinopoiskId = movie.id,
            title = movie.title,
            posterUrlPreview = movie.image,
            date = movie.year
        )
        dao.insert(movieRoomEntity)
        listeners.forEach {
            handler.post {
                it(movie.id, true)
            }
        }
        it.onComplete()
    }.subscribeOn(Schedulers.io())

    fun registerListener(listener: Listener) = listeners.add(listener)
    fun unregisterListener(listener: Listener) = listeners.remove(listener)

    fun deleteFavMovie(id: Int) = Completable.create {
        val movieRoomEntity = dao.getFavMovie(id)
        dao.delete(movieRoomEntity)
        listeners.forEach {
            handler.post {
                it(id, false)
            }
        }
        it.onComplete()
    }.subscribeOn(Schedulers.io())
}