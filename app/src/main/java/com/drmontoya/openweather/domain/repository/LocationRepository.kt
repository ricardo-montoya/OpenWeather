package com.drmontoya.openweather.domain.repository

import com.drmontoya.openweather.data.local.entity.LocationEntity
import com.drmontoya.openweather.domain.Resource
import com.drmontoya.openweather.domain.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getLocationsByKeyword(keyword : String) : Flow<Resource<Any>>

    suspend fun saveLocationToLocal(locationEntity: LocationEntity)

    fun getSavedLocations(): Flow<List<LocationEntity>>

    suspend fun deleteLocation(location: LocationEntity)

    suspend fun clearLocationDatabase()

    suspend fun getLocationById(id : Int): Location
}