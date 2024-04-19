package com.hitachivantara.mobilecoe.android.ui.movies.subInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hitachivantara.mobilecoe.android.R
import com.hitachivantara.mobilecoe.android.data.model.Cast
import com.hitachivantara.mobilecoe.android.databinding.ItemCastBinding

class CastsAdapter: PagingDataAdapter<Cast, ViewHolder>(CAST_COMPARATOR) {

    companion object {
        private val CAST_COMPARATOR = object : DiffUtil.ItemCallback<Cast>() {
            override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
                return oldItem.cast_id == newItem.cast_id &&  oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Cast, newItem: Cast) =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as CastViewHolder).bindData(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return CastViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cast, parent,false)
        )
    }

    inner class CastViewHolder(itemView: View): ViewHolder(itemView){
        private val binding = ItemCastBinding.bind(itemView)

        fun bindData(data: Cast?) {
            binding.cast = data
            binding.executePendingBindings()
        }
    }
}