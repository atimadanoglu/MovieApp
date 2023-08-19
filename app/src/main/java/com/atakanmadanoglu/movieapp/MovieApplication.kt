package com.atakanmadanoglu.movieapp

import android.app.Application
import com.atakanmadanoglu.movieapp.data.mapper.MovieUiMapper
import com.atakanmadanoglu.movieapp.data.repository.MovieRepository
import com.atakanmadanoglu.movieapp.data.service.remote.MovieService
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MovieApplication : Application() {
    @Inject lateinit var movieRepository: MovieRepository
    @Inject lateinit var service: MovieService
    @Inject lateinit var uiMapper: MovieUiMapper
}