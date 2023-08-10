package com.atakanmadanoglu.movieapp.data.service.remote.dto

data class MovieDto(
    val adult: Boolean,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String
)