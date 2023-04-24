package com.drmontoya.openweather.di

import android.content.Context
import androidx.room.Room
import com.drmontoya.openweather.data.local.Database
import com.drmontoya.openweather.data.local.dao.ForecastDao
import com.drmontoya.openweather.data.local.dao.LocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Provides
    @Singleton
    fun providesRoomInstance(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(context, Database::class.java, "database").build()
    }

    @Provides
    @Singleton
    fun providesLocationDao(database: Database): LocationDao {
        return database.locationDao()
    }

    @Provides
    @Singleton
    fun providesForecastModule(database: Database): ForecastDao {
        return database.forecastDao()
    }
}