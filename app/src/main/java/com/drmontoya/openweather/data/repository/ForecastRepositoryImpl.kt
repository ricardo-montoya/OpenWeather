package com.drmontoya.openweather.data.repository

import android.util.Log
import com.drmontoya.openweather.data.local.dao.ForecastDao
import com.drmontoya.openweather.data.local.entity.ForecastEntity
import com.drmontoya.openweather.data.network.api.weather.WeatherApiService
import com.drmontoya.openweather.data.network.dto.ForecastDTO
import com.drmontoya.openweather.data.network.dto.asEntity
import com.drmontoya.openweather.domain.repository.ForecastRepository
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(
    val apiService: WeatherApiService,
    val forecastDao: ForecastDao
) : ForecastRepository {
    override suspend fun getForecastByLatitudeAndLongitudeLocal(
        latitude: Double,
        longitude: Double
    ): ForecastEntity? {
        return forecastDao.getForecast(
            latitude = latitude,
            longitude = longitude
        )
    }

    override suspend fun upsertForecastWithLatitudeAndLongitude(
        latitude: Double,
        longitude: Double
    ) {
        val retrievedForecast =
            getForecastByLongitudeAndLatitudeRemote(latitude = latitude, longitude = longitude)
        retrievedForecast?.let {
            insertForecast(it)
            Log.i("DEBUG", "$it")
        }
    }

    override suspend fun getForecastByLongitudeAndLatitudeRemote(
        latitude: Double,
        longitude: Double
    ): ForecastDTO? {
        return try {
            val response = apiService.getForecastOfLocationWithLatitudeAndLongitude(
                latitude = latitude,
                longitude = longitude
            )
            response.body()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun insertForecastList(forecastList: List<ForecastDTO>) {
        forecastDao.insertAllForecast(*(forecastList.map { it.asEntity() }.toTypedArray()))
    }

    override suspend fun insertForecast(forecastDTO: ForecastDTO) {
        forecastDao.insertForecast(forecastDTO.asEntity())
    }

    override suspend fun removeForecast(forecastDTO: ForecastDTO) {
        forecastDao.removeFromDatabase(forecastDTO.asEntity())
    }

    override suspend fun clearAllForecast() {
        forecastDao.clearDatabase()
    }
}