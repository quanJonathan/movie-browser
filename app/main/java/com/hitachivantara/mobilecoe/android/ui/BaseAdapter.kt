package com.hitachivantara.mobilecoe.android.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hitachivantara.mobilecoe.android.ui.utils.ItemClickListener


abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseAdapter.ViewHolder<T>>() {
    private var list = emptyList<T>()

    private var itemClickListener: ItemClickListener<T>? = null

    override fun getItemCount() = list.size

    open fun setClickListener(clickListener: ItemClickListener<T>) {
        itemClickListener = clickListener
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {

        getItem(position)?.let {item ->
            holder.bindData(item)
            holder.retrieveIndex(position)
            holder.itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    fun getIndex(item: T): Int{
        return list.indexOf(item)
    }

    fun getItem(position: Int): T? {
        if (position < 0 || position >= list.size) return null
        return list[position]
    }

    fun updateData(list: List<T>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun onItemClick(item: T){
        itemClickListener?.onClick(item)
    }


    abstract class ViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bindData(data: T)
        abstract fun retrieveIndex(position: Int)
    }
}