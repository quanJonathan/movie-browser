package com.hitachivantara.mobilecoe.android.ui.watchlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hitachivantara.mobilecoe.android.R
import com.hitachivantara.mobilecoe.android.data.model.Movie
import com.hitachivantara.mobilecoe.android.databinding.ItemMovieLargeBinding
import com.hitachivantara.mobilecoe.android.databinding.ItemTopMovieBinding
import com.hitachivantara.mobilecoe.android.ui.BaseAdapter
import com.hitachivantara.mobilecoe.android.ui.search.SearchResultAdapter
import com.hitachivantara.mobilecoe.android.ui.utils.ItemClickListener

class WatchListAdapter: BaseAdapter<Movie>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<Movie> {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie_large, parent, false)
        )
    }

    class MovieViewHolder(itemView: View) : ViewHolder<Movie>(itemView) {
        private var binding: ItemMovieLargeBinding = ItemMovieLargeBinding.bind(itemView)

        override fun bindData(data: Movie) {
            Log.d("movie in watch list", data.toString())
            binding.movie = data
            binding.executePendingBindings()
        }

        override fun retrieveIndex(position: Int) {
        }
    }

}