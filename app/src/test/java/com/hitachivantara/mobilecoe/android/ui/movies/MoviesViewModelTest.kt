package com.hitachivantara.mobilecoe.android.ui.movies

import android.content.Context
import android.text.method.Touch.scrollTo
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.hitachivantara.mobilecoe.android.DispatcherProvider
import com.hitachivantara.mobilecoe.android.Injection
import com.hitachivantara.mobilecoe.android.TestDispatcherProvider
import com.hitachivantara.mobilecoe.android.data.MovieRepository
import com.hitachivantara.mobilecoe.android.data.MovieRepository.Companion.NETWORK_PAGE_SIZE
import com.hitachivantara.mobilecoe.android.data.api.ApiEmptyResponse
import com.hitachivantara.mobilecoe.android.data.api.ApiErrorResponse
import com.hitachivantara.mobilecoe.android.data.api.ApiSuccessResponse
import com.hitachivantara.mobilecoe.android.data.model.Movie
import com.hitachivantara.mobilecoe.android.data.model.response.ResponseList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations.openMocks

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MoviesViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private lateinit var testViewmodel: MoviesViewModel

    @Mock
    private lateinit var repository: MovieRepository
    private lateinit var mocks: AutoCloseable
    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        mocks = openMocks(this)
        dispatcherProvider = TestDispatcherProvider()
        testViewmodel = MoviesViewModel(repository, dispatcherProvider)
    }

    @Test
    fun test_fetchPopularMovies_shouldReturnSuccessResponse() {
        val movie = mock(Movie::class.java)
        Mockito.`when`(movie.title).thenReturn("movie title")
        val response = ApiSuccessResponse(ResponseList<Movie>().apply {
            results = listOf(movie, movie, movie, movie, movie)
        })

        runTest {
            Mockito.doReturn(flowOf(response)).`when`(repository).getPopularMovies()
            testViewmodel.getTopPopularMovies()
            Assert.assertEquals(5, testViewmodel.popularMovies.value?.size)
            Assert.assertNull(testViewmodel.error.value)
            Assert.assertEquals("movie title", testViewmodel.popularMovies.value?.get(0)?.title)
        }
    }

    @Test
    fun test_fetchPopularMovies_shouldReturnEmptyResponse() {
        val response = ApiEmptyResponse<Any>()
        runTest {
            Mockito.doReturn(flowOf(response)).`when`(repository).getPopularMovies()
            testViewmodel.getTopPopularMovies()
            Assert.assertNull(testViewmodel.popularMovies.value)
            Assert.assertEquals("No data", testViewmodel.error.value)
        }
    }

    @Test
    fun test_fetchPopularMovies_shouldReturnErrorResponse() {
        val response = ApiErrorResponse(404, "Unauthorized")
        runTest {
            Mockito.doReturn(flowOf(response)).`when`(repository).getPopularMovies()
            testViewmodel.getTopPopularMovies()
            Assert.assertNull(testViewmodel.popularMovies.value)
            Assert.assertEquals("Unauthorized", testViewmodel.error.value)
        }
    }

    @Test
    fun test_fetchMovieWithId_shouldReturnSuccessResponse() {
        val movie = mock(Movie::class.java)
        val testId = 1087671
        val emptyMovie = Movie(
            adult = false,
            backdrop_path = "",
            genres = emptyList(),
            id = testId,
            original_language = "",
            original_title = "",
            overview = "",
            popularity = 0.0,
            poster_path = "",
            release_date = "",
            title = "",
            video = false,
            vote_average = 0.0,
            vote_count = 0
        )

        Mockito.`when`(movie.id).thenReturn(testId)
        val response = ApiSuccessResponse(emptyMovie)

        runTest {
            Mockito.doReturn(flowOf(response)).`when`(repository).getMovieById(testId)
            testViewmodel.getMovie(testId)
            Assert.assertNull(testViewmodel.error.value)
            Assert.assertEquals(testId, testViewmodel.currentMovie.value?.id)
        }
    }

    @Test
    fun test_fetchMovieWithId_shouldReturnEmptyResponse() {
        val testId = 1000000
        val response = ApiEmptyResponse<Any>()
        runTest {
            Mockito.doReturn(flowOf(response)).`when`(repository).getMovieById(testId)
            testViewmodel.getMovie(testId)
            Assert.assertNull(testViewmodel.currentMovie.value)
            Assert.assertEquals("No data", testViewmodel.error.value)
        }
    }

    @Test
    fun test_fetchMovieWithId_shouldReturnErrorResponse() {
        val testId = 1
        val response = ApiErrorResponse(404, "Unauthorized")
        runTest {
            Mockito.doReturn(flowOf(response)).`when`(repository).getMovieById(testId)
            testViewmodel.getMovie(testId)
            Assert.assertNull(testViewmodel.currentMovie.value)
            Assert.assertEquals("Unauthorized", testViewmodel.error.value)
        }
    }


    @After
    fun teardown() {
        mocks.close()
    }
}