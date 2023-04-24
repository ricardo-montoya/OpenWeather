package com.drmontoya.openweather.domain.model

data class DetailHourlyForecast(
    val date : String,
    val temperature : Double,
    val precipitationProbability : Int
)
