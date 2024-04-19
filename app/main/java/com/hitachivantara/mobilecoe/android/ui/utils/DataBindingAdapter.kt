package com.hitachivantara.mobilecoe.android.ui.utils

import android.content.res.Resources
import android.os.Build
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.hitachivantara.mobilecoe.android.R
import com.hitachivantara.mobilecoe.android.data.model.Genre
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"

@BindingAdapter("img_poster")
fun loadImage(appCompatImageView: AppCompatImageView, path: String?) {
    Glide.with(appCompatImageView.context)
        .load(IMAGE_URL + path)
        .placeholder(R.drawable.loading_animation)
        .error(R.drawable.ic_broken_image)
        .override(Resources.getSystem().displayMetrics.widthPixels)
        .fallback(R.drawable.default_avatar)
        .into(appCompatImageView)
}

@BindingAdapter("img_avatar")
fun loadAvatar(appCompatImageView: AppCompatImageView, path: String?) {
    Glide.with(appCompatImageView.context)
        .load(IMAGE_URL + path)
        .circleCrop()
        .placeholder(R.drawable.loading_animation)
        .error(R.drawable.ic_broken_image)
        .into(appCompatImageView)
}

@BindingAdapter("popular_number")
fun loadNumberImage(appCompatImageView: AppCompatImageView, index: Int){
    val image = when(index){
        1 -> R.drawable.popular_1
        2 -> R.drawable.popular_2
        3 -> R.drawable.popular_3
        4 -> R.drawable.popular_4
        else -> R.drawable.popular_5
    }
    Glide.with(appCompatImageView.context)
        .load(image)
        .into(appCompatImageView)
}

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("modified_date")
fun bindModifiedText(appCompatTextView: AppCompatTextView, dateString: String?){
    Log.d("date", dateString.toString())
    if(dateString.isNullOrEmpty()){
        appCompatTextView.text = ""
        return
    }

    try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
        val date = LocalDate.parse(dateString, formatter)
        appCompatTextView.text = date.year.toString()

    } catch (e: Exception) {
        appCompatTextView.text = ""
        e.printStackTrace()
    }
}

@BindingAdapter("stringify")
fun bindJoinListToString(textView: TextView, list: List<Genre>?) {
    if(list.isNullOrEmpty()){
        textView.text = textView.context.getString(R.string.no_genres)
        return
    }
    list?.let {
        val joinedString = it.joinToString(", ") { genre -> genre.name }
        textView.text = joinedString
    }
}