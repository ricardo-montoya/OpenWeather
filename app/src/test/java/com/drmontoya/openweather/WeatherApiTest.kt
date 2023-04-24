package com.drmontoya.openweather

import com.drmontoya.openweather.data.network.api.weather.GeoApiService
import com.drmontoya.openweather.data.network.api.weather.WeatherApiConstants
import com.drmontoya.openweather.data.network.api.weather.WeatherApiService
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherApiTest {

    lateinit var geoApiService: GeoApiService
    lateinit var weatherApiService: WeatherApiService

    @Before
    fun setup() {
        geoApiService = Retrofit.Builder()
            .baseUrl(WeatherApiConstants.GEO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeoApiService::class.java)
        weatherApiService = Retrofit.Builder()
            .baseUrl(WeatherApiConstants.FORECAST_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    @Test
    fun getLocationsCompletionsFromApiByKeyWord_ShouldReturnListItems() {
        val keyword = "Mex"
        val response = runBlocking {
            geoApiService.getCompletionOptionsByKeyword(
                keyword = keyword,
                language = "en"
            )
        }
        Assert.assertEquals(true, response.body()?.results?.isNotEmpty())
    }

    @Test
    fun getLocationsShouldReturnNothing() {
        val keyword = "153136"
        val response = runBlocking {
            geoApiService.getCompletionOptionsByKeyword(
                keyword = keyword,
                language = "en"
            )
        }
        Assert.assertEquals(null, response.body()?.results)
    }


}

