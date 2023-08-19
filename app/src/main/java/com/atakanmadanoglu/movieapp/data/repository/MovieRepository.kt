package com.atakanmadanoglu.movieapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.atakanmadanoglu.movieapp.data.datasource.remote.MovieRemoteDataSource
import com.atakanmadanoglu.movieapp.data.mapper.MovieEntityMapper
import com.atakanmadanoglu.movieapp.data.mapper.MovieUiMapper
import com.atakanmadanoglu.movieapp.data.service.local.MovieDatabase
import com.atakanmadanoglu.movieapp.data.service.remote.MovieService
import com.atakanmadanoglu.movieapp.presentation.model.MovieUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val database: MovieDatabase,
    private val movieService: MovieService,
    private val uiMapper: MovieUiMapper,
    private val entityMapper: MovieEntityMapper,
    private val movieRemoteDataSource: MovieRemoteDataSource
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getSearchMovieResultStream(query: String): Flow<PagingData<MovieUI>> {
        return Pager(
            config = PagingConfig(pageSize = 40, initialLoadSize = 80),
            remoteMediator = MovieRemoteMediator(
                query = query,
                movieService = movieService,
                movieDatabase = database,
                uiMapper = uiMapper,
                entityMapper = entityMapper
            ),
            pagingSourceFactory = {
                database.movieDao.getMoviesByQuery(query)
            }
        ).flow.map {
            it.map {  movieEntity ->
                uiMapper.entityToMovieUi(movieEntity)
            }
        }
    }

   /* @OptIn(ExperimentalPagingApi::class)
    fun getSearchMovieResultStreamWithPagingSource(onQueryChanged: (String) -> Boolean): Flow<PagingData<MovieUI>> {
        val pagingSource = MoviePagingSource(service = )
        if (onQueryChanged())

        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = pagingSource
        ).flow
    }*/
}