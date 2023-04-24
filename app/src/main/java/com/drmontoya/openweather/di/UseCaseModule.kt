package com.drmontoya.openweather.di

import com.drmontoya.openweather.domain.repository.LocationRepository
import com.drmontoya.openweather.domain.use_case.GetSavedLocationsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Provides
    @Singleton
    fun providesGetSavedLocationsUseCase(locationRepository: LocationRepository): GetSavedLocationsUseCase{
        return GetSavedLocationsUseCase(locationRepository)
    }
}