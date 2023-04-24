package com.drmontoya.openweather.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.drmontoya.openweather.data.local.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM locationentity")
    fun getAllLocations(): Flow<List<LocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationEntity)

    @Delete
    suspend fun deleteLocation(location: LocationEntity)

    @Query("DELETE FROM locationentity")
    suspend fun clearLocations()

    @Query("SELECT * FROM locationentity WHERE id = :id LIMIT 1")
    suspend fun getLocationById(id: Int): LocationEntity
}