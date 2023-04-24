package com.drmontoya.openweather.domain.model

import com.drmontoya.openweather.data.local.entity.LocationEntity

data class Location(
    val admin1: String?,
    val admin2: String?,
    val admin3: String?,
    val country: String?,
    val elevation: Double?,
    val id: Int?,
    val latitude: Double?,
    val longitude: Double?,
    val name: String?,
    val population: Int?,
    val timezone: String?
)

fun Location.asEntityModel(): LocationEntity {
    return LocationEntity(
        id,
        admin1,
        admin2,
        admin3,
        country,
        elevation,
        latitude,
        longitude,
        name,
        population,
        timezone
    )
}