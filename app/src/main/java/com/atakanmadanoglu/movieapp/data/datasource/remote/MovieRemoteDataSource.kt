package com.atakanmadanoglu.movieapp.data.datasource.remote

import com.atakanmadanoglu.movieapp.data.service.remote.MovieService

class MovieRemoteDataSource(
    private val service: MovieService
) {
    /*fun getMoviesByQuery(
        query: String,
        page: Int
    ): Flow<List<MovieDto>> = flow {
        val response = service.getMoviesByQuery(query, page = page)
        if (response.isSuccessful) {
            emit(response.body()?.results ?: emptyList())
        }
    }*/
}