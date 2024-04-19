package com.hitachivantara.mobilecoe.android.ui.movies

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.hitachivantara.mobilecoe.android.DefaultDispatcherProvider
import com.hitachivantara.mobilecoe.android.data.MovieRepository

class MoviesViewModelFactory(
    owner:SavedStateRegistryOwner,
    private val repository: MovieRepository
) : AbstractSavedStateViewModelFactory(owner, null) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            return MoviesViewModel(
                movieRepository = repository, DefaultDispatcherProvider()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}