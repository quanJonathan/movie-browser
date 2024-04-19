package com.hitachivantara.mobilecoe.android.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hitachivantara.mobilecoe.android.data.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchListMovieDao {
    @Query("SELECT * FROM movies")
    fun getAll(): Flow<List<Movie>>

    @Query("DELETE FROM movies where id=:movieId")
    suspend fun deleteWithId(movieId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)
}