package com.wiselogia.kinopoiskclient.ui.favorite

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wiselogia.kinopoiskclient.databinding.FavoriteFragmentBinding
import com.wiselogia.kinopoiskclient.ui.movie.MovieActivity
import com.wiselogia.kinopoiskclient.utilities.ShowableAdapter

class FavoriteFragment: Fragment() {
    private lateinit var binding: FavoriteFragmentBinding
    private val viewModel by viewModels<FavoriteViewModel>()
    private val showableAdapter by lazy {
        ShowableAdapter({
            viewModel.dislike(it)
        }) {
            startActivity(Intent(context, MovieActivity::class.java).apply {
                putExtra("id", it.id)
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FavoriteFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.moviesView.apply {
            adapter = showableAdapter
        }
        viewModel.items.subscribe{
            showableAdapter.showables = it
        }
    }
}