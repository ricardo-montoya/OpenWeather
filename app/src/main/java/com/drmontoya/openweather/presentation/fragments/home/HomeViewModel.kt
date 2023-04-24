package com.drmontoya.openweather.presentation.fragments.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drmontoya.openweather.R
import com.drmontoya.openweather.data.local.entity.asDTO
import com.drmontoya.openweather.data.local.entity.asDomainModel
import com.drmontoya.openweather.data.network.dto.ForecastDTO
import com.drmontoya.openweather.domain.model.DayForecastData
import com.drmontoya.openweather.domain.model.Location
import com.drmontoya.openweather.domain.model.receiver.AlarmReceiver
import com.drmontoya.openweather.domain.notification.utils.NotificationConstants
import com.drmontoya.openweather.domain.repository.ForecastRepository
import com.drmontoya.openweather.domain.use_case.GetSavedLocationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val getSavedLocationsUseCase: GetSavedLocationsUseCase,
    val forecastRepository: ForecastRepository
) : ViewModel() {
    private val _selectedForecast = MutableLiveData<ForecastDTO?>()
    val selectedForecast: LiveData<ForecastDTO?>
        get() = _selectedForecast

    private val _selectedLocation = MutableLiveData<Location?>()
    val selectedLocation: LiveData<Location?>
        get() = _selectedLocation

    private val _savedLocations = MutableLiveData<List<Location?>>()
    val savedLocations: LiveData<List<Location?>>
        get() = _savedLocations

    private val _forwardDaysForecast = MutableLiveData<List<DayForecastData>?>()
    val forwardDaysForecast: LiveData<List<DayForecastData>?>
        get() = _forwardDaysForecast

    private val second = 1_000L


    init {
        loadSavedLocations()
    }

    fun loadSavedLocations() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getSavedLocationsUseCase().collect { list ->
                    _savedLocations.postValue(list.map { it.asDomainModel() })
                }
            }
        }
    }

    private fun loadCurrentForecast(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                forecastRepository.upsertForecastWithLatitudeAndLongitude(
                    latitude = latitude, longitude = longitude
                )
                _selectedForecast.postValue(
                    forecastRepository.getForecastByLatitudeAndLongitudeLocal(
                        latitude = latitude, longitude = longitude
                    )?.asDTO()
                )
            }
        }
    }

    fun updateSelection(id: Int) {
        val selectedItem = savedLocations.value?.find { it?.id == id }
        if (selectedItem != null) {
            loadCurrentForecast(
                latitude = selectedItem.latitude!!, longitude = selectedItem.longitude!!
            )
            _selectedLocation.postValue(selectedItem)
        } else {
            if (!savedLocations.value.isNullOrEmpty()) {
                updateSelection(savedLocations.value!!.last()!!.id!!)
            }
        }
    }

    fun getCurrentWeatherStateForImage(): WeatherState {
        var currentWeatherState: WeatherState? = null
        selectedForecast.value?.let { forecast ->
            val index = selectedForecast.value?.hourly?.time?.let { getCurrentTimeIndex(it) } ?: 0
            currentWeatherState = when (forecast.hourly.precipitation_probability[index]) {
                in (21..60) -> WeatherState.Breeze
                in (61..101) -> WeatherState.Cloudy
                else -> WeatherState.Sunny
            }
        }
        return currentWeatherState ?: WeatherState.Sunny
    }

    fun getCurrentTimeIndex(list: List<String>): Int? {
        val currentTime = Calendar.getInstance()
        val currentYear = currentTime.get(Calendar.YEAR)
        val currentMonth = currentTime.get(Calendar.MONTH) + 1
        val currentDay = currentTime.get(Calendar.DAY_OF_MONTH)
        val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)

        for ((index, strDate) in list.withIndex()) {
            val dateArr = strDate.split("T")[0].split("-")
            val year = dateArr[0].toInt()
            val month = dateArr[1].toInt()
            val day = dateArr[2].toInt()
            val hour = strDate.split("T")[1].split(":")[0].toInt()

            if (currentYear == year && currentMonth == month && currentDay == day && currentHour == hour) {
                return index
            }
        }

        return null
    }

    fun updateForwardForecastList() {
        if (_selectedForecast.value != null) {
            val data = _selectedForecast.value?.daily
            data?.let {
                val forecasts = data.time.zip(data.temperature_2m_max).zip(data.temperature_2m_min)
                    .zip(data.precipitation_probability_max)
                    .map { (timeTempMaxAndTempMin, precipitation) ->
                        val (timeTempMax, tempMin) = timeTempMaxAndTempMin
                        val (time, tempMax) = timeTempMax
                        DayForecastData(
                            date = time,
                            tempMax = tempMax,
                            tempMin = tempMin,
                            precipitaitonProbability = precipitation
                        )
                    }
                _forwardDaysForecast.postValue(forecasts)
            }
        }
    }

    //Format

    //Notifications
    fun sendNotification(context: Context) {
        val location = selectedLocation.value
        val forecast = selectedForecast.value
        if (location != null && forecast != null) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val notifyIntent = Intent(context, AlarmReceiver::class.java)
            notifyIntent.putExtra(
                NotificationConstants.TITLE,
                buildNotificationTitle(forecast, location)
            )
            notifyIntent.putExtra(NotificationConstants.BODY, buildNotificationBody(forecast))
            notifyIntent.putExtra(NotificationConstants.IMAGE, getTodaysWeatherImage(forecast))
            val notificationPendingIntent = PendingIntent.getBroadcast(
                context,
                NotificationConstants.REQUEST_CODE,
                notifyIntent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val elapsedTime = SystemClock.elapsedRealtime()
            AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                elapsedTime + (second * 10),
                notificationPendingIntent
            )
            Log.i("DEBUG", "Registration done")
        }
    }

    companion object {

        fun getIndexOfToday(forecast: ForecastDTO): Int {
            val dates = forecast.daily.time
            val today = Date()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getDefault()

            for ((index, dateStr) in dates.withIndex()) {
                val date = dateFormat.parse(dateStr)
                if (date == today) {
                    return index
                }
            }

            return 0
        }

        fun buildNotificationTitle(forecast: ForecastDTO, location: Location): String {
            val data = forecast.daily
            val temperatureMin = data.temperature_2m_min[getIndexOfToday(forecast)]
            val temperatureMax = data.temperature_2m_max[getIndexOfToday(forecast)]
            val name = location.name
            return "$temperatureMin° to $temperatureMax° for $name"
        }

        fun buildNotificationBody(forecast: ForecastDTO): String {
            val data =
                forecast.daily.precipitation_probability_max[getIndexOfToday(forecast)]
            return "Precipitation probability: $data%"
        }

        fun getTodaysWeatherImage(forecast: ForecastDTO): Int {
            val data =
                forecast.daily.precipitation_probability_max[getIndexOfToday(forecast)]
            return when (data) {
                in (0..20) -> R.drawable.sunny
                in (21..60) -> R.drawable.cloudy
                else -> R.drawable.breeze
            }
        }
    }
}

sealed class WeatherState() {
    object Sunny : WeatherState()
    object Cloudy : WeatherState()
    object Breeze : WeatherState()
}