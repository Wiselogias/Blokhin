package com.wiselogia.kinopoiskclient.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wiselogia.kinopoiskclient.app.App
import com.wiselogia.kinopoiskclient.databinding.MainFragmentBinding
import com.wiselogia.kinopoiskclient.ui.movie.MovieActivity
import com.wiselogia.kinopoiskclient.utilities.LoadMoreListener
import com.wiselogia.kinopoiskclient.utilities.ShowableAdapter
import com.wiselogia.kinopoiskclient.utilities.onText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class MainFragment: Fragment() {
    private val disposable = CompositeDisposable()

    private lateinit var binding: MainFragmentBinding
    private val scrollSubject = PublishSubject.create<Unit>()
    private var onLoadMore = LoadMoreListener {
        scrollSubject.onNext(Unit)
    }
    val handler = Handler(Looper.getMainLooper())
    val backgroundHandler = Handler(HandlerThread("rrrr").run{start(); looper})
    val dbService by lazy {
        (requireActivity().application as App).kinopoiskDBService
    }
    private val viewModel by viewModels<MainViewModel>()
    private val showableAdapter by lazy {
        ShowableAdapter({
            viewModel.toggleFavorite(it)
        }) {
            startActivity(Intent(context, MovieActivity::class.java).apply {
                putExtra("id", it.id)
            })
        }
    }
    private val callback: (Int, Boolean) -> Unit = { id, fav ->
        showableAdapter.setFavorite(fav, id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.moviesView.apply {
            adapter = showableAdapter
            setOnScrollChangeListener(onLoadMore)
        }

        setListeners()
        setObservers()
    }

    private fun setListeners() {
        viewModel.viewChanges(
            binding.searchView.onText(),
            scrollSubject
        )
    }

    private fun setObservers() {
        binding.shimmer.isVisible = true
        disposable.add(
            viewModel.errorSubject.observeOn(AndroidSchedulers.mainThread()).subscribe {
                Toast.makeText(
                    context,
                    "error with code: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        disposable.add(
            viewModel.listSubject.doOnEach {
                backgroundHandler.post {
                    val list = dbService.getAllFavMovies()
                    handler.post {
                        list.forEach {
                            showableAdapter.setFavorite(true, it.kinopoiskId)
                        }
                    }
                }
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                showableAdapter.addItems(it)
                onLoadMore.isLoading = false
                binding.shimmer.isVisible = false
            }
        )
        disposable.add(
            viewModel.clearSubject.observeOn(AndroidSchedulers.mainThread()).subscribe {
                showableAdapter.clear()
                binding.shimmer.isVisible = true
            }
        )
        viewModel.dbService.registerListener(callback)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
        viewModel.dbService.unregisterListener(callback)
    }
}