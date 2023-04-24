package com.drmontoya.openweather.presentation.fragments.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drmontoya.openweather.data.local.entity.asDTO
import com.drmontoya.openweather.data.network.dto.ForecastDTO
import com.drmontoya.openweather.domain.model.DetailHourlyForecast
import com.drmontoya.openweather.domain.repository.ForecastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel
@Inject constructor(
    val forecastRepository: ForecastRepository
) : ViewModel() {
    private val _detailHourlyForecastList = MutableLiveData<List<DetailHourlyForecast>>()
    val detailHourlyForecastList: LiveData<List<DetailHourlyForecast>>
        get() = _detailHourlyForecastList

    private val _currentForecast = MutableLiveData<ForecastDTO>()
    val currentForecast: LiveData<ForecastDTO>
        get() = _currentForecast


    fun loadCurrentForecast(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _currentForecast.postValue(
                    forecastRepository.getForecastByLatitudeAndLongitudeLocal(
                        latitude = latitude,
                        longitude = longitude
                    )?.asDTO()
                )
            }
        }
    }

    fun loadDetailHourlyForecast() {
        _detailHourlyForecastList.postValue(
            mapDataToDetailHourlyForecast()
        )
    }

    private fun mapDataToDetailHourlyForecast(): List<DetailHourlyForecast>? {
        return _currentForecast.value?.hourly?.let { data ->
            data.time.zip(data.temperature_2m).zip(data.precipitation_probability)
                .map { (timeAndTemp, precipitation) ->
                    val (time, temp) = timeAndTemp
                    DetailHourlyForecast(
                        temperature = temp,
                        date = time,
                        precipitationProbability = precipitation
                    )
                }
        }
    }
}