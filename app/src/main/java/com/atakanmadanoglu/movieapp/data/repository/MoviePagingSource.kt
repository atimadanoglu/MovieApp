package com.atakanmadanoglu.movieapp.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.atakanmadanoglu.movieapp.data.mapper.MovieUiMapper
import com.atakanmadanoglu.movieapp.data.service.remote.MovieService
import com.atakanmadanoglu.movieapp.presentation.model.MovieUI
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class MoviePagingSource @Inject constructor(
    private val service: MovieService,
    private val uiMapper: MovieUiMapper,
    val query: String
): PagingSource<Int, MovieUI>() {
    override fun getRefreshKey(state: PagingState<Int, MovieUI>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieUI> {
        println("çağrıldı")
        return try {
            val page = params.key ?: 1
            val response = service.getMoviesByQuery(query, page = page)
            val movieUiList = response.results.map {
                uiMapper.dtoToMovieUi(it)
            }

            val prevKey = if (page == 1) null else (page - 1)
            val nextKey = if (movieUiList.isEmpty()) null else (page + 1)

            LoadResult.Page(
                data = movieUiList,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

}