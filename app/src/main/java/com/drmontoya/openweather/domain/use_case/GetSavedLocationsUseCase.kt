package com.drmontoya.openweather.domain.use_case

import com.drmontoya.openweather.data.local.entity.LocationEntity
import com.drmontoya.openweather.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedLocationsUseCase @Inject constructor(
    val repository : LocationRepository
) {
    operator fun invoke(): Flow<List<LocationEntity>> {
        return repository.getSavedLocations()
    }
}