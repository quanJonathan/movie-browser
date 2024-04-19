package com.hitachivantara.mobilecoe.android.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.hitachivantara.mobilecoe.android.data.api.ApiErrorResponse
import com.hitachivantara.mobilecoe.android.data.api.ApiResponse
import com.hitachivantara.mobilecoe.android.data.api.ApiService
import com.hitachivantara.mobilecoe.android.data.api.enqueue
import com.hitachivantara.mobilecoe.android.data.database.AppDatabase
import com.hitachivantara.mobilecoe.android.data.model.Cast
import com.hitachivantara.mobilecoe.android.data.model.Movie
import com.hitachivantara.mobilecoe.android.data.model.Review
import com.hitachivantara.mobilecoe.android.data.model.response.ResponseList
import com.hitachivantara.mobilecoe.android.ui.movies.MovieSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MovieRepository(private val apiService: ApiService, private val database: AppDatabase) {

    fun getMovies(dataSource: MovieSource?): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {MoviePagingSource(apiService, dataSource)}
        ).flow
    }

    fun getMovieReviews(id: Int): Flow<PagingData<Review>> {
        return Pager(
            config = PagingConfig(pageSize = REVIEW_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {ReviewPagingSource(apiService, id)}
        ).flow
    }

    fun getMovieCasts(id: Int): Flow<PagingData<Cast>>{
        return Pager(
            config = PagingConfig(pageSize = CAST_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {CastPagingSource(apiService, id)}
        ).flow
    }

    fun getPopularMovies() : Flow<ApiResponse<ResponseList<Movie>>> = callbackFlow {
        delay(2000)
        apiService.getPopularMovies().enqueue(this::trySend)
        awaitClose { trySend(ApiErrorResponse(-1, "timeout")) }
    }

    fun getMovieById(id: Int): Flow<ApiResponse<Movie>> = callbackFlow {
        delay(2000)
        apiService.getMovieById(id).enqueue(this::trySend)
        awaitClose { trySend(ApiErrorResponse(-1, "timeout")) }
    }

    suspend fun saveToWatchList(saveMovie: Movie) {
        database.withTransaction {
            database.watchListDao().insertMovie(movie = saveMovie)
        }
    }

    fun getAllWatchListMovies(): Flow<List<Movie>> {
        return database.watchListDao().getAll()
    }

    suspend fun deleteFromWatchList(movie: Movie) {
        database.withTransaction {
            database.watchListDao().deleteWithId(movie.id)
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 30
        const val REVIEW_PAGE_SIZE = 10
        const val CAST_PAGE_SIZE = 20
    }
}