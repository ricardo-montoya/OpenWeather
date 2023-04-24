package com.drmontoya.openweather.presentation.fragments.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.drmontoya.openweather.R
import com.drmontoya.openweather.data.network.dto.ForecastDTO
import com.drmontoya.openweather.databinding.FragmentHomeBinding
import com.drmontoya.openweather.domain.model.Location
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.lang.StringBuilder

@AndroidEntryPoint
class HomeFragment : Fragment() {

    val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    val dayForecastAdapter = DayForecastAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupToolbar()
        setupNotificationChannels()
        setupUiComponents()
        setupUiListeners()
        setupViewModelObserver()
        return binding.root
    }

    private fun setupNotificationChannels() {
        createChannel(
            channelId = requireContext().getString(R.string.todays_forecast_channel_id),
            channelName = requireContext().getString(R.string.todays_forecast_channel_name)
        )
    }

    private fun setupUiComponents() {
        binding.forwardForecastList.adapter = dayForecastAdapter
    }

    private fun setupUiListeners() {
        binding.selectLocationButton.setOnClickListener {
            displayLocationsDialog()
        }
        binding.seeMoreButton.setOnClickListener {
            viewModel.selectedForecast.value?.let {
                this.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                        latitude = it.latitude.toFloat(), longitude = it.longitude.toFloat()
                    )
                )
            }
        }
    }

    private fun setupViewModelObserver() {
        viewModel.selectedForecast.observe(viewLifecycleOwner) { forecast ->
            if (forecast != null) {
                binding.shimmerOverlay.root.visibility = View.INVISIBLE
                viewModel.updateForwardForecastList()
                loadWeatherViewsFromForecast(forecast)
                viewModel.sendNotification(requireContext())
            } else {
                binding.shimmerOverlay.root.visibility = View.VISIBLE
                clearWeatherViews()
            }
        }
        viewModel.selectedLocation.observe(viewLifecycleOwner) { location ->
            location?.let {
                binding.locationNameTextView.text = "${it.name}"
            }
        }
        viewModel.savedLocations.observe(viewLifecycleOwner) {
            viewModel.updateSelection(getSelectedLocation())
        }
        viewModel.forwardDaysForecast.observe(viewLifecycleOwner) {
            dayForecastAdapter.submitList(it)
        }
    }

    private fun clearWeatherViews() {
        binding.locationNameTextView.visibility = View.INVISIBLE
        binding.currentTemperatureTextView.text = ""
        binding.seeMoreButton.visibility = View.INVISIBLE
        binding.weatherImageView.visibility = View.INVISIBLE
    }

    private fun loadWeatherViewsFromForecast(forecast: ForecastDTO) {
        val index = viewModel.getCurrentTimeIndex(forecast.hourly.time) ?: 0
        binding.locationNameTextView.visibility = View.INVISIBLE
        binding.weatherImageView.visibility = View.VISIBLE
        binding.seeMoreButton.visibility = View.VISIBLE
        binding.currentTemperatureTextView.text =
            StringBuilder().append(forecast.hourly.temperature_2m[index]).append("°C")
        binding.weatherImageView.setImageResource(
            when (viewModel.getCurrentWeatherStateForImage()) {
                WeatherState.Breeze -> R.drawable.breeze
                WeatherState.Cloudy -> R.drawable.cloudy
                WeatherState.Sunny -> R.drawable.sunny
            }
        )
    }

    private fun displayLocationsDialog() {
        val savedLocations = viewModel.savedLocations.value
        if (!savedLocations.isNullOrEmpty()) {
            displayLocationsDialogWithList(savedLocations as List<Location>)
        } else {
            displayLocationsDialogEmpty()
        }
    }

    private fun displayLocationsDialogWithList(items: List<Location>) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(SELECT_A_LOCATION)
        builder.setItems(items.map { it.name }.toTypedArray()) { _, which ->
            val selectedItem = items[which]
            selectedItem.id?.let {
                viewModel.updateSelection(it)
                updateSelectedLocation(it)
            }
        }
        builder.show()

    }

    private fun displayLocationsDialogEmpty() {
        Snackbar.make(requireView(), YOU_HAVE_NO_SELECTED_ITEMS, Snackbar.LENGTH_SHORT).show()
    }

    private fun setupToolbar() {
        val toolBar = binding.toolBar
        val navController = NavHostFragment.findNavController(this)
        toolBar.setOnMenuItemClickListener {
            navController.navigate(it.itemId)
            true
        }
        binding.toolBar.apply {
            navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_search)
            setNavigationOnClickListener {
                navController.navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
            }
        }
    }

    private fun updateSelectedLocation(id: Int) {
        Log.i("DEBUG", "$id")
        val sharedPref =
            requireContext().getSharedPreferences(LOCAL_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt(SELECTED_LOCATION, id)
        editor.apply()
    }

    private fun getSelectedLocation(): Int {
        val sharedPref =
            requireContext().getSharedPreferences(LOCAL_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPref.getInt(SELECTED_LOCATION, 0)
    }

    //Notification
    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.description = "Today's weather info"
            val notificationmanager =
                requireActivity().getSystemService(NotificationManager::class.java)
            notificationmanager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        const val LOCAL_PREFERENCES = "local_preferences"
        const val SELECTED_LOCATION = "selected_location"
        const val SELECT_A_LOCATION = "Select a location"
        const val YOU_HAVE_NO_SELECTED_ITEMS = "You haven't selected a location yet"
    }
}