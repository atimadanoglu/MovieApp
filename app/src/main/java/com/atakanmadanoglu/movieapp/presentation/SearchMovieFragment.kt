package com.atakanmadanoglu.movieapp.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.atakanmadanoglu.movieapp.databinding.FragmentSearchMovieBinding
import com.atakanmadanoglu.movieapp.presentation.adapter.SearchedMovieAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchMovieFragment : Fragment() {
    private var _binding: FragmentSearchMovieBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchedMovieAdapter: SearchedMovieAdapter
    private val viewModel: MovieViewModel by viewModels { MovieViewModel.Factory }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchMovieBinding.inflate(inflater, container, false)
        searchedMovieAdapter = SearchedMovieAdapter(
            movieItemClickListener = { movieId ->
                TODO("GO TO MOVIE DETAILS")
            }
        )
        binding.searchedMoviesRecyclerView.adapter = searchedMovieAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pager.collectLatest {
                searchedMovieAdapter.submitData(it)
            }
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.setSearchQuery(query)
                }
                if (viewModel.uiState.value.searchQuery.length > 2) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.pager.collectLatest {
                            searchedMovieAdapter.submitData(it)
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    viewModel.setSearchQuery(newText)
                }
                if (viewModel.uiState.value.searchQuery.length > 2) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.pager.collectLatest {
                            searchedMovieAdapter.submitData(it)
                        }
                    }
                }
                return true
            }

        })
        return binding.root
    }
}