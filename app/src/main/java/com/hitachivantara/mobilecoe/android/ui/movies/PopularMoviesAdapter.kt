package com.hitachivantara.mobilecoe.android.ui.movies

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hitachivantara.mobilecoe.android.R
import com.hitachivantara.mobilecoe.android.data.model.Movie
import com.hitachivantara.mobilecoe.android.databinding.ItemTopMovieBinding
import com.hitachivantara.mobilecoe.android.ui.BaseAdapter

class PopularMoviesAdapter: BaseAdapter<Movie>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<Movie> {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_top_movie, parent, false)
        )
    }

    class MovieViewHolder(itemView: View) : ViewHolder<Movie>(itemView) {
        private var binding: ItemTopMovieBinding = ItemTopMovieBinding.bind(itemView)

        override fun bindData(data: Movie) {
            binding.movie = data
            binding.executePendingBindings()
        }

        override fun retrieveIndex(position: Int) {
            Log.d("position", position.toString())
            binding.index = position + 1
        }
    }
}