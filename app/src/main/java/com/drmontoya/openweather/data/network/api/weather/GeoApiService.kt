package com.drmontoya.openweather.data.network.api.weather

import com.drmontoya.openweather.data.network.dto.LocationsContainer
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoApiService {
    @GET(WeatherApiConstants.LOCATIONS_CITY_AUTOCOMPLETE_END_POINT)
    suspend fun getCompletionOptionsByKeyword(
        @Query("name") keyword: String,
        @Query(WeatherApiConstants.LANGUAGE_FIELD_NAME) language: String
    ): Response<LocationsContainer>

}