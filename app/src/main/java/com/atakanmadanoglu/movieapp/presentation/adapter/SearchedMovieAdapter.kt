package com.atakanmadanoglu.movieapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.atakanmadanoglu.movieapp.databinding.MovieItemBinding
import com.atakanmadanoglu.movieapp.presentation.model.MovieUI

class SearchedMovieAdapter(
    private val movieItemClickListener: (id: Int) -> Unit
): PagingDataAdapter<MovieUI, SearchedMovieAdapter.MovieViewHolder>(MovieItemDiffUtil()) {
    class MovieViewHolder(
        private val movieItemBinding: MovieItemBinding
    ): RecyclerView.ViewHolder(movieItemBinding.root) {
        companion object {
            fun inflateFrom(parent: ViewGroup): MovieViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieItemBinding.inflate(layoutInflater, parent, false)
                return MovieViewHolder(binding)
            }
        }

        fun bind(
            movieUI: MovieUI,
            movieItemClickListener: (id: Int) -> Unit
        ) {
            movieItemBinding.movieTitle.text = movieUI.title
            movieItemBinding.movieDesc.text = movieUI.overview
            movieItemBinding.movieItemCard.setOnClickListener {
                movieItemClickListener(movieUI.id)
            }
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, movieItemClickListener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.inflateFrom(parent)
    }
}

class MovieItemDiffUtil: DiffUtil.ItemCallback<MovieUI>() {
    override fun areItemsTheSame(oldItem: MovieUI, newItem: MovieUI): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieUI, newItem: MovieUI): Boolean {
        return oldItem == newItem
    }

}