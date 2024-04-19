package com.hitachivantara.mobilecoe.android.ui.movies.subInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hitachivantara.mobilecoe.android.R
import com.hitachivantara.mobilecoe.android.data.model.Review
import com.hitachivantara.mobilecoe.android.databinding.ItemReviewBinding


class ReviewsAdapter : PagingDataAdapter<Review, ViewHolder>(REVIEW_COMPARATOR) {

   companion object {
       private val REVIEW_COMPARATOR = object : DiffUtil.ItemCallback<Review>() {
           override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
               return oldItem.author_details.rating == newItem.author_details.rating &&  oldItem.author_details.username == newItem.author_details.username
           }

           override fun areContentsTheSame(oldItem: Review, newItem: Review) =
               oldItem == newItem
       }
   }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ReviewViewHolder).bindData(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent,false)
        )
    }

    inner class ReviewViewHolder(itemView: View): ViewHolder(itemView){
        private val binding = ItemReviewBinding.bind(itemView)

        fun bindData(data: Review?) {
            binding.review = data
            binding.executePendingBindings()
        }
    }
}