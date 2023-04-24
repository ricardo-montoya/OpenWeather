package com.drmontoya.openweather.presentation.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.drmontoya.openweather.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: FragmentDetailBinding
    val listAdapter = DetailListAdapter()
    val arguments: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        setupUiComponents()
        setupViewModelObservers()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadCurrentForecast(
            latitude = arguments.latitude +0.0,
            longitude = arguments.longitude + 0.0
        )
    }

    private fun setupUiComponents() {
        binding.detailList.adapter = listAdapter
    }

    private fun setupViewModelObservers() {
        viewModel.currentForecast.observe(viewLifecycleOwner) {
            viewModel.loadDetailHourlyForecast()
        }
        viewModel.detailHourlyForecastList.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }
    }
}