package com.drmontoya.openweather.data.network.dto

import com.drmontoya.openweather.domain.model.Location

data class LocationsContainer(
    val generationtime_ms: Double,
    val results: List<LocationDTO>
)

data class LocationDTO(
    val admin1: String,
    val admin1_id: Int,
    val admin2: String,
    val admin2_id: Int,
    val admin3: String,
    val admin3_id: Int,
    val country: String,
    val country_code: String,
    val country_id: Int,
    val elevation: Double,
    val feature_code: String,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val population: Int,
    val postcodes: List<String>,
    val timezone: String
)

fun LocationDTO.asDomainModel(): Location {
    return Location(
        admin1,
        admin2,
        admin3,
        country,
        elevation,
        id,
        latitude,
        longitude,
        name,
        population,
        timezone
    )
}