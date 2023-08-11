package com.atakanmadanoglu.movieapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.atakanmadanoglu.movieapp.data.mapper.MovieDtoToEntityMapper
import com.atakanmadanoglu.movieapp.data.mapper.MovieEntityToUiMapper
import com.atakanmadanoglu.movieapp.data.service.local.MovieDatabase
import com.atakanmadanoglu.movieapp.data.service.remote.MovieService
import com.atakanmadanoglu.movieapp.presentation.model.MovieUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val database: MovieDatabase,
    private val movieService: MovieService,
    private val movieEntityToUiMapper: MovieEntityToUiMapper,
    private val movieDtoToEntityMapper: MovieDtoToEntityMapper
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getSearchMovieResultStream(query: String): Flow<PagingData<MovieUI>> {
        val pagingSourceFactory = { database.movieDao.getMovies() }
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MovieRemoteMediator(
                query = query,
                movieService = movieService,
                movieDatabase = database,
                mapper = movieDtoToEntityMapper
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { movieEntity -> movieEntityToUiMapper.mapToMovieUi(movieEntity) }
        }
    }
}