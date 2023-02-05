package com.wiselogia.kinopoiskclient.ui.movie

import android.annotation.SuppressLint
import android.os.Bundle

import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.wiselogia.kinopoiskclient.databinding.MovieInfoBinding
import com.wiselogia.kinopoiskclient.utilities.glide
import com.wiselogia.kinopoiskclient.utilities.glideWithBlur
import io.reactivex.disposables.CompositeDisposable

class MovieActivity : AppCompatActivity() {
    private val disposable = CompositeDisposable()

    lateinit var binding: MovieInfoBinding

    private lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MovieInfoBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        movieViewModel = ViewModelProvider(
            viewModelStore,
            MovieViewModelFactory(intent.getIntExtra("id", 0))
        )[MovieViewModel::class.java]

    }

    override fun onStart() {
        super.onStart()
        setObservers()
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        disposable.add(
            movieViewModel.movieData
                .doOnError {
                    if (it.message != "HTTP 422 ") {
                        Toast.makeText(
                            this,
                            "error with code: ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .subscribe {
                    binding.yearView.text = "Год выпуска: ${it?.date}"
                    binding.promoView.glide(it?.posterUrl ?: "")
                    binding.nameView.text = it.nameRu ?: it.nameOriginal
                    binding.backgroundView.glideWithBlur(it?.posterUrl ?: "")
                    binding.countriesView.text = "Страны: " + it.countries.joinToString(", ") {it.country}
                    binding.genresView.text = "Жанры: " + it.genres.joinToString(", ") {it.genre}
                    binding.overviewView.text = it.description
                }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}