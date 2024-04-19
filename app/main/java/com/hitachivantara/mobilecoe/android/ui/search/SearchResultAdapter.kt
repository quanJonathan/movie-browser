package com.hitachivantara.mobilecoe.android.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hitachivantara.mobilecoe.android.R
import com.hitachivantara.mobilecoe.android.data.model.Movie
import com.hitachivantara.mobilecoe.android.databinding.ItemMovieLargeBinding
import com.hitachivantara.mobilecoe.android.ui.utils.ItemClickListener

class SearchResultAdapter :PagingDataAdapter<Movie, ViewHolder>(MOVIE_COMPARATOR) {

    var itemClickListener: ItemClickListener<Movie>? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {movie ->
            (holder as MovieViewHolder).bindData(movie)
            holder.itemView.setOnClickListener {
                itemClickListener?.onClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie_large, parent, false)
        )
    }


    inner class MovieViewHolder(itemView: View) : ViewHolder(itemView) {
        private var binding: ItemMovieLargeBinding = ItemMovieLargeBinding.bind(itemView)

        fun bindData(data: Movie?) {
            binding.movie = data
            binding.executePendingBindings()
        }
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>(){
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id && oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem
        }
    }
}