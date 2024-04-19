package com.hitachivantara.mobilecoe.android.ui.search

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hitachivantara.mobilecoe.android.DispatcherProvider
import com.hitachivantara.mobilecoe.android.data.MovieRepository
import com.hitachivantara.mobilecoe.android.data.SearchRepository
import com.hitachivantara.mobilecoe.android.data.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchResultViewModel(
    private val searchRepository: SearchRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _searchResultFlow = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val searchResultFlow: StateFlow<PagingData<Movie>> get() = _searchResultFlow

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    val isLoading = ObservableBoolean()

    fun searchMovies(searchText: String) {
        isLoading.set(true)
        viewModelScope.launch(dispatcherProvider.io) {
            searchRepository.searchMovies(searchText).cachedIn(viewModelScope).collectLatest {
                _searchResultFlow.value = it
                isLoading.set(false)
            }
        }
    }

}