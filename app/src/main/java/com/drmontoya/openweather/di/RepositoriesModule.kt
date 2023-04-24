package com.drmontoya.openweather.di

import com.drmontoya.openweather.data.repository.ForecastRepositoryImpl
import com.drmontoya.openweather.data.repository.LocationRepositoryImpl
import com.drmontoya.openweather.domain.repository.ForecastRepository
import com.drmontoya.openweather.domain.repository.LocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {
    @Binds
    abstract fun providesLocationRepositoryImplInstance(
        locationRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository

    @Binds
    abstract fun providesForecastRepositoryImpl(
        forecastRepositoryImpl: ForecastRepositoryImpl
    ): ForecastRepository
}