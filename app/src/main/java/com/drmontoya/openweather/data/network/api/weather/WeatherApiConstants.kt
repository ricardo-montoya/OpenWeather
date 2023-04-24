package com.drmontoya.openweather.data.network.api.weather

class WeatherApiConstants {
    companion object {
        const val GEO_BASE_URL = "https://geocoding-api.open-meteo.com/"
        const val GEO = "geo"
        const val FORECAST_BASE_URL = "https://api.open-meteo.com/"
        const val FORECAST = "forecast"
        const val LOCATIONS_CITY_AUTOCOMPLETE_END_POINT = "v1/search?count=10&format=json"
        const val LANGUAGE_FIELD_NAME = "language"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val FORECAST_END_POINT =
            "v1/forecast?hourly=temperature_2m,precipitation_probability,precipitation&daily=temperature_2m_max,temperature_2m_min,precipitation_probability_max&timezone=auto"
    }
}