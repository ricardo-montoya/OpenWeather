package com.drmontoya.openweather.domain.model

data class DayForecastData(
    val date : String,
    val tempMax : Double,
    val tempMin : Double,
    val precipitaitonProbability : Int
)
