package com.atakanmadanoglu.movieapp.data.mapper

import com.atakanmadanoglu.movieapp.data.service.local.entity.MovieEntity
import com.atakanmadanoglu.movieapp.presentation.model.MovieUI
import javax.inject.Inject

class MovieEntityToUiMapper @Inject constructor() {
    fun mapToMovieUi(
        movieEntity: MovieEntity
    ): MovieUI = with(movieEntity) {
        MovieUI(
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
}