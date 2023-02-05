package com.wiselogia.kinopoiskclient.utilities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wiselogia.kinopoiskclient.R
import com.wiselogia.kinopoiskclient.databinding.MovieCardBinding
import com.wiselogia.kinopoiskclient.ui.main.ShowableModel


class ShowableAdapter(
    private val onLongClick: (ShowableModel) -> Unit,
    private val onClick: (ShowableModel) -> Unit
) :
    RecyclerView.Adapter<ShowableAdapter.ShowableHolder>() {
    var showables = listOf<ShowableModel>()
        set(value) {
            val res = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize() = field.size

                override fun getNewListSize() = value.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    field[oldItemPosition] == value[newItemPosition]

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ) = field[oldItemPosition] == value[newItemPosition]

            })
            field = value
            res.dispatchUpdatesTo(this)
        }

    inner class ShowableHolder(private val binding: MovieCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageView = binding.moviePoster
        private val titleView = binding.movieTitle

        fun bind(
            showable: ShowableModel,
            onClick: (ShowableModel) -> Unit,
            onLongClick: (ShowableModel) -> Unit
        ) {
            imageView.glide(showable.image)
            titleView.text = showable.title
            binding.yearTitle.text = showable.year.toString()
            binding.root.setOnClickListener {
                onClick(showable)
            }
            binding.favButton.setOnClickListener {
                onLongClick(showable)
            }
            binding.favButton.icon = AppCompatResources.getDrawable(
                binding.root.context,
                if (showable.fav) R.drawable.ic_round_star_24
                else R.drawable.ic_round_star_border_24
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowableHolder {
        return ShowableHolder(
            MovieCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShowableHolder, position: Int) =
        holder.bind(showables[position], onClick, onLongClick)

    override fun getItemCount(): Int = showables.size

    fun addItems(newItems: List<ShowableModel>) {
        showables = showables.toMutableList().apply {
            addAll(newItems)
        }
    }

    fun clear() {
        showables = listOf()
    }

    fun setFavorite(fav: Boolean, id: Int) {
        val index = showables.indexOfFirst { it.id == id }
        if (index < 0) return
        showables[index].fav = fav
        notifyItemChanged(index)
    }
}