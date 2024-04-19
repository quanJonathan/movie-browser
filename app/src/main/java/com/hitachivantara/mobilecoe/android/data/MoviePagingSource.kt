package com.hitachivantara.mobilecoe.android.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hitachivantara.mobilecoe.android.data.MovieRepository.Companion.NETWORK_PAGE_SIZE
import com.hitachivantara.mobilecoe.android.data.api.ApiService
import com.hitachivantara.mobilecoe.android.data.model.Movie
import com.hitachivantara.mobilecoe.android.ui.movies.MovieSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MoviePagingSource(
    private val movieApiService: ApiService,
    private val dataSource: MovieSource?
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        val page = params.key ?: 1
        return try {
            val response = withContext(Dispatchers.IO) {
                when (dataSource) {
                    MovieSource.NOW_PLAYING -> movieApiService.getNowPlayingMovies(page = page)
                        .execute()

                    MovieSource.POPULAR -> movieApiService.getPopularMovies(page = page).execute()
                    MovieSource.TOP_RATED -> movieApiService.getTopRatedMovies(page = page)
                        .execute()

                    else -> movieApiService.getUpComingMovies(page = page).execute()
                }
            }

            if (response.isSuccessful) {
                val movies = response.body()!!.results
                val nextKey = if (movies.isEmpty()) {
                    null
                } else {
                    page + (params.loadSize / NETWORK_PAGE_SIZE)
                }
                LoadResult.Page(
                    data = movies,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = nextKey
                )
            } else {
                LoadResult.Error(Exception("Error loading data"))
            }
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}