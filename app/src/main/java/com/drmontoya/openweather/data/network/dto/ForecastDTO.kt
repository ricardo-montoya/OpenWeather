package com.drmontoya.openweather.data.network.dto

import com.drmontoya.openweather.data.local.entity.DailyEntity
import com.drmontoya.openweather.data.local.entity.DailyUnitsEntity
import com.drmontoya.openweather.data.local.entity.ForecastEntity
import com.drmontoya.openweather.data.local.entity.HourlyEntity
import com.drmontoya.openweather.data.local.entity.HourlyUnitsEntity

data class ForecastDTO(
    val daily: Daily,
    val daily_units: DailyUnits,
    val elevation: Double,
    val generationtime_ms: Double,
    val hourly: Hourly,
    val hourly_units: HourlyUnits,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezone_abbreviation: String,
    val utc_offset_seconds: Int
)

data class Daily(
    val precipitation_probability_max: List<Int>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val time: List<String>
)

data class DailyUnits(
    val precipitation_probability_max: String,
    val temperature_2m_max: String,
    val temperature_2m_min: String,
    val time: String
)

data class Hourly(
    val precipitation_probability: List<Int>,
    val temperature_2m: List<Double>,
    val time: List<String>
)

data class HourlyUnits(
    val precipitation: String,
    val temperature_2m: String,
    val time: String
)
fun ForecastDTO.asEntity(): ForecastEntity {
    val dailyEntity = DailyEntity(
        dailyPrecipitationProbabilityMax = daily.precipitation_probability_max,
        dailyTemperature2mMax = daily.temperature_2m_max,
        dailyTemperature2mMin = daily.temperature_2m_min,
        dailyTime = daily.time
    )
    val dailyUnitsEntity = DailyUnitsEntity(
        dailyPrecipitationProbabilityMaxUnits = daily_units.precipitation_probability_max,
        dailyTemperature2mMaxUnits = daily_units.temperature_2m_max,
        dailyTemperature2mMinUnits = daily_units.temperature_2m_min,
        dailyTimeUnits = daily_units.time
    )
    val hourlyEntity = HourlyEntity(
        hourlyPrecipitation = hourly.precipitation_probability,
        hourlyTemperature2m = hourly.temperature_2m,
        hourlyTime = hourly.time
    )
    val hourlyUnitsEntity = HourlyUnitsEntity(
        hourlyPrecipitationUnits = hourly_units.precipitation,
        hourlyTemperature2mUnits = hourly_units.temperature_2m,
        hourlyTimeUnits = hourly_units.time
    )
    return ForecastEntity(
        daily = dailyEntity,
        dailUnits = dailyUnitsEntity,
        elevation = elevation,
        generationTimeMs = generationtime_ms,
        hourly = hourlyEntity,
        hourlyUnits = hourlyUnitsEntity,
        latitude = latitude,
        longitude = longitude,
        timezone = timezone,
        timezoneAbbreviation = timezone_abbreviation,
        utcOffsetSeconds = utc_offset_seconds
    )
}
