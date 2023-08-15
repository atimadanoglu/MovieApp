package com.atakanmadanoglu.movieapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.atakanmadanoglu.movieapp.MovieApplication
import com.atakanmadanoglu.movieapp.data.repository.MovieRepository
import com.atakanmadanoglu.movieapp.presentation.model.MovieUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SearchMovieUiState(
    val searchQuery: String = "samurai"
)

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
): ViewModel() {

    private val _searchMovieUiState = MutableStateFlow(SearchMovieUiState())
    val uiState: StateFlow<SearchMovieUiState> get() = _searchMovieUiState

    val pager = movieRepository
        .getSearchMovieResultStream(_searchMovieUiState.value.searchQuery)
        .cachedIn(viewModelScope)

    fun setSearchQuery(newQuery: String) {
        _searchMovieUiState.update {
            it.copy(searchQuery = newQuery)
        }
    }

    fun searchMovies() {
        val searchQuery = uiState.value.searchQuery
        movieRepository.getSearchMovieResultStream(searchQuery)
    }

    companion object {
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
    }
}