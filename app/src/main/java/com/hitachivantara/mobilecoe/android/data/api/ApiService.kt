package com.hitachivantara.mobilecoe.android.data.api
import com.hitachivantara.mobilecoe.android.data.model.Movie
import com.hitachivantara.mobilecoe.android.data.model.Review
import com.hitachivantara.mobilecoe.android.data.model.response.CastResponseList
import com.hitachivantara.mobilecoe.android.data.model.response.ResponseList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("page") page: Int? = 1,
        @Query("language") language: String? = "en-US"
    ): Call<ResponseList<Movie>>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("page") page: Int? = 1,
        @Query("language") language: String? = "en-US"
    ):Call<ResponseList<Movie>>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("page") page: Int? = 1,
        @Query("language") language: String? = "en-US"
    ): Call<ResponseList<Movie>>

    @GET("movie/upcoming")
    fun getUpComingMovies(
        @Query("page") page: Int? = 1,
        @Query("language") language: String? = "en-US"
    ): Call<ResponseList<Movie>>

    @GET("movie/{movieId}/recommendations")
    fun getRelatedMovies(
        @Path("movie_id") movie_id: Int,
        @Query("page") page: Int? = 1
    ): Call<ResponseList<Movie>>

    @GET("movie/{movie_id}")
    fun getMovieById(
        @Path("movie_id") movie_id: Int,
        @Query("language") language: String? = "en-US"
    ): Call<Movie>

    @GET("movie/{movie_id}/reviews")
    fun getMovieReviews(
        @Path("movie_id") movie_id: Int,
        @Query("page") page: Int? = 1,
        @Query("language") language: String? = "en-US"
    ): Call<ResponseList<Review>>


    @GET("movie/{movie_id}/credits")
    fun getMovieCasts(
        @Path("movie_id") movie_id: Int,
        @Query("language") language: String? = "en-US"
    ): Call<CastResponseList>

    @GET("search/movie")
    fun searchMovies(
        @Query("query") query: String,
        @Query("include_adult") include_adult: Boolean? = true,
        @Query("language") language: String? = "en-US",
        @Query("page") page: Int? = 1,
    ): Call<ResponseList<Movie>>
}