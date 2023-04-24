package com.drmontoya.openweather.di

import com.drmontoya.openweather.data.network.api.weather.WeatherApiConstants
import com.drmontoya.openweather.data.network.api.weather.GeoApiService
import com.drmontoya.openweather.data.network.api.weather.WeatherApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    @Singleton
    @Named(WeatherApiConstants.GEO)
    fun providesGeoApiInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WeatherApiConstants.GEO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesGeoApiService(@Named(WeatherApiConstants.GEO) geoApi: Retrofit): GeoApiService {
        return geoApi.create(GeoApiService::class.java)
    }

    @Provides
    @Singleton
    @Named(WeatherApiConstants.FORECAST)
    fun providesWeatherApiInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WeatherApiConstants.FORECAST_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesWeatherApiService(@Named(WeatherApiConstants.FORECAST) weatherApi: Retrofit): WeatherApiService {
        return weatherApi.create(WeatherApiService::class.java)
    }
}