package com.atakanmadanoglu.movieapp

import android.app.Application
import com.atakanmadanoglu.movieapp.data.repository.MovieRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MovieApplication : Application() {
    @Inject lateinit var movieRepository: MovieRepository
}