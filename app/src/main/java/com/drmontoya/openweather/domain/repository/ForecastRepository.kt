package com.drmontoya.openweather.domain.repository

import com.drmontoya.openweather.data.local.entity.ForecastEntity
import com.drmontoya.openweather.data.network.dto.ForecastDTO

interface ForecastRepository {

    suspend fun getForecastByLatitudeAndLongitudeLocal(
        latitude: Double, longitude: Double
    ): ForecastEntity?


    suspend fun upsertForecastWithLatitudeAndLongitude(
        latitude: Double, longitude: Double
    )

    suspend fun getForecastByLongitudeAndLatitudeRemote(
        latitude: Double, longitude: Double
    ): ForecastDTO?

    suspend fun insertForecastList(forecastList: List<ForecastDTO>)
    suspend fun insertForecast(forecastDTO: ForecastDTO)

    suspend fun removeForecast(forecastDTO: ForecastDTO)
    suspend fun clearAllForecast()

}