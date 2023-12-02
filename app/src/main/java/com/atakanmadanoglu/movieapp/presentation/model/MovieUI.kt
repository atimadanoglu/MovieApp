package com.atakanmadanoglu.movieapp.presentation.model

data class MovieUI(
    val adult: Boolean,
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String
)