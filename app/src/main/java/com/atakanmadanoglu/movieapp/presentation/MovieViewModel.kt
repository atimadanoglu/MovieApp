package com.atakanmadanoglu.movieapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.atakanmadanoglu.movieapp.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class SearchMovieUiState(
    val searchQuery: String = "samurai",
    val retrieveNewData: Boolean = false
)

class MovieViewModel(
    private val movieRepository: MovieRepository
): ViewModel() {

    private val _searchMovieUiState = MutableStateFlow(SearchMovieUiState())
    val uiState: StateFlow<SearchMovieUiState> get() = _searchMovieUiState


    /*val pager = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { MoviePagingSource(
            movieService, uiMapper, _searchMovieUiState.value.searchQuery
        )}
    ).flow.cachedIn(viewModelScope)*/

    val pager = getSearchMovieResultStream()
        .cachedIn(viewModelScope)

    /*val pager = movieRepository
        .getSearchMovieResultStreamWithPagingSource(_searchMovieUiState.value.searchQuery)
        .cachedIn(viewModelScope)*/

    fun setSearchQuery(newQuery: String) {
        _searchMovieUiState.update {
            it.copy(searchQuery = newQuery)
        }
    }

    fun getSearchMovieResultStream() =
        movieRepository.getSearchMovieResultStream(uiState.value.searchQuery)

    fun setRetrieveNewData() {
        val searchQuery = uiState.value.searchQuery
        if (searchQuery.length > 2) {
            _searchMovieUiState.update {
                it.copy(retrieveNewData = true)
            }
        }
    }

   /* companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras

                return MovieViewModel(
                    movieRepository = (application as MovieApplication).movieRepository
                ) as T
            }
        }
    }*/
}