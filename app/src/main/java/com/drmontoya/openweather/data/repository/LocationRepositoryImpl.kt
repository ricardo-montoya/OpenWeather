package com.drmontoya.openweather.data.repository

import com.drmontoya.openweather.data.local.dao.LocationDao
import com.drmontoya.openweather.data.local.entity.LocationEntity
import com.drmontoya.openweather.data.local.entity.asDomainModel
import com.drmontoya.openweather.data.network.api.weather.GeoApiService
import com.drmontoya.openweather.data.network.dto.asDomainModel
import com.drmontoya.openweather.domain.Resource
import com.drmontoya.openweather.domain.model.Location
import com.drmontoya.openweather.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    val geoApiService: GeoApiService,
    val locationDAO: LocationDao
) : LocationRepository {
    override suspend fun getLocationsByKeyword(keyword: String) =
        flow {
            emit(Resource.Loading)
            try {

                val response = geoApiService.getCompletionOptionsByKeyword(keyword, "es")
                if (response.isSuccessful) {
                    response.body()?.results?.let { list ->
                        emit(Resource.Success(list.map { it.asDomainModel() }
                            ?: emptyList<Location>()))
                    }
                } else {
                    emit(Resource.Failed(response.message()))
                }
            } catch (e: Exception) {
                emit(Resource.Failed("Make sure you are connected to the internet"))
            }
        }

    override suspend fun saveLocationToLocal(locationEntity: LocationEntity) {
        locationDAO.insertLocation(locationEntity)
    }

    override fun getSavedLocations(): Flow<List<LocationEntity>> {
        return locationDAO.getAllLocations()
    }

    override suspend fun deleteLocation(location: LocationEntity) {
        locationDAO.deleteLocation(location)
    }

    override suspend fun clearLocationDatabase() {
        locationDAO.clearLocations()
    }

    override suspend fun getLocationById(id: Int): Location {
        return locationDAO.getLocationById(id).asDomainModel()
    }
}