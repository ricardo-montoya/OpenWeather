package com.drmontoya.openweather.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.drmontoya.openweather.domain.model.Location

@Entity
data class LocationEntity(
    @PrimaryKey val id: Int?,
    val admin1: String?,
    val admin2: String?,
    val admin3: String?,
    val country: String?,
    val elevation: Double?,
    val latitude: Double?,
    val longitude: Double?,
    val name: String?,
    val population: Int?,
    val timezone: String?
)

fun LocationEntity.asDomainModel(): Location {
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