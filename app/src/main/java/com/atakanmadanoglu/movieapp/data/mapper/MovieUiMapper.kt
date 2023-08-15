package com.atakanmadanoglu.movieapp.data.mapper

import com.atakanmadanoglu.movieapp.data.service.local.entity.MovieEntity
import com.atakanmadanoglu.movieapp.data.service.remote.dto.MovieDto
import com.atakanmadanoglu.movieapp.presentation.model.MovieUI
import javax.inject.Inject

class MovieUiMapper @Inject constructor() {
    fun entityToMovieUi(
        movieEntity: MovieEntity
    ): com.atakanmadanoglu.movieapp.presentation.model.MovieUI = with(movieEntity) {
        com.atakanmadanoglu.movieapp.presentation.model.MovieUI(
            adult = adult,
            id = id,
            originalLanguage = originalLanguage,
            originalTitle = originalTitle,
            overview = overview,
            popularity = popularity,
            posterPath = posterPath,
            releaseDate = releaseDate,
            title = title
        )
    }

    fun dtoToMovieUi(
        movieDto: MovieDto
    ): MovieUI = with(movieDto) {
        MovieUI(
            adult = adult,
            id = id,
            originalLanguage = original_language,
            originalTitle = original_title,
            overview = overview,
            popularity = popularity,
            posterPath = poster_path ?: "",
            releaseDate = release_date,
            title = title
        )
    }
}