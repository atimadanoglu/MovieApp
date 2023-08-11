package com.atakanmadanoglu.movieapp.data.mapper

import com.atakanmadanoglu.movieapp.data.service.local.entity.MovieEntity
import com.atakanmadanoglu.movieapp.data.service.remote.dto.MovieDto
import javax.inject.Inject

class MovieDtoToEntityMapper @Inject constructor() {
    private fun mapToMovieEntity(
        movieDto: MovieDto
    ): MovieEntity = with(movieDto) {
        MovieEntity(
            adult = adult,
            id = id,
            originalLanguage = original_language,
            originalTitle = original_title,
            overview = overview,
            popularity = popularity,
            posterPath = poster_path ?:"",
            releaseDate = release_date,
            title = title
        )
    }

    fun mapToMovieEntityList(
        movieDtoList: List<MovieDto>
    ): List<MovieEntity> = movieDtoList.map {
        mapToMovieEntity(it)
    }
}