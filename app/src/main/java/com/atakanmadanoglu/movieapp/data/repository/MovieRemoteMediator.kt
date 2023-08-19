package com.atakanmadanoglu.movieapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.atakanmadanoglu.movieapp.data.mapper.MovieEntityMapper
import com.atakanmadanoglu.movieapp.data.mapper.MovieUiMapper
import com.atakanmadanoglu.movieapp.data.service.local.MovieDatabase
import com.atakanmadanoglu.movieapp.data.service.local.entity.MovieEntity
import com.atakanmadanoglu.movieapp.data.service.local.entity.RemoteKeyEntity
import com.atakanmadanoglu.movieapp.data.service.remote.MovieService
import okio.IOException
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val query: String,
    private val movieService: MovieService,
    private val movieDatabase: MovieDatabase,
    private val uiMapper: MovieUiMapper,
    private val entityMapper: MovieEntityMapper
): RemoteMediator<Int, MovieEntity>() {
    private val movieDao = movieDatabase.movieDao
    private val remoteKeyDao = movieDatabase.remoteKeyDao
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                println("refresh")
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                println("prepend")
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                println("append")
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        try {
            state.pages
            val response = movieService.getMoviesByQuery(
                query = query, page = page
            )

            val movieUiList = response.results.map {
                uiMapper.dtoToMovieUi(it)
            }
            val movieEntityList = response.results.let {
                entityMapper.mapToMovieEntityList(it, query)
            }

            val endOfPaginationReached = movieEntityList.isEmpty()

            val prevKey = if (page == 1) null else (page - 1)
            val nextKey = if (endOfPaginationReached) null  else (page + 1)

            val keys = movieEntityList.map {
                RemoteKeyEntity(it.id, prevKey, nextKey)
            }

            movieDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearRemoteKeys()
                    movieDao.deleteAll()
                }
                keys?.let { remoteKeyDao.insertAll(it) }
                movieEntityList?.let { movieDao.upsertMovies(it) }
            }
            return MediatorResult.Success(
                endOfPaginationReached = endOfPaginationReached
            )
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MovieEntity>): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let {
                remoteKeyDao.remoteKeyById(it)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, MovieEntity>): RemoteKeyEntity? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { movieEntity -> remoteKeyDao.remoteKeyById(movieEntity.id) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, MovieEntity>): RemoteKeyEntity? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { movieEntity -> remoteKeyDao.remoteKeyById(movieEntity.id) }
    }
}