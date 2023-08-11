package com.atakanmadanoglu.movieapp.data.service.remote

import com.atakanmadanoglu.movieapp.data.service.remote.dto.MovieDetailDto
import com.atakanmadanoglu.movieapp.data.service.remote.dto.MoviesDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("search/movie")
    suspend fun getMoviesByQuery(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): MoviesDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetailsById(
        @Path("movie_id") id: Int,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US"
    ): MovieDetailDto
}