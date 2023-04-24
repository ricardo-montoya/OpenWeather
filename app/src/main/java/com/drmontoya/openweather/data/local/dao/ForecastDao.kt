package com.drmontoya.openweather.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.drmontoya.openweather.data.local.entity.ForecastEntity

@Dao
interface ForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllForecast(vararg forecastEntity: ForecastEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecastEntity: ForecastEntity)


    @Query("SELECT * FROM forecast ORDER BY ABS(latitude - :latitude) + ABS(longitude - :longitude) LIMIT 1")
    suspend fun getForecast(latitude: Double, longitude: Double): ForecastEntity?

    @Delete
    suspend fun removeFromDatabase(forecastEntity: ForecastEntity)

    @Query("DELETE FROM forecast")
    suspend fun clearDatabase()

}