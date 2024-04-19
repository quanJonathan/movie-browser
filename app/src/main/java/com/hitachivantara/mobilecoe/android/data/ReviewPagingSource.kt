package com.hitachivantara.mobilecoe.android.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hitachivantara.mobilecoe.android.data.api.ApiService
import com.hitachivantara.mobilecoe.android.data.model.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ReviewPagingSource(private val apiService: ApiService, val id: Int):PagingSource<Int, Review>() {
    override fun getRefreshKey(state: PagingState<Int, Review>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        val page = params.key ?: 1
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.getMovieReviews(movie_id = id).execute()
            }

            if (response.isSuccessful) {
                val reviews = response.body()!!.results
                val nextKey = if (reviews.isEmpty()) {
                    null
                } else {
                    page + (params.loadSize / MovieRepository.REVIEW_PAGE_SIZE)
                }
                LoadResult.Page(
                    data = reviews,
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
