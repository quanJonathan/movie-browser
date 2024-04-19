package com.hitachivantara.mobilecoe.android.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.hitachivantara.mobilecoe.android.data.database.GenreListConverter

@Entity("movies")
@TypeConverters(GenreListConverter::class)
data class Movie(
    val adult: Boolean,
    val backdrop_path: String,
    val genres: List<Genre>,
    @PrimaryKey
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

data class Genre (
    val id: Int,
    val name: String
)