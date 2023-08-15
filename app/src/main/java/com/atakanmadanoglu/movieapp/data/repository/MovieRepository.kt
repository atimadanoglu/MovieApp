package com.atakanmadanoglu.movieapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.atakanmadanoglu.movieapp.data.mapper.MovieEntityMapper
import com.atakanmadanoglu.movieapp.data.mapper.MovieUiMapper
import com.atakanmadanoglu.movieapp.data.service.local.MovieDatabase
import com.atakanmadanoglu.movieapp.data.service.remote.MovieService
import com.atakanmadanoglu.movieapp.presentation.model.MovieUI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val database: MovieDatabase,
    private val movieService: MovieService,
    private val uiMapper: MovieUiMapper,
    private val entityMapper: MovieEntityMapper
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getSearchMovieResultStream(query: String): Flow<PagingData<MovieUI>> {
        val pagingSourceFactory = {
            database.movieDao.getMoviesByQuery(query)
        }
        return Pager(
            config = PagingConfig(pageSize = 40, initialLoadSize = 80),
            remoteMediator = MovieRemoteMediator(
                query = query,
                movieService = movieService,
                movieDatabase = database,
                entityMapper = entityMapper
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { uiMapper.entityToMovieUi(it) }
        }
    }
}