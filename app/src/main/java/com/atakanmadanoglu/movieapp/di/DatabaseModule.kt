package com.atakanmadanoglu.movieapp.di

import android.content.Context
import androidx.room.Room
import com.atakanmadanoglu.movieapp.data.service.local.MovieDao
import com.atakanmadanoglu.movieapp.data.service.local.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun getMovieDatabase(
        @ApplicationContext context: Context
    ): MovieDatabase = Room.databaseBuilder(
        context, MovieDatabase::class.java, MovieDatabase.DB_NAME
    ).build()

    @Provides
    @Singleton
    fun getMovieDao(
        movieDatabase: MovieDatabase
    ): MovieDao = movieDatabase.movieDao
}