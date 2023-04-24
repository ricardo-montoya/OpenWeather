package com.drmontoya.openweather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.drmontoya.openweather.data.local.dao.ForecastDao
import com.drmontoya.openweather.data.local.dao.LocationDao
import com.drmontoya.openweather.data.local.entity.DoubleListConverter
import com.drmontoya.openweather.data.local.entity.ForecastEntity
import com.drmontoya.openweather.data.local.entity.IntListConverter
import com.drmontoya.openweather.data.local.entity.LocationEntity
import com.drmontoya.openweather.data.local.entity.StringListConverter

@Database(
    entities = [LocationEntity::class, ForecastEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    IntListConverter::class,
    StringListConverter::class,
    DoubleListConverter::class
)
abstract class Database : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun forecastDao(): ForecastDao
}