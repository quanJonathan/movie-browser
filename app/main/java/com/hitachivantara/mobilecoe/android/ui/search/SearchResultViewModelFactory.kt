package com.hitachivantara.mobilecoe.android.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hitachivantara.mobilecoe.android.DefaultDispatcherProvider
import com.hitachivantara.mobilecoe.android.data.MovieRepository
import com.hitachivantara.mobilecoe.android.data.SearchRepository
import com.hitachivantara.mobilecoe.android.data.preferences.PreferencesStore
import com.hitachivantara.mobilecoe.android.ui.movies.MoviesViewModel

class SearchResultViewModelFactory (
    private val preferencesStore: PreferencesStore
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchResultViewModel::class.java)) {
            return SearchResultViewModel(
                searchRepository = SearchRepository(preferencesStore), DefaultDispatcherProvider()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}