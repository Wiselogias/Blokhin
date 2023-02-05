package com.wiselogia.kinopoiskclient.app

import android.app.Application
import com.wiselogia.kinopoiskclient.db.KinopoiskDBService

class App: Application() {
    val kinopoiskDBService by lazy {
        KinopoiskDBService(this)
    }
}