package com.hitachivantara.mobilecoe.android.ui.movies

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hitachivantara.mobilecoe.android.DispatcherProvider
import com.hitachivantara.mobilecoe.android.data.MovieRepository
import com.hitachivantara.mobilecoe.android.data.api.ApiEmptyResponse
import com.hitachivantara.mobilecoe.android.data.api.ApiErrorResponse
import com.hitachivantara.mobilecoe.android.data.api.ApiSuccessResponse
import com.hitachivantara.mobilecoe.android.data.model.Cast
import com.hitachivantara.mobilecoe.android.data.model.Movie
import com.hitachivantara.mobilecoe.android.data.model.Review
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

enum class MovieSource{
    NOW_PLAYING, POPULAR, TOP_RATED, UPCOMING
}

class MoviesViewModel(
    private val movieRepository: MovieRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val _moviesFlow = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val moviesFlow: StateFlow<PagingData<Movie>> get() = _moviesFlow

    private val _currentMovie = MutableLiveData<Movie>()
    val currentMovie: LiveData<Movie> get() = _currentMovie

    private val _reviewsFlow = MutableStateFlow<PagingData<Review>>(PagingData.empty())
    val reviewsFlow: StateFlow<PagingData<Review>> get() = _reviewsFlow

    private val _castsFlow = MutableStateFlow<PagingData<Cast>>(PagingData.empty())
    val castsFlow: StateFlow<PagingData<Cast>> get() = _castsFlow

    private val _dataSource = MutableLiveData(MovieSource.NOW_PLAYING)
    val dataSource: LiveData<MovieSource> get() = _dataSource

    private val _allWatchList: MutableLiveData<List<Movie>> = MutableLiveData(emptyList())
    val allWatchList: LiveData<List<Movie>> get() = _allWatchList


    init {
         viewModelScope.launch(dispatcherProvider.io) {
           getAllWatchList().collectLatest {
                _allWatchList.postValue(it)
            }
        }
    }

    private val _popularMovies = MutableLiveData<List<Movie>>()
    val popularMovies get() = _popularMovies

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    val isLoading = ObservableBoolean()

    fun fetchMovies() {
        isLoading?.set(true)
        viewModelScope.launch(dispatcherProvider.io) {
            movieRepository.getMovies(dataSource.value)
                .cachedIn(viewModelScope).collectLatest { pagingData ->
                    _moviesFlow.value = pagingData
                    isLoading?.set(false)
                }
        }
    }

    fun getTopPopularMovies() {
        viewModelScope.launch(dispatcherProvider.io) {
            movieRepository.getPopularMovies().collectLatest {
                when (it) {
                    is ApiSuccessResponse -> {
                        _popularMovies.postValue(it?.body?.results?.sortedByDescending { it?.vote_average }?.subList(0, 5))
                    }

                    is ApiEmptyResponse -> {
                        _error.postValue("No data")
                    }

                    is ApiErrorResponse -> {
                        _error.postValue(it.errorMessage)
                    }
                }
            }
        }
    }

    fun changeSource(source: MovieSource) {
        _dataSource.value = source
        fetchMovies()
    }

    fun getMovieReview(){
        isLoading?.set(true)
        viewModelScope.launch(dispatcherProvider.io) {
            movieRepository.getMovieReviews(currentMovie.value!!.id)
                .cachedIn(viewModelScope).collectLatest{ pagingData ->
               _reviewsFlow.value = pagingData
                isLoading?.set(false)
            }
        }
    }

    fun getMovieCast(){
        isLoading?.set(true)
        viewModelScope.launch(dispatcherProvider.io) {
            movieRepository.getMovieCasts(currentMovie.value!!.id)
                .cachedIn(viewModelScope).collectLatest{ pagingData ->
                    _castsFlow.value = pagingData
                    isLoading?.set(false)
                }
        }
    }

    fun getMovie(id: Int) {
        viewModelScope.launch(dispatcherProvider.io) {
            movieRepository.getMovieById(id).collect{
                when (it) {
                    is ApiSuccessResponse -> {
                        _currentMovie.postValue(it.body!!)
                    }

                    is ApiEmptyResponse -> {
                        _error.postValue("No data")
                    }

                    is ApiErrorResponse -> {
                        _error.postValue(it.errorMessage)
                    }
                }
            }
        }
    }

    fun actionOnWatchList(movie: Movie): Int {

        //save 0, delete 1
        var result = 0
        viewModelScope.launch {
            if(allWatchList.value?.contains(movie) == true){
                result = 1
                deleteFromWatchList(movie)
            }else{
                saveToWatchList(movie)
            }
        }

        return result
    }


    suspend fun saveToWatchList(movie: Movie){
        movieRepository.saveToWatchList(movie)
    }

    suspend fun deleteFromWatchList(movie: Movie){
        movieRepository.deleteFromWatchList(movie)
    }


    fun getAllWatchList(): Flow<List<Movie>> {
        return movieRepository.getAllWatchListMovies()
    }

    fun hasMovieInWatchList(movie: Movie): Boolean {
        var isInWatchList = false
        viewModelScope.launch {
            if(allWatchList.value?.contains(movie) == true) {
                isInWatchList = true
            }
        }
        return isInWatchList
    }

    fun rateMovie(movie: Movie?, value: Float) {

    }
}