package com.atakanmadanoglu.movieapp.data.service.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.atakanmadanoglu.movieapp.data.service.local.entity.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = MovieDatabase.DB_VERSION,
    exportSchema = false
)
abstract class MovieDatabase: RoomDatabase() {
    abstract val movieDao: MovieDao
    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "movie-database"
    }
}