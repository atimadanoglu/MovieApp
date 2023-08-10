package com.atakanmadanoglu.movieapp.data.service.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.atakanmadanoglu.movieapp.data.service.local.entity.MovieEntity

@Dao
interface MovieDao {
    @Upsert
    suspend fun upsertMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM movie")
    fun getMovies(): PagingSource<Int, MovieEntity>

    @Query("DELETE FROM movie")
    suspend fun deleteAll()
}