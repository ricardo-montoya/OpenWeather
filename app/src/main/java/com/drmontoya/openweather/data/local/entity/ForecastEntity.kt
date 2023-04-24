package com.drmontoya.openweather.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.drmontoya.openweather.data.network.dto.Daily
import com.drmontoya.openweather.data.network.dto.DailyUnits
import com.drmontoya.openweather.data.network.dto.ForecastDTO
import com.drmontoya.openweather.data.network.dto.Hourly
import com.drmontoya.openweather.data.network.dto.HourlyUnits

@Entity(tableName = "forecast")
data class ForecastEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @Embedded
    val daily: DailyEntity,
    @Embedded
    val dailUnits: DailyUnitsEntity,
    val elevation: Double,
    val generationTimeMs: Double,
    @Embedded
    val hourly: HourlyEntity,
    @Embedded
    val hourlyUnits: HourlyUnitsEntity,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val timezoneAbbreviation: String,
    val utcOffsetSeconds: Int
)

@Entity
data class DailyEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "daily_entry_id")
    val id: Int = 0,
    @TypeConverters(IntListConverter::class)
    val dailyPrecipitationProbabilityMax: List<Int>,
    @TypeConverters(DoubleListConverter::class)
    val dailyTemperature2mMax: List<Double>,
    @TypeConverters(DoubleListConverter::class)
    val dailyTemperature2mMin: List<Double>,
    @TypeConverters(StringListConverter::class)
    val dailyTime: List<String>
)

@Entity
data class DailyUnitsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "daily_units_entity")
    val id: Int = 0,
    val dailyPrecipitationProbabilityMaxUnits: String,
    val dailyTemperature2mMaxUnits: String,
    val dailyTemperature2mMinUnits: String,
    val dailyTimeUnits: String
)

@Entity
data class HourlyEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "hourly_entity")
    val id: Int = 0,
    @TypeConverters(DoubleListConverter::class)
    val hourlyPrecipitation: List<Int>,
    @TypeConverters(DoubleListConverter::class)
    val hourlyTemperature2m: List<Double>,
    @TypeConverters(StringListConverter::class)
    val hourlyTime: List<String>
)

@Entity
data class HourlyUnitsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "hourly_units_entity_id")
    val id: Int = 0,
    val hourlyPrecipitationUnits: String,
    val hourlyTemperature2mUnits: String,
    val hourlyTimeUnits: String
)
fun ForecastEntity.asDTO(): ForecastDTO {
    val dailyDTO = Daily(
        precipitation_probability_max = daily.dailyPrecipitationProbabilityMax,
        temperature_2m_max = daily.dailyTemperature2mMax,
        temperature_2m_min = daily.dailyTemperature2mMin,
        time = daily.dailyTime
    )
    val dailyUnitsDTO = DailyUnits(
        precipitation_probability_max = dailUnits.dailyPrecipitationProbabilityMaxUnits,
        temperature_2m_max = dailUnits.dailyTemperature2mMaxUnits,
        temperature_2m_min = dailUnits.dailyTemperature2mMinUnits,
        time = dailUnits.dailyTimeUnits
    )
    val hourlyDTO = Hourly(
        precipitation_probability = hourly.hourlyPrecipitation,
        temperature_2m = hourly.hourlyTemperature2m,
        time = hourly.hourlyTime
    )
    val hourlyUnitsDTO = HourlyUnits(
        precipitation = hourlyUnits.hourlyPrecipitationUnits,
        temperature_2m = hourlyUnits.hourlyTemperature2mUnits,
        time = hourlyUnits.hourlyTimeUnits
    )
    return ForecastDTO(
        daily = dailyDTO,
        daily_units = dailyUnitsDTO,
        elevation = elevation,
        generationtime_ms = generationTimeMs,
        hourly = hourlyDTO,
        hourly_units = hourlyUnitsDTO,
        latitude = latitude,
        longitude = longitude,
        timezone = timezone,
        timezone_abbreviation = timezoneAbbreviation,
        utc_offset_seconds = utcOffsetSeconds
    )
}
