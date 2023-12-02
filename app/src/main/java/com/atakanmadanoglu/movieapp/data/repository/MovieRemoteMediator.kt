package com.atakanmadanoglu.movieapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.atakanmadanoglu.movieapp.data.mapper.MovieDtoToEntityMapper
import com.atakanmadanoglu.movieapp.data.service.local.MovieDatabase
import com.atakanmadanoglu.movieapp.data.service.local.entity.MovieEntity
import com.atakanmadanoglu.movieapp.data.service.remote.MovieService
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator @Inject constructor(
    private val query: String = "",
    private val movieService: MovieService,
    private val movieDatabase: MovieDatabase,
    private val mapper: MovieDtoToEntityMapper
): RemoteMediator<Int, MovieEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val loadKey: Int = when(loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    lastItem.id
                }
            }
            val response = movieService.getMoviesByQuery(
                query = query, page = loadKey
            )

            val movies = mapper.mapToMovieEntityList(response.results)

            movieDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDatabase.movieDao.deleteAll()
                }
                movieDatabase.movieDao.upsertMovies(movies)
            }
            MediatorResult.Success(
                endOfPaginationReached = movies.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}