package com.drmontoya.openweather.data.network.api.weather

import com.drmontoya.openweather.data.network.dto.ForecastDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET(WeatherApiConstants.FORECAST_END_POINT)
    suspend fun getForecastOfLocationWithLatitudeAndLongitude(
        @Query(WeatherApiConstants.LATITUDE) latitude: Double,
        @Query(WeatherApiConstants.LONGITUDE) longitude: Double
    ): Response<ForecastDTO>

}