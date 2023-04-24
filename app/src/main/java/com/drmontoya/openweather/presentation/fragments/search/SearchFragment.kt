package com.drmontoya.openweather.presentation.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.drmontoya.openweather.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchResultsListAdapter: SearchResultsListAdapter
    private lateinit var savedResultsAdapter: SearchResultsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchResultsListAdapter = SearchResultsListAdapter(
            viewType = SearchResultsListAdapter.VIEW_TYPE_RESULT,
            onClick = { location ->
                viewModel.saveLocationToDatabase(location)
            })
        savedResultsAdapter = SearchResultsListAdapter(
            viewType = SearchResultsListAdapter.VIEW_TYPE_SAVED,
            onClick = { location ->
                viewModel.removeSavedLocation(location)
                if(location.name == SearchViewModel.CLEAR_ALL_ITEM)
                    viewModel.clearAllSavedItems()
            }
        )
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        setupRecyclerView()
        setupObservers()
        setupUiObservers()
        return binding.root
    }

    private fun setupUiObservers() {
        binding.searchView.editText.doOnTextChanged { text, start, before, count ->
            text?.let {
                if (it.isNotEmpty() && it.isNotBlank()) {
                    viewModel.performSearch(it.toString())
                } else {
                    viewModel.postEmptyList()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.searchResultsRecyclerView
        recyclerView.adapter = searchResultsListAdapter
        binding.savedLocationsRecyclerView.adapter = savedResultsAdapter
    }

    private fun setupObservers() {
        viewModel.savedLocations.observe(viewLifecycleOwner) {
            savedResultsAdapter.submitList(it)
        }
        viewModel.searchResults.observe(viewLifecycleOwner) {
            searchResultsListAdapter.submitList(it)
            setupStateByLoadingState(it.isEmpty())
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.dummyRes.visibility = View.VISIBLE
                binding.dummyRes.text = it.toString()
            } else {
                binding.dummyRes.visibility = View.INVISIBLE
            }

        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            setupStateByLoadingState(it && viewModel.searchResults.value?.isEmpty() ?: true)
        }
    }

    private fun setupStateByLoadingState(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingStateScreen.root.visibility = View.VISIBLE
        } else {
            binding.loadingStateScreen.root.visibility = View.INVISIBLE
        }
    }

}