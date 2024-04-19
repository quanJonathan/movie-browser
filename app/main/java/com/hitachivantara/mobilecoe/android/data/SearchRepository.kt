package com.hitachivantara.mobilecoe.android.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hitachivantara.mobilecoe.android.data.api.ApiService
import com.hitachivantara.mobilecoe.android.data.api.NetworkModule
import com.hitachivantara.mobilecoe.android.data.model.Movie
import com.hitachivantara.mobilecoe.android.data.preferences.PreferencesStore
import kotlinx.coroutines.flow.Flow

class SearchRepository(private val preferencesStore: PreferencesStore) {
    private val networkModule by lazy { NetworkModule(preferencesStore) }
    private val apiService by lazy { networkModule.createService(ApiService::class.java) }

    fun searchMovies(searchText: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = SEARCH_RESULT_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {SearchMoviePagingSource(apiService, searchText)}
        ).flow
    }

    companion object {
        const val SEARCH_RESULT_SIZE = 30
    }
}